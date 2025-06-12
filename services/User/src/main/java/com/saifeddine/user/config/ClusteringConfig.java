package com.saifeddine.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.clustering")
public class ClusteringConfig {

    // Chemin du fichier modèle KMeans
    private String modelPath = "classpath:kmeans_model.model";

    // Nombre de clusters dans le modèle
    private int clusterCount = 3;

    // Descriptions des clusters
    private Map<Integer, String> clusterDescriptions = new HashMap<>();

    // Initialisation par défaut des descriptions de clusters
    public ClusteringConfig() {
        clusterDescriptions.put(0, "Profil technique orienté Informatique et Data Science");
        clusterDescriptions.put(1, "Profil business orienté Finance et Marketing");
        clusterDescriptions.put(2, "Profil académique orienté Éducation et Recherche");
    }

    // Getters et Setters
    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public int getClusterCount() {
        return clusterCount;
    }

    public void setClusterCount(int clusterCount) {
        this.clusterCount = clusterCount;
    }

    public Map<Integer, String> getClusterDescriptions() {
        return clusterDescriptions;
    }

    public void setClusterDescriptions(Map<Integer, String> clusterDescriptions) {
        this.clusterDescriptions = clusterDescriptions;
    }

    /**
     * Récupère la description d'un cluster spécifique
     * @param clusterId ID du cluster
     * @return Description du cluster ou description par défaut si non trouvée
     */
    public String getClusterDescription(int clusterId) {
        return clusterDescriptions.getOrDefault(clusterId, "Cluster non défini");
    }
}
