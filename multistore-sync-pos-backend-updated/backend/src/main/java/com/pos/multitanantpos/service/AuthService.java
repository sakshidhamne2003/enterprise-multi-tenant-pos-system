package com.pos.multitanantpos.service;

import com.pos.multitanantpos.dto.AuthResponse;
import com.pos.multitanantpos.dto.LoginRequest;
import com.pos.multitanantpos.dto.RegisterRequest;
import com.pos.multitanantpos.model.Role;
import com.pos.multitanantpos.model.Tenant;
import com.pos.multitanantpos.model.User;
import com.pos.multitanantpos.repository.UserRepository;
import com.pos.multitanantpos.security.CustomUserDetails;
import com.pos.multitanantpos.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final TenantService tenantService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthService(UserService userService,
                       TenantService tenantService,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       UserRepository userRepository) {
        this.userService = userService;
        this.tenantService = tenantService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public AuthResponse register(RegisterRequest request) {
        // Validate tenant exists
        Tenant tenant = tenantService.findById(request.getTenantId());

        // Check username not already taken
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already taken.");
        }

        // If registering MANAGER or CASHIER, must be authenticated as ADMIN
        if (request.getRole() != Role.ADMIN) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isAnonymous = auth == null
                    || !auth.isAuthenticated()
                    || "anonymousUser".equals(auth.getPrincipal());

            if (isAnonymous) {
                throw new IllegalArgumentException(
                        "You must be logged in as ADMIN to register a MANAGER or CASHIER.");
            }

            CustomUserDetails caller = (CustomUserDetails) auth.getPrincipal();
            if (caller.getRole() != Role.ADMIN) {
                throw new IllegalArgumentException("Only ADMIN can register new users.");
            }

            // ADMIN can only register users for their own tenant
            if (!caller.getTenantId().equals(request.getTenantId())) {
                throw new IllegalArgumentException("You can only register users for your own store.");
            }
        }

        // Build and save user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setTenant(tenant);

        User saved = userService.save(user);
        return toAuthResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {
        String token = jwtUtil.generateToken(user.getUsername());
        String tenantName = user.getTenant() != null ? user.getTenant().getName() : null;
        Long tenantId = user.getTenant() != null ? user.getTenant().getId() : null;
        return new AuthResponse(token, user.getUsername(), user.getRole(), tenantName, tenantId);
    }
}
