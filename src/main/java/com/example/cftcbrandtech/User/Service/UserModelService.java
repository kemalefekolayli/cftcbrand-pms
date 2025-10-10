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

    private final UserModelRepository userModelRepository;;

    public UserModel GetUserById(Long id){
        return userModelRepository.findById(id).orElseThrow(()->new GlobalException(ErrorCodes.AUTH_USER_NOT_FOUND));
    }

    public List<UserModel> GetAllUsers(){
        return userModelRepository.findAll();
    }

}
