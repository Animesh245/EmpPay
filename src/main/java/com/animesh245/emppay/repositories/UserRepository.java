package com.animesh245.emppay.repositories;

import com.animesh245.emppay.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByUserId(String userId);
    User getUserByName(String name);
    User save(User user);
    void deleteUserByUserId(String  userId);
    @Modifying
    @Query(value = "UPDATE user u SET e.isActive = :isActive WHERE e.id = :userId",nativeQuery = true)
    void customUpdateEmployeeStatus(int isActive, String  userId);
}
