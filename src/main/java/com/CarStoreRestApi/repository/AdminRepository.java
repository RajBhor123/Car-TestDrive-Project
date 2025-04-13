package com.CarStoreRestApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CarStoreRestApi.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
	
	Admin findByEmail(String email);

}
