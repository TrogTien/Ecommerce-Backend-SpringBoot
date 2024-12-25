package com.example.shopapp.services.interfaces;

import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserUpdateDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password) throws Exception;

    User findUserByPhoneNumber(String phoneNumber) throws DataNotFoundException;

    User updateUser(UserUpdateDTO userUpdateDTO, Long userId) throws Exception;
}
