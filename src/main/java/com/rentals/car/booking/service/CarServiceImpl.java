package com.rentals.car.booking.service;

import com.rentals.car.booking.entity.Booking;
import com.rentals.car.booking.entity.Car;
import com.rentals.car.booking.repository.CarRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarServiceImpl implements CarService{
    @Autowired
    CarRepository carRepository;

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Booking> getAvailableBookings(Booking booking) {
        List<Booking> availableBookings = new ArrayList<>();
        for(Car car : carRepository.findAll()){
            if(car.isAvailable(booking.getStartDate(),booking.getEndDate(),null))
                availableBookings.add(new Booking(
                        null,
                        booking.getStartDate(),
                        booking.getEndDate(),
                        car));
        }
        return availableBookings;
    }

    @Override
    public List<Booking> getCarBookings(Long carId) throws ObjectNotFoundException {
        Car car = carRepository.getById(carId);
        if (car == null)
            throw new ObjectNotFoundException(carId,"No car found with id:"+carId);
        return car.getBookingList();
    }
}
