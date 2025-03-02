package com.example.TakerManger.Repository;

import com.example.TakerManger.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositiry extends JpaRepository<Users,Long> {
    Users findByEmail(String email);

}
