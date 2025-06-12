package com.saifeddine.user.service;

import com.saifeddine.user.model.Role;
import com.saifeddine.user.model.User;
import com.saifeddine.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Recommande des profils similaires basés sur le cluster de l'utilisateur
     * @param userId ID de l'utilisateur pour lequel générer des recommandations
     * @param limit Nombre maximum de recommandations à retourner
     * @return Liste des utilisateurs recommandés
     */
    public List<User> getRecommendedProfiles(Long userId, int limit) {
        Optional<User> userOpt = userService.getUserWithClusterInfo(userId);

        if (userOpt.isEmpty() || userOpt.get().getCluster() == null) {
            return Collections.emptyList();
        }

        User user = userOpt.get();
        Integer userCluster = user.getCluster();

        // Trouver d'autres utilisateurs dans le même cluster
        List<User> sameClusterUsers = userRepository.findByCluster(userCluster);

        // Filtrer l'utilisateur actuel et limiter le nombre de résultats
        return sameClusterUsers.stream()
                .filter(u -> !u.getId().equals(userId))
                .sorted((u1, u2) -> compareUserSimilarity(user, u1, u2))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Recommande des profils d'un certain rôle basés sur le cluster de l'utilisateur
     * @param userId ID de l'utilisateur pour lequel générer des recommandations
     * @param role Rôle des profils à recommander
     * @param limit Nombre maximum de recommandations
     * @return Liste des utilisateurs recommandés
     */
    public List<User> getRecommendedProfilesByRole(Long userId, Role role, int limit) {
        Optional<User> userOpt = userService.getUserWithClusterInfo(userId);

        if (userOpt.isEmpty() || userOpt.get().getCluster() == null) {
            return Collections.emptyList();
        }

        User user = userOpt.get();
        Integer userCluster = user.getCluster();

        // Trouver tous les utilisateurs dans le même cluster
        List<User> sameClusterUsers = userRepository.findByCluster(userCluster);

        // Filtrer par rôle, exclure l'utilisateur actuel et limiter le nombre de résultats
        return sameClusterUsers.stream()
                .filter(u -> u.getRole() == role && !u.getId().equals(userId))
                .sorted((u1, u2) -> compareUserSimilarity(user, u1, u2))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Méthode de comparaison pour trier les utilisateurs par similarité
     * Plus le score est bas, plus les utilisateurs sont similaires
     * @param reference Utilisateur de référence
     * @param user1 Premier utilisateur à comparer
     * @param user2 Deuxième utilisateur à comparer
     * @return -1 si user1 est plus similaire, 1 si user2 est plus similaire, 0 si égaux
     */
    private int compareUserSimilarity(User reference, User user1, User user2) {
        double score1 = calculateSimilarityScore(reference, user1);
        double score2 = calculateSimilarityScore(reference, user2);

        return Double.compare(score1, score2);
    }

    /**
     * Calcule un score de similarité entre deux utilisateurs
     * Plus le score est bas, plus les utilisateurs sont similaires
     * @param reference Utilisateur de référence
     * @param other Utilisateur à comparer
     * @return Score de similarité
     */
    private double calculateSimilarityScore(User reference, User other) {
        double score = 0.0;

        // Vérifier la spécialité
        if (reference.getSpecialty() != null && other.getSpecialty() != null) {
            if (!reference.getSpecialty().equals(other.getSpecialty())) {
                score += 1.0;
            }
        } else {
            score += 0.5; // Pénalité si l'une des spécialités est nulle
        }

        // Vérifier le secteur
        if (reference.getSector() != null && other.getSector() != null) {
            if (!reference.getSector().equals(other.getSector())) {
                score += 1.0;
            }
        } else {
            score += 0.5; // Pénalité si l'un des secteurs est nul
        }

        // Si le profil est vérifié, c'est un bonus (score réduit)
        if (other.getVerified() != null && other.getVerified()) {
            score -= 0.5;
        }

        return score;
    }
}
