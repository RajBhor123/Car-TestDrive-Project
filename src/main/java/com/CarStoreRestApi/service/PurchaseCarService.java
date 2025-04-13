package com.CarStoreRestApi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CarStoreRestApi.model.PurchaseCar;
import com.CarStoreRestApi.repository.PurchaseCarRepository;

@Service
public class PurchaseCarService {
	
	 @Autowired
	    private PurchaseCarRepository purchaseCarRepository;

	    public void savePurchaseCar(PurchaseCar purchaseCar) {
	        purchaseCarRepository.save(purchaseCar);
	    }
	    

	    public List<PurchaseCar> getPurchaseByEmail(String email) {
	        return purchaseCarRepository.findByUserEmail(email);
	    }

}
