package com.rentals.car.booking.service;

import com.rentals.car.booking.entity.Booking;
import com.rentals.car.booking.entity.Car;

import java.util.List;

public interface CarService {
    Car saveCar(Car car);

    List<Booking> getAvailableBookings(Booking booking);

    List<Booking> getCarBookings(Long carId);
}
