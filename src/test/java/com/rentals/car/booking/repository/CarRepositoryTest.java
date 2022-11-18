package com.rentals.car.booking.repository;

import com.rentals.car.booking.entity.Booking;
import com.rentals.car.booking.entity.Car;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private CarRepository carRepository;
    @BeforeEach
    void setUp() throws ParseException {
        Car car = new Car();
        car.setModel("BMW 650");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Booking booking = new Booking();
        booking.setStartDate(df.parse("2023-12-25T06:30:00"));
        booking.setEndDate(df.parse("2023-12-26T20:30:00"));
        booking.setCar(car);
        car.addBooking(booking);
        testEntityManager.persist(car);
        testEntityManager.persist(booking);
    }
}