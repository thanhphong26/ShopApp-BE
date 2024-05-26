package com.pnt.shopapp.services;

import com.pnt.shopapp.dtos.UserDTO;
import com.pnt.shopapp.exceptions.DataNotFoundException;
import com.pnt.shopapp.models.User;

public interface IUserService{
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password);
}
