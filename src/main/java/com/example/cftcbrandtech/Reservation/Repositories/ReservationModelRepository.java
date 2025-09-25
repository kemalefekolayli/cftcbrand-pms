package com.example.cftcbrandtech.User.Repository;

import com.example.cftcbrandtech.Reservation.ReservationModel;
import com.example.cftcbrandtech.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationModelRepository extends JpaRepository<ReservationModel, Long> {

}
