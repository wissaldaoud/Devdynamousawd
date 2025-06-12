package com.saifeddine.user.service;

import com.saifeddine.user.model.User;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.springframework.core.io.ClassPathResource;

import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.clusterers.SimpleKMeans;
import weka.core.SerializationHelper;

@Service
public class ClusteringService {
    private static final Logger logger = LoggerFactory.getLogger(ClusteringService.class);

    private SimpleKMeans kmeansModel;
    private Instances dataStructure;

    // Les spécialités et secteurs disponibles dans le système
    private static final Set<String> SPECIALTIES = new HashSet<>(Arrays.asList(
            "Data Science", "Finance", "Informatique", "Marketing", "Mathématiques"
    ));

    private static final Set<String> SECTORS = new HashSet<>(Arrays.asList(
            "Finance", "Santé", "Technologie", "Éducation", "Énergie"
    ));

    @PostConstruct
    public void init() {
        try {
            // Charger le modèle KMeans depuis les ressources
            InputStream modelIS = new ClassPathResource("kmeans_model.pkl").getInputStream();
            kmeansModel = (SimpleKMeans) SerializationHelper.read(modelIS);

            // Créer la structure de données pour prédire de nouveaux utilisateurs
            setupDataStructure();

            logger.info("Modèle KMeans chargé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors du chargement du modèle KMeans", e);
        }
    }

    private void setupDataStructure() {
        // Créer les attributs pour correspondre aux données d'entraînement
        java.util.ArrayList<Attribute> attributes = new java.util.ArrayList<>();

        // Ajouter les attributs numériques
        attributes.add(new Attribute("paid"));
        attributes.add(new Attribute("verified"));

        // Ajouter les attributs pour chaque spécialité
        for (String specialty : SPECIALTIES) {
            attributes.add(new Attribute("specialty_" + specialty));
        }

        // Ajouter les attributs pour chaque secteur
        for (String sector : SECTORS) {
            attributes.add(new Attribute("sector_" + sector));
        }

        // Créer la structure de données vide
        dataStructure = new Instances("UserData", attributes, 0);
    }

    public int predictCluster(User user) {
        try {
            // Créer une instance pour l'utilisateur à prédire
            double[] values = new double[dataStructure.numAttributes()];

            // Remplir avec les valeurs de l'utilisateur
            int i = 0;

            // Paid
            values[i++] = user.getPaid() != null ? (user.getPaid() ? 1.0 : 0.0) : -1.0;

            // Verified
            values[i++] = user.getVerified() != null ? (user.getVerified() ? 1.0 : 0.0) : -1.0;

            // Specialties
            String userSpecialty = user.getSpecialty();
            for (String specialty : SPECIALTIES) {
                values[i++] = (userSpecialty != null && userSpecialty.contains(specialty)) ? 1.0 : 0.0;
            }

            // Sectors
            String userSector = user.getSector();
            for (String sector : SECTORS) {
                values[i++] = (userSector != null && userSector.contains(sector)) ? 1.0 : 0.0;
            }

            // Créer l'instance
            DenseInstance instance = new DenseInstance(1.0, values);
            instance.setDataset(dataStructure);

            // Prédire le cluster
            return kmeansModel.clusterInstance(instance);
        } catch (Exception e) {
            logger.error("Erreur lors de la prédiction du cluster", e);
            return -1; // Valeur par défaut en cas d'erreur
        }
    }

    public Map<String, Object> getClusterInfo(int clusterId) {
        Map<String, Object> info = new HashMap<>();
        try {
            // Get the cluster centroids from the model
            Instances centroids = kmeansModel.getClusterCentroids();

            // Get the specific centroid for the requested clusterId
            double[] centroidValues = new double[centroids.numAttributes()];
            for (int i = 0; i < centroids.numAttributes(); i++) {
                centroidValues[i] = centroids.instance(clusterId).value(i);
            }
            info.put("centroid", centroidValues);

            // You can add descriptions based on cluster analysis
            switch (clusterId) {
                case 0:
                    info.put("description", "Profil technique orienté Informatique et Data Science");
                    break;
                case 1:
                    info.put("description", "Profil business orienté Finance et Marketing");
                    break;
                case 2:
                    info.put("description", "Profil académique orienté Éducation et Recherche");
                    break;
                default:
                    info.put("description", "Profil non classifié");
            }

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des informations du cluster", e);
            info.put("error", "Impossible de récupérer les informations du cluster");
        }
        return info;
    }
}