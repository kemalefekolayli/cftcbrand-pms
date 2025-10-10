package com.example.cftcbrandtech.User.Controller;

import com.example.cftcbrandtech.Security.JwtHelper;
import com.example.cftcbrandtech.User.Dto.UpdateUserDto;
import com.example.cftcbrandtech.User.Service.UserModelService;
import com.example.cftcbrandtech.User.UserModel;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserModelService userModelService;

    private final JwtHelper jwtHelper; // we gonna eventually remove the token creation logic from this class else where

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        // Token'dan user'ı al ve yetki kontrolü yap todo: userlara permissions tarzında bir şey ekle ve bu endpointte permission check yap
        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7)); // "Bearer " kısmını çıkar
        UserModel user = userModelService.GetUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String token) { // todo: bu fonksiyona paging + permission check eklememiz gerek
        UserModel currentUser = jwtHelper.getUserFromToken(token.substring(7));
        List<UserModel> userList = userModelService.GetAllUsers();
        return ResponseEntity.ok(userList);
    }





}
