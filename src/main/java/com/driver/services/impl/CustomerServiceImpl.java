package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.model.TripStatus;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Optional<Customer> customerOptional = customerRepository2.findById(customerId);
		if(customerOptional.isPresent()){
			Customer customer = customerOptional.get();
			customerRepository2.delete(customer);
		}

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		TripBooking tripBooking = new TripBooking(fromLocation, toLocation, distanceInKm);
		Optional<Customer> customerOptional= customerRepository2.findById(customerId);

		if(customerOptional.isPresent()){
			Customer customer = customerOptional.get();
			ArrayList<TripBooking>bookingArrayList = (ArrayList<TripBooking>) customer.getTripBookingList();
			bookingArrayList.add(tripBooking);
			customer.setTripBookingList(bookingArrayList);
			customerRepository2.save(customer);
		}

		Driver driver = driverRepository2.getlowestIdDriver();
		if(driver==null){
			throw new Exception("No cab available!");
		}
		ArrayList<TripBooking>bookingArrayList = (ArrayList<TripBooking>) driver.getTripBookingList();
		bookingArrayList.add(tripBooking);
		driver.setTripBookingList(bookingArrayList);
		driverRepository2.save(driver);

		return  tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		tripBookingRepository2.deleteById(tripId);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> tripBooking = tripBookingRepository2.findById(tripId);
		if(tripBooking.isPresent()){
			TripBooking tripBooking1 = tripBooking.get();
			tripBooking1.setStatus(TripStatus.COMPLETED);
			tripBookingRepository2.save(tripBooking1);
		}
	}
}
