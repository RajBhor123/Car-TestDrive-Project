package com.CarStoreRestApi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.repository.DealerRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class DealerService {

    @Autowired
    private DealerRepository dealerRepository;

    public List<Dealer> getAllDealers() {
        return dealerRepository.findAll();
    }

    public Optional<Dealer> getDealerById(Long id) {
        return dealerRepository.findById(id);
    }

    public Dealer saveDealer(Dealer dealer) {
        return dealerRepository.save(dealer);
    }

    public Dealer updateDealer(Long id, Dealer dealerDetails) {
        return dealerRepository.findById(id).map(dealer -> {
            dealer.setName(dealerDetails.getName());
            dealer.setAddress(dealerDetails.getAddress());
            dealer.setPhoneNumber(dealerDetails.getPhoneNumber());
            dealer.setcity(dealerDetails.getcity());
            dealer.setState(dealerDetails.getState());
            dealer.setCountry(dealerDetails.getCountry());
            dealer.setPincode(dealerDetails.getPincode());
            return dealerRepository.save(dealer);
        }).orElseThrow(() -> new RuntimeException("Dealer not found with id: " + id));
    }

    public void deleteDealer(Long id) {
        dealerRepository.deleteById(id);
    }
    
    public Optional<Dealer> findByNameAndPassword(String name, String password) {
        return dealerRepository.findByNameAndPassword(name, password);
    }

// // Fetch distinct cities from the Dealer table via the Car table
//    public List<String> getDealerCities() {
//        return dealerRepository.findAll()
//                .stream()
//                .map(dealer -> dealer.getCity())
//                .distinct()
//                .collect(Collectors.toList());
//    }

 // Fetch distinct cities from the Dealer table
    public List<String> getDealerCities() {
        return dealerRepository.findDistinctCities();
    }
    
 // Method to fetch dealers by city
    public List<Dealer> getDealersByCity(String city) {
        return dealerRepository.findByCity(city);  // This will query the database for dealers in the specified city
    }

    public Dealer findDealerForCar(Car car) {
        return dealerRepository.findByCarsContains(car)
                .orElseThrow();
    }
 // Method to find a dealer by ID
    public Dealer findById(Long dealerId) {
        // Assuming you are using Spring Data JPA or similar
        return dealerRepository.findById(dealerId).orElse(null); // Returns null if dealer is not found
    }
    
    public Dealer findByName(String name) {
	    return dealerRepository.findByName(name);  // Ensure this matches your repository method
 }

}