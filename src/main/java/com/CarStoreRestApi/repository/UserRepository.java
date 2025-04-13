package com.CarStoreRestApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CarStoreRestApi.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	//User findByUsernameOrEmail(String usernameOrEmail, String email);
    User findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
	Optional<User> findById(Long id);

}
