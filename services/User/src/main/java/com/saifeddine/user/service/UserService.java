package com.saifeddine.user.service;

import com.saifeddine.user.dto.ProfileUpdateDTO;
import com.saifeddine.user.dto.RoleRequestDTO;
import com.saifeddine.user.dto.RoleUpdateDTO;
import com.saifeddine.user.dto.UserDTO;
import com.saifeddine.user.model.Role;
import com.saifeddine.user.model.User;
import com.saifeddine.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClusteringService clusteringService;



    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User createUser(@Valid UserDTO userDTO) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Map DTO to Entity
        User user = modelMapper.map(userDTO, User.class);

        // Always set role to PENDING for new registrations
        user.setRole(Role.PENDING);
        user.setRequestedRole(null);

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // No need to validate role-specific fields for new registrations
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update allowed fields
        modelMapper.map(userDTO, existingUser);

        // Prevent username/email changes
        if (!existingUser.getUserName().equals(userDTO.getUserName())) {
            throw new IllegalArgumentException("Username cannot be changed");
        }
        if (!existingUser.getEmail().equals(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email cannot be changed");
        }

        validateRoleSpecificFields(existingUser);
        return userRepository.save(existingUser);
    }

    public User requestRole(Long userId, String requestedRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Convert String to Role enum
        try {
            Role role = Role.valueOf(requestedRole.toUpperCase());

            // Check if user already has a role request
            if(user.getRequestedRole() != null) {
                throw new IllegalStateException("Role request already exists");
            }

            user.setRequestedRole(role);
            return userRepository.save(user);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + requestedRole);
        }
    }

    public UserDTO handleRoleRequest(Long userId, String action) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getRequestedRole() == null) {
            throw new IllegalStateException("No pending role request for this user");
        }

        if ("APPROVE".equalsIgnoreCase(action)) {
            user.setRole(user.getRequestedRole());
        }
        // For both approve and reject, clear the requested role
        user.setRequestedRole(null);
        return convertToDTO(userRepository.save(user));
    }


    public User completeUserProfile(Long userId, ProfileUpdateDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Update fields
        updateFieldsFromDTO(user, profileDTO);
        // Update only provided fields
        // Validate ONLY on initial profile completion
        if (!user.isProfileCompleted()) {
            validateRoleSpecificFields(user);
            user.setProfileCompleted(true);
        }
        return userRepository.save(user);
    }
    private void updateFieldsFromDTO(User user, ProfileUpdateDTO profileDTO) {
        if (profileDTO.getPhoneNumber() != null)
            user.setPhoneNumber(profileDTO.getPhoneNumber());

        // Update student-specific fields only if provided
        if (user.getRole() == Role.STUDENT) {
            if (profileDTO.getPaid() != null) user.setPaid(profileDTO.getPaid());
            if (profileDTO.getCv() != null) user.setCv(profileDTO.getCv());
            if (profileDTO.getCompetence() != null)
                user.setCompetence(profileDTO.getCompetence());
            if (profileDTO.getSpecialty() != null)
                user.setSpecialty(profileDTO.getSpecialty());
        }
        // ... other role-specific updates
    }
    public User updateUserRole(Long userId, RoleUpdateDTO roleUpdateDTO) {
        try {
            Role newRole = Role.valueOf(roleUpdateDTO.getNewRole().toUpperCase());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setRole(newRole);
            return userRepository.save(user);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role specified");
        }
    }


    private void validateRoleSpecificFields(User user) {
        final Role role = user.getRole();

        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }

        // Skip validation for PENDING users
        if (role == Role.PENDING) {
            return;
        }

        switch (role) {
            case STUDENT -> validateStudentFields(user);
            case TEACHER, FREELANCER -> validateEducatorFields(user);
            case ENTERPRISE -> validateEnterpriseFields(user);
            case ADMIN -> {} // No specific field requirements
        }
    }
    private void validateStudentFields(User user) {
        List<String> missingFields = new ArrayList<>();

        if (user.getPaid() == null) missingFields.add("paid");
        if (isNullOrEmpty(user.getCv())) missingFields.add("cv");
        // Only require competence/specialty on initial setup
        if (!user.isProfileCompleted()) {
            if (isNullOrEmpty(user.getCompetence()))
                missingFields.add("competence");
            if (isNullOrEmpty(user.getSpecialty()))
                missingFields.add("specialty");
        }

        if (!missingFields.isEmpty()) {
            throw new IllegalArgumentException("Student requires fields: " + missingFields);
        }
    }

    private void validateEducatorFields(User user) {
        if (isNullOrEmpty(user.getSpecialty())) {
            throw new IllegalArgumentException("Specialty is required for " + user.getRole());
        }
    }
    private void validateEnterpriseFields(User user) {
        List<String> missingFields = new ArrayList<>();

        if (isNullOrEmpty(user.getBusinessCode())) missingFields.add("businessCode");
        if (isNullOrEmpty(user.getTaxId())) missingFields.add("taxId");
        if (isNullOrEmpty(user.getAddress())) missingFields.add("address");

        if (!missingFields.isEmpty()) {
            throw new IllegalArgumentException("Enterprise requires fields: " + missingFields);
        }
    }


    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Other methods remain same as previous implementation
    @Override
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User updateProfile(Long userId, ProfileUpdateDTO profileDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(user.getRole() == Role.PENDING) {
            throw new IllegalStateException("Complete profile only after role approval");
        }

        // Update fields based on DTO
        if(profileDTO.getPhoneNumber() != null) user.setPhoneNumber(profileDTO.getPhoneNumber());

        // Role-specific updates
        switch(user.getRole()) {
            case STUDENT:
                if(profileDTO.getPaid() != null) user.setPaid(profileDTO.getPaid());
                if(profileDTO.getCv() != null) user.setCv(profileDTO.getCv());
                if(profileDTO.getCompetence() != null) user.setCompetence(profileDTO.getCompetence());
                if(profileDTO.getSpecialty() != null) user.setSpecialty(profileDTO.getSpecialty());
                break;

            case ENTERPRISE:
                if(profileDTO.getBusinessCode() != null) user.setBusinessCode(profileDTO.getBusinessCode());
                if(profileDTO.getTaxId() != null) user.setTaxId(profileDTO.getTaxId());
                if(profileDTO.getAddress() != null) user.setAddress(profileDTO.getAddress());
                if(profileDTO.getSector() != null) user.setSector(profileDTO.getSector());
                break;

            case TEACHER, FREELANCER:
                if(profileDTO.getSpecialty() != null) user.setSpecialty(profileDTO.getSpecialty());
                break;
        }

        validateProfileCompletion(user);
        return userRepository.save(user);
    }

    private void validateProfileCompletion(User user) {
        if(user.getRole() == Role.PENDING) return;

        switch(user.getRole()) {
            case STUDENT:
                if(user.getPaid() == null || user.getCv() == null ||
                        user.getCompetence() == null || user.getSpecialty() == null) {
                    throw new IllegalArgumentException("Missing required student fields");
                }
                break;

            case ENTERPRISE:
                if(user.getBusinessCode() == null || user.getTaxId() == null ||
                        user.getAddress() == null) {
                    throw new IllegalArgumentException("Missing required enterprise fields");
                }
                break;

            case TEACHER, FREELANCER:
                if(user.getSpecialty() == null) {
                    throw new IllegalArgumentException("Specialty required");
                }
                break;
        }
    }

    public List<RoleRequestDTO> getPendingRoleRequests() {
        return userRepository.findByRequestedRoleIsNotNull()
                .stream()
                .map(this::convertToRoleRequestDTO)
                .toList();
    }

    private RoleRequestDTO convertToRoleRequestDTO(User user) {
        RoleRequestDTO dto = new RoleRequestDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setCurrentRole(user.getRole().name());
        dto.setRequestedRole(user.getRequestedRole().name());
        return dto;
    }

    /**
     * Attribue un cluster à un utilisateur basé sur son profil
     * @param user L'utilisateur à classifier
     * @return L'utilisateur avec son cluster assigné
     */
    @Transactional
    public User assignCluster(User user) {
        int clusterId = clusteringService.predictCluster(user);
        user.setCluster(clusterId);
        return userRepository.save(user);
    }
    /**
     * Met à jour le cluster de tous les utilisateurs dans la base de données
     * @return Le nombre d'utilisateurs mis à jour
     */
    @Transactional
    public int updateAllUserClusters() {
        int count = 0;
        List<User> users = userRepository.findAll();

        for (User user : users) {
            try {
                int clusterId = clusteringService.predictCluster(user);
                user.setCluster(clusterId);
                userRepository.save(user);
                count++;
            } catch (Exception e) {
                logger.error("Erreur lors de la mise à jour du cluster pour l'utilisateur ID: " + user.getId(), e);
            }
        }

        return count;
    }

    /**
     * Récupère les informations sur un cluster spécifique
     * @param clusterId L'ID du cluster
     * @return Map contenant les informations du cluster
     */
    public Map<String, Object> getClusterInfo(int clusterId) {
        return clusteringService.getClusterInfo(clusterId);
    }

    /**
     * Récupère tous les utilisateurs appartenant à un cluster spécifique
     * @param clusterId L'ID du cluster
     * @return Liste des utilisateurs dans ce cluster
     */
    public List<User> getUsersByCluster(Integer clusterId) {
        return userRepository.findByCluster(clusterId);
    }

    /**
     * Récupère un utilisateur par son ID et ajoute des informations sur son cluster
     * @param userId L'ID de l'utilisateur
     * @return L'utilisateur avec des informations sur son cluster
     */
    public Optional<User> getUserWithClusterInfo(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Si l'utilisateur n'a pas encore de cluster, on lui en attribue un
            if (user.getCluster() == null) {
                user = assignCluster(user);
            }
        }

        return userOpt;
    }


}
