package com.saifeddine.user.controller;

import com.saifeddine.user.model.Role;
import com.saifeddine.user.model.User;
import com.saifeddine.user.service.RecommendationService;
import com.saifeddine.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserService userService;

    /**
     * Obtient des recommandations de profils similaires pour un utilisateur
     * @param userId ID de l'utilisateur
     * @param limit Nombre maximum de recommandations (optionnel, défaut: 5)
     * @return Liste des utilisateurs recommandés
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER', 'ENTERPRISE', 'FREELANCER')")
    public ResponseEntity<?> getRecommendedProfiles(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") int limit) {

        List<User> recommendations = recommendationService.getRecommendedProfiles(userId, limit);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("recommendations", recommendations);
        response.put("count", recommendations.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtient des recommandations de profils d'un rôle spécifique pour un utilisateur
     * @param userId ID de l'utilisateur
     * @param role Rôle des profils à recommander
     * @param limit Nombre maximum de recommandations (optionnel, défaut: 5)
     * @return Liste des utilisateurs recommandés
     */
    @GetMapping("/user/{userId}/role/{role}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'TEACHER', 'ENTERPRISE', 'FREELANCER')")
    public ResponseEntity<?> getRecommendedProfilesByRole(
            @PathVariable Long userId,
            @PathVariable Role role,
            @RequestParam(defaultValue = "5") int limit) {

        List<User> recommendations = recommendationService.getRecommendedProfilesByRole(userId, role, limit);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("role", role);
        response.put("recommendations", recommendations);
        response.put("count", recommendations.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtient des recommandations pour l'utilisateur actuellement authentifié
     * @param limit Nombre maximum de recommandations (optionnel, défaut: 5)
     * @return Liste des utilisateurs recommandés
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ENTERPRISE', 'FREELANCER')")
    public ResponseEntity<?> getRecommendationsForCurrentUser(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(required = false) Role role) {

        // En supposant que vous avez un service d'authentification qui renvoie l'utilisateur connecté
        // Cette partie dépend de votre implémentation de la sécurité
        // User currentUser = authenticationService.getCurrentUser();

        // Pour cet exemple, nous supposons que vous récupérez l'utilisateur d'une autre manière
        // Vous devrez adapter cette partie à votre implémentation

        // En attendant, renvoyons une réponse générique
        return ResponseEntity.ok(Map.of(
                "message", "Cette fonctionnalité nécessite l'accès à l'utilisateur authentifié"
        ));
    }
}
