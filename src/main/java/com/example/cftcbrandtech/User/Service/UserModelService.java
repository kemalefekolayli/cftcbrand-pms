package com.example.cftcbrandtech.User.Service;


import com.example.cftcbrandtech.Exceptions.ErrorCodes;
import com.example.cftcbrandtech.Exceptions.GlobalException;
import com.example.cftcbrandtech.User.Dto.UpdateUserDto;
import com.example.cftcbrandtech.User.Dto.UserLoginDto;
import com.example.cftcbrandtech.User.Dto.UserRegisDto;
import com.example.cftcbrandtech.User.Mapper.UserModelMapper;
import com.example.cftcbrandtech.User.UserModel;
import com.example.cftcbrandtech.User.Repository.UserModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserModelService {

    private final UserModelRepository userModelRepository;
    private final UserModelMapper userModelMapper;

    public boolean RegisterUser(UserRegisDto dto){ // we need to add validation checks here
        if (userModelRepository.existsByEmail(dto.getEmail())) { // is this redundant ?? we might need some testing here
            throw new GlobalException(ErrorCodes.AUTH_EMAIL_TAKEN);
        }
        UserModel userModel = userModelMapper.RegisterUserFromDto(dto);
        userModelRepository.save(userModel);
        return true;
    }

    public UserModel LoginUser(UserLoginDto dto){
        UserModel userToLogin = userModelRepository.findByEmail(dto.getEmail()).orElseThrow(()->new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));
        if(userToLogin.getPassword().equals(dto.getPassword())){
            return userToLogin;
        }
        else {
            throw new GlobalException(ErrorCodes.AUTH_INVALID_CREDENTIALS); // different errors for mail and password might cause security issues
        }
    }

    public UserModel GetUserById(Long id){
        return userModelRepository.findById(id).orElseThrow(()->new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));
    }

    public List<UserModel> GetAllUsers(){
        return userModelRepository.findAll();
    }

    public UserModel UpdateUser(UpdateUserDto dto, Long id){
        UserModel userModel = userModelRepository.findById(id).orElseThrow(()->new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));
        if(!dto.getPassword().isEmpty()){
            userModel.setPassword(dto.getPassword());
        }
        if(!dto.getFirstName().isEmpty()){
            userModel.setFirstName(dto.getFirstName());
        }
        if(!dto.getLastName().isEmpty()){
            userModel.setLastName(dto.getLastName());
        }
        userModelRepository.save(userModel);
        return userModel;
    }

    public  void DeleteUser(Long id){
        UserModel userToBeDeleted = userModelRepository.findById(id).orElseThrow(()->new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));
        userToBeDeleted.setActive(false);
        userModelRepository.save(userToBeDeleted);
    }
}
