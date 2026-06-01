package com.pos.multitanantpos.controller;

import com.pos.multitanantpos.dto.UserDTO;
import com.pos.multitanantpos.dto.UserProfile;
import com.pos.multitanantpos.model.Role;
import com.pos.multitanantpos.model.User;
import com.pos.multitanantpos.security.CustomUserDetails;
import com.pos.multitanantpos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get current logged-in user profile
    @GetMapping("/me")
    public ResponseEntity<UserProfile> current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userService.findByUsername(authentication.getName());
        UserProfile profile = new UserProfile();
        profile.setUsername(user.getUsername());
        profile.setRole(user.getRole());
        profile.setTenantName(user.getTenant() != null ? user.getTenant().getName() : null);
        return ResponseEntity.ok(profile);
    }

    // Get all users in the same tenant — ADMIN only
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails caller = (CustomUserDetails) auth.getPrincipal();

        if (caller.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        List<UserDTO> users = userService.findByTenantId(caller.getTenantId())
                .stream()
                .map(u -> new UserDTO(
                        u.getId(),
                        u.getUsername(),
                        u.getRole(),
                        u.getTenant() != null ? u.getTenant().getName() : null
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // Delete a user — ADMIN only, cannot delete themselves
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails caller = (CustomUserDetails) auth.getPrincipal();

        if (caller.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        User target = userService.findById(id);

        // Cannot delete yourself
        if (target.getUsername().equals(caller.getUsername())) {
            return ResponseEntity.status(400).build();
        }

        // Cannot delete users from other tenants
        if (!target.getTenant().getId().equals(caller.getTenantId())) {
            return ResponseEntity.status(403).build();
        }

        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
