package com.saifeddine.user.service;

import com.saifeddine.user.dto.UserDTO;
import com.saifeddine.user.model.Role;
import com.saifeddine.user.model.User;

import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO);
    List<User> getAllUsers();
    User getUserById(Long id);
    List<User> getUsersByRole(Role role);
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}

