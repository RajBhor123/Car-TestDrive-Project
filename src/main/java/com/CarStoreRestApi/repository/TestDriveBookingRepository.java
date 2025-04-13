package com.CarStoreRestApi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CarStoreRestApi.model.TestDriveBooking;

public interface TestDriveBookingRepository extends JpaRepository<TestDriveBooking, Long>{
	
	@EntityGraph(attributePaths = {"car", "dealer", "user"})
	Optional<TestDriveBooking> findById(Long id);
	
	List<TestDriveBooking> findByDealerId(Long dealerId);

}
