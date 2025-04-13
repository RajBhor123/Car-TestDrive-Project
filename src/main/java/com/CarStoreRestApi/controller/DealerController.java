package com.CarStoreRestApi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.service.DealerService;

@RestController
@RequestMapping("/api/dealers")
public class DealerController {

    @Autowired
    private DealerService dealerService;

    // Get all dealers
    @GetMapping
    public List<Dealer> getAllDealers() {
        return dealerService.getAllDealers();
    }

    // Get dealer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Dealer> getDealerById(@PathVariable Long id) {
        Optional<Dealer> dealer = dealerService.getDealerById(id);
        return dealer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new dealer
    @PostMapping("/add")
    public ResponseEntity<Dealer> createDealer(@RequestBody Dealer dealer) {
        Dealer savedDealer = dealerService.saveDealer(dealer);
        return ResponseEntity.ok(savedDealer);
    }

    // Update dealer by ID
    @PutMapping("/{id}")
    public ResponseEntity<Dealer> updateDealer(@PathVariable Long id, @RequestBody Dealer dealerDetails) {
        try {
            Dealer updatedDealer = dealerService.updateDealer(id, dealerDetails);
            return ResponseEntity.ok(updatedDealer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete dealer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDealer(@PathVariable Long id) {
        dealerService.deleteDealer(id);
        return ResponseEntity.noContent().build();
    }
}

