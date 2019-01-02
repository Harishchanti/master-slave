package com.jpa.example.masterslave.repositories;

import com.jpa.example.masterslave.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("FROM UserEntity o WHERE o.id = ?1")
    UserEntity findByUserId(Long id);
}

