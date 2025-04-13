package com.CarStoreRestApi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.CarStoreRestApi.model.Car;


public interface CarRepository extends CrudRepository<Car,Integer> {

	List<Car> findByType(String type);

    Optional<Car> findById(Long id);
    
    List<Car> findByNameOrType(String name, String type);
    
    List<Car> findByDealerId(Long dealerId);
}
