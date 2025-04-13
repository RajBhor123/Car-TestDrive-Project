package com.CarStoreRestApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CarStoreRestApi.model.PurchaseCar;

public interface PurchaseCarRepository extends JpaRepository<PurchaseCar, Long>{

	 List<PurchaseCar> findByUserEmail(String email);
}
