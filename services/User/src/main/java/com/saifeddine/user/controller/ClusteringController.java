package com.saifeddine.user.controller;
import com.saifeddine.user.model.User;
import com.saifeddine.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clustering")
public class ClusteringController {
    @Autowired
    private UserService userService;

    /**
     * Met à jour les clusters pour tous les utilisateurs
     * @return Nombre d'utilisateurs mis à jour
     */
    @PostMapping("/update-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateAllUserClusters() {
        int updatedCount = userService.updateAllUserClusters();

        Map<String, Object> response = new HashMap<>();
        response.put("updatedUsers", updatedCount);
        response.put("message", "Clusters mis à jour pour " + updatedCount + " utilisateurs");

        return ResponseEntity.ok(response);
    }

    /**
     * Attribue un cluster à un utilisateur spécifique
     * @param userId ID de l'utilisateur
     * @return L'utilisateur avec son cluster mis à jour
     */
    @PostMapping("/assign/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignClusterToUser(@PathVariable Long userId) {
        return userService.getUserWithClusterInfo(userId)
                .map(user -> {
                    User updatedUser = userService.assignCluster(user);
                    Map<String, Object> response = new HashMap<>();
                    response.put("user", updatedUser);
                    response.put("clusterInfo", userService.getClusterInfo(updatedUser.getCluster()));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère tous les utilisateurs d'un cluster spécifique
     * @param clusterId ID du cluster
     * @return Liste des utilisateurs dans ce cluster
     */
    @GetMapping("/users/{clusterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsersByCluster(@PathVariable Integer clusterId) {
        List<User> users = userService.getUsersByCluster(clusterId);
        return ResponseEntity.ok(users);
    }

    /**
     * Récupère des informations sur un cluster spécifique
     * @param clusterId ID du cluster
     * @return Informations sur le cluster
     */
    @GetMapping("/info/{clusterId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getClusterInfo(@PathVariable Integer clusterId) {
        Map<String, Object> clusterInfo = userService.getClusterInfo(clusterId);
        return ResponseEntity.ok(clusterInfo);
    }
    /**
     * Récupère la distribution des utilisateurs par cluster
     * @return Distribution des utilisateurs
     */
    @GetMapping("/distribution")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<Integer, Long>> getClusterDistribution() {
        Map<Integer, Long> distribution = new HashMap<>();

        // Récupérer tous les clusters
        for (int i = 0; i < 3; i++) {  // Nous supposons 3 clusters basés sur votre modèle KMeans
            List<User> usersInCluster = userService.getUsersByCluster(i);
            distribution.put(i, (long) usersInCluster.size());
        }

        return ResponseEntity.ok(distribution);
    }
}
