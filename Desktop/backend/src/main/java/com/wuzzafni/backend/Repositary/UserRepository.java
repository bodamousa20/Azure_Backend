package com.wuzzafni.backend.Repositary;

import com.wuzzafni.backend.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUserEmail(String userEmail);
}
