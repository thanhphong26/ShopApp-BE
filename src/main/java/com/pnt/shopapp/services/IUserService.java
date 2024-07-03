package com.pnt.shopapp.services;

import com.pnt.shopapp.dtos.UpdateUserDTO;
import com.pnt.shopapp.dtos.UserDTO;
import com.pnt.shopapp.exceptions.DataNotFoundException;
import com.pnt.shopapp.models.User;

import java.util.List;

public interface IUserService{
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password, Long roleId) throws Exception;
    User getUserDetailsFromToken(String token) throws Exception;
    List<User> getAllUsers();
    User updateUser(Long id, UpdateUserDTO updateUserDTO) throws DataNotFoundException;
}
