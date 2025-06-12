package com.saifeddine.user.dto;

import com.saifeddine.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DTO pour transmettre des informations sur un cluster
 */
public class ClusterInfoDTO {
    private Integer clusterId;
    private String description;
    private Map<String, Object> clusterData;
    private int userCount;
    private List<UserSummaryDTO> topUsers;

    // Constructeur par défaut
    public ClusterInfoDTO() {
    }

    // Constructeur avec paramètres
    public ClusterInfoDTO(Integer clusterId, String description, Map<String, Object> clusterData,
                          List<User> users, int maxUsers) {
        this.clusterId = clusterId;
        this.description = description;
        this.clusterData = clusterData;
        this.userCount = users.size();

        // Prendre les premiers utilisateurs (limité par maxUsers)
        this.topUsers = users.stream()
                .limit(maxUsers)
                .map(UserSummaryDTO::fromUser)
                .collect(Collectors.toList());
    }

    // Getters et Setters
    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getClusterData() {
        return clusterData;
    }

    public void setClusterData(Map<String, Object> clusterData) {
        this.clusterData = clusterData;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public List<UserSummaryDTO> getTopUsers() {
        return topUsers;
    }

    public void setTopUsers(List<UserSummaryDTO> topUsers) {
        this.topUsers = topUsers;
    }

    /**
     * Classe DTO interne pour résumer les informations d'un utilisateur
     */
    public static class UserSummaryDTO {
        private Long id;
        private String userName;
        private String email;
        private String specialty;
        private String sector;

        // Constructeur par défaut
        public UserSummaryDTO() {
        }

        // Méthode factory pour créer un UserSummaryDTO à partir d'un User
        public static UserSummaryDTO fromUser(User user) {
            UserSummaryDTO dto = new UserSummaryDTO();
            dto.setId(user.getId());
            dto.setUserName(user.getUserName());
            dto.setEmail(user.getEmail());
            dto.setSpecialty(user.getSpecialty());
            dto.setSector(user.getSector());
            return dto;
        }

        // Getters et Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSpecialty() {
            return specialty;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public String getSector() {
            return sector;
        }

        public void setSector(String sector) {
            this.sector = sector;
        }
    }
}
