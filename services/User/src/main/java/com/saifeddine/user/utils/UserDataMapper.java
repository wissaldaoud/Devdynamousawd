package com.saifeddine.user.utils;

import com.saifeddine.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe utilitaire pour transformer les données utilisateur
 * en format compatible avec le modèle de clustering
 */
@Component
public class UserDataMapper {
    private static final Logger logger = LoggerFactory.getLogger(UserDataMapper.class);

    // Spécialités et secteurs disponibles dans le système
    private static final Set<String> SPECIALTIES = new HashSet<>(Arrays.asList(
            "Data Science", "Finance", "Informatique", "Marketing", "Mathématiques"
    ));

    private static final Set<String> SECTORS = new HashSet<>(Arrays.asList(
            "Finance", "Santé", "Technologie", "Éducation", "Énergie"
    ));

    /**
     * Convertit un objet User en tableau de features pour le clustering
     * @param user L'utilisateur à convertir
     * @return Le tableau de caractéristiques au format double[]
     */
    public double[] convertUserToFeatures(User user) {
        // Créer un tableau de la taille appropriée
        // 2 (paid, verified) + nombre de spécialités + nombre de secteurs
        double[] features = new double[2 + SPECIALTIES.size() + SECTORS.size()];
        int index = 0;

        // Attributs paid et verified
        features[index++] = user.getPaid() != null ? (user.getPaid() ? 1.0 : 0.0) : -1.0;
        features[index++] = user.getVerified() != null ? (user.getVerified() ? 1.0 : 0.0) : -1.0;

        // Spécialités
        String userSpecialty = user.getSpecialty();
        for (String specialty : SPECIALTIES) {
            features[index++] = (userSpecialty != null && userSpecialty.contains(specialty)) ? 1.0 : 0.0;
        }

        // Secteurs
        String userSector = user.getSector();
        for (String sector : SECTORS) {
            features[index++] = (userSector != null && userSector.contains(sector)) ? 1.0 : 0.0;
        }

        return features;
    }

    /**
     * Normalise les caractéristiques utilisateur selon le même schéma
     * que celui utilisé lors de l'entraînement
     * @param features Les caractéristiques non normalisées
     * @return Les caractéristiques normalisées
     */
    public double[] normalizeFeatures(double[] features) {
        // Note: Dans une implémentation réelle, cette méthode utiliserait
        // le même StandardScaler que celui utilisé lors de l'entraînement
        // Pour l'instant, nous utilisons une normalisation simplifiée

        double[] normalized = new double[features.length];

        // Supposons que le StandardScaler soit appliqué ailleurs ou que les features
        // sont déjà dans le bon format pour le modèle
        System.arraycopy(features, 0, normalized, 0, features.length);

        return normalized;
    }

    /**
     * Analyse la description de la spécialité de l'utilisateur et retourne
     * les spécialités reconnues
     * @param specialtyDescription Description textuelle des spécialités
     * @return Set des spécialités reconnues
     */
    public Set<String> extractSpecialties(String specialtyDescription) {
        Set<String> detected = new HashSet<>();

        if (specialtyDescription == null || specialtyDescription.isEmpty()) {
            return detected;
        }

        for (String specialty : SPECIALTIES) {
            if (specialtyDescription.toLowerCase().contains(specialty.toLowerCase())) {
                detected.add(specialty);
            }
        }

        return detected;
    }

    /**
     * Analyse la description du secteur de l'utilisateur et retourne
     * les secteurs reconnus
     * @param sectorDescription Description textuelle des secteurs
     * @return Set des secteurs reconnus
     */
    public Set<String> extractSectors(String sectorDescription) {
        Set<String> detected = new HashSet<>();

        if (sectorDescription == null || sectorDescription.isEmpty()) {
            return detected;
        }

        for (String sector : SECTORS) {
            if (sectorDescription.toLowerCase().contains(sector.toLowerCase())) {
                detected.add(sector);
            }
        }

        return detected;
    }
}
