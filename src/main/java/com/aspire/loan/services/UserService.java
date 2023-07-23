package com.aspire.loan.services;

import com.aspire.loan.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers(long userId, String password);

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(long userId, long id, String password);

    UserDTO updateUser(UserDTO userDTO, long userId, long id, String password);

    void deleteUser(long userId, long id, String password);
}
