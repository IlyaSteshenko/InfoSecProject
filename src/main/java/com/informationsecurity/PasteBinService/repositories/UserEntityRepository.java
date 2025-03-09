package com.informationsecurity.PasteBinService.repositories;

import com.informationsecurity.PasteBinService.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserById(Long id);
    UserEntity findUserByUsername(String username);
}
