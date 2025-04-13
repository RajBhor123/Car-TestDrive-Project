package com.CarStoreRestApi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.Dealer;

public interface DealerRepository extends JpaRepository<Dealer, Long>{
	
	
	
	@Query("SELECT DISTINCT d.city FROM Dealer d WHERE d.city IS NOT NULL")
    List<String> findDistinctCities();
	
	// Custom query method to find dealers by city
    List<Dealer> findByCity(String city);

	Optional<Dealer> findByCarsContains(Car car);

	Optional<Dealer> getDealerById(Long id);
	
	Optional<Dealer> findByNameAndPassword(String name, String password);
	
	// Custom query to find a dealer by name
    Dealer findByName(String name);

}
