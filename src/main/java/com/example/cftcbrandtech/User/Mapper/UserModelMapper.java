package com.example.cftcbrandtech.User.Mapper;

import com.example.cftcbrandtech.User.Dto.UserRegisDto;
import com.example.cftcbrandtech.User.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserModelMapper {

    public UserModel RegisterUserFromDto(UserRegisDto dto){
        UserModel userModel = new UserModel();
        userModel.setFirstName(dto.getFirstName());
        userModel.setEmail(dto.getEmail());
        userModel.setPassword(dto.getPassword());
        userModel.setLastName(dto.getLastName());
        userModel.setPhoneNumber(dto.getPhoneNumber());
        return userModel;
    }


}
