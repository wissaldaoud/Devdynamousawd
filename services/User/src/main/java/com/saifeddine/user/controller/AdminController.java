package com.saifeddine.user.controller;

import com.saifeddine.user.dto.RoleRequestDTO;
import com.saifeddine.user.dto.RoleUpdateDTO;
import com.saifeddine.user.dto.UserDTO;
import com.saifeddine.user.model.Role;
import com.saifeddine.user.model.User;
import com.saifeddine.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/role-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleRequestDTO>> getPendingRoleRequests() {
        // Get users who have requested a role change (requestedRole is not null)
        return ResponseEntity.ok(userService.getPendingRoleRequests());
    }

    @PostMapping("/{userId}/handle-request")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> handleRoleRequest(
            @PathVariable Long userId,
            @RequestParam String action) {
        if (!action.equalsIgnoreCase("APPROVE") && !action.equalsIgnoreCase("REJECT")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.handleRoleRequest(userId, action));
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long userId,
            @RequestBody RoleUpdateDTO roleUpdateDTO
    ) {
        User updatedUser = userService.updateUserRole(userId, roleUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }
}
