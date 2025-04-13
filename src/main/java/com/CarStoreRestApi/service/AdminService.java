package com.CarStoreRestApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CarStoreRestApi.model.Admin;
import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.repository.AdminRepository;
import com.CarStoreRestApi.repository.CarRepository;

@Service
public class AdminService {

	@Autowired
    private AdminRepository adminRepository;

    public Admin registerAdmin(Admin admin) {
        // Save the admin directly without password encoding
        return adminRepository.save(admin);
    }

    public Admin authenticateAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }
 }

