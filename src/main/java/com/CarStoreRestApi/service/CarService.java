package com.CarStoreRestApi.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.repository.CarRepository;

@Service
public class CarService {

	@Autowired
	private CarRepository carrepoobj;
    private final String uploadDir = "C:/Users/bhorr/Downloads/CarStoreRestApi/CarStoreRestApi/src/main/resources/static/images/"; // Define your directory path

	List<Car> carlist = new ArrayList<Car>();
	
	public  String savecar(Car carobj, MultipartFile file) {
        // Ensure the directory exists
        File directory = new File(uploadDir);
        System.out.println(directory.getPath());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file to the directory
        String filePath = uploadDir + file.getOriginalFilename();
        System.out.println(filePath);
        try {
			file.transferTo(new File(filePath));
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        carobj.setImagePath(file.getOriginalFilename());
        carrepoobj.save(carobj);
        return "Car object saved";
    }
	
	
	
	public List<Car> fetchdata()
	{
		 
		 return (List<Car>) carrepoobj.findAll();
		
	}
	
	
	public String updatedata(int id, Car carobj)
	{
		carrepoobj.save(carobj);
		return "Data Updated Successfully";
		
	}
	
	public String deletedata(int id)
	{
		System.out.println(id);
		carrepoobj.deleteById((int) id);
		return "data deleted";
	}



//	public Optional<Car> getCarById(Long id) {
//        return carrepoobj.findById(id);
//    }

	public Car findById(Long carId) {
	    return carrepoobj.findById(carId).orElse(null); // Return null if not found
	}
	
	public Car getCarById(Long carId) {
        return carrepoobj.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found!"));
    }
	
	public List<Car> getCarsByDealer(Long dealerId) {
	    return carrepoobj.findByDealerId(dealerId);
	}
	
	public void saveCar(Car car) {
		carrepoobj.save(car);
    }
	
	public Iterable<Car> getAllCars() {
	    return carrepoobj.findAll();
	}

}
