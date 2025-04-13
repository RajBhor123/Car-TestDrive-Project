package com.CarStoreRestApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.repository.CarRepository;
import com.CarStoreRestApi.service.CarService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/car")
public class CarController {
	
	@Autowired
	private CarService carserviceobj;
	
	@Autowired
	private CarRepository carrepoobj;
	
//	@PostMapping("/search")
//	public String petsearch(@RequestParam String searchdata,Model model,HttpSession session)
//	{
//		List<Car> petobj =carrepoobj.findByTypeOrName(searchdata,searchdata);
//		model.addAttribute("pets",petobj);
//		model.addAttribute("username",session.getAttribute("loggedInUser"));
//		return "Dashboard";
//	}
	
    @PostMapping("/add")
    public ResponseEntity<String> addcar(@RequestPart("car") Car carobj, 
                                         @RequestPart("file") MultipartFile file) {
        String message = carserviceobj.savecar(carobj, file);
        return ResponseEntity.ok(message);
    }
	@GetMapping("/fetchdata")
	public List<Car> fetchdata()
	{
		return carserviceobj.fetchdata();
	}
	
	@PutMapping("updatedata/{id}")
	public String updatedata(@PathVariable int id,@RequestBody Car carobj)
	{
		carserviceobj.updatedata(id, carobj);
		return "Data updated";
	}
	
	
	@DeleteMapping("/deletedata/{id}")
	public String deletedata(@PathVariable int id)
	{
		
		carserviceobj.deletedata(id);
		return "Data Deleted";
	}

}
