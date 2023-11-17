package com.animesh245.emppay.repositories;

import com.animesh245.emppay.entities.User;
import com.animesh245.emppay.utils.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserByUserId(String userId);
    User getUserByUserTypeRole(UserType userType);
    List<User> getAllByIsActive(int isActive);
//    @Modifying
//    @Query(value = "select * from users u where u.name = :name", nativeQuery = true)
    User getUserByName(String name);
    User getUserByEmail(String email);
    User save(User user);
    void deleteUserByUserId(String  userId);
    @Modifying
    @Query(value = "UPDATE user u SET e.isActive = :isActive WHERE e.id = :userId",nativeQuery = true)
    void customUpdateEmployeeStatus(int isActive, String  userId);
}

