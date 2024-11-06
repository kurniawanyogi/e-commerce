package com.eccomerce.auth_service.repository;

import com.eccomerce.auth_service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndStatusNot(String username, Character status);

    @Query(value = "SELECT * FROM `USER` US WHERE (:username IS NULL OR US.USERNAME LIKE :username%)" +
            " AND (:firstName IS NULL OR US.FIRST_NAME LIKE :firstName%)", nativeQuery = true)
    Page<User> getListUser(@Param(value = "username") String username, @Param(value = "firstName") String firstName, Pageable pageable);
}
