package com.CarStoreRestApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.repository.CarRepository;

@Service
public class CarHtmlService {

    @Autowired
    private CarRepository carRepository;

    // Get all cars
    public List<Car> getAllCars() {
        return (List<Car>) carRepository.findAll();
    }

    // Get car by ID
    public Car getCarById(Long id) {
        return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
    }
    
//    // Save a new car
//    public void saveCar(Car car) {
//        carRepository.save(car);
//    }

    // Get cars by type (SUV, Sedan, etc.)
    public List<Car> getCarsByType(String type) {
        return carRepository.findByType(type);
    }
}

