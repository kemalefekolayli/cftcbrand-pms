package com.example.cftcbrandtech.User.Repository;

import com.example.cftcbrandtech.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findById(Long id);

    Optional<UserModel> findByEmail(String email);

    boolean existsByEmail(String email);
}
