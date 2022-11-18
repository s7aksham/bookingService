package com.rentals.car.booking.repository;

import com.rentals.car.booking.entity.Booking;
import com.rentals.car.booking.entity.Car;
import org.hibernate.TransientPropertyValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private Long carId;
    private Long bookingId;

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
        carId = (Long) testEntityManager.persistAndGetId(car);
        bookingId = (Long) testEntityManager.persistAndGetId(booking);
    }

    @Test
    public void getBooking_validId(){
        Booking booking = bookingRepository.findById(bookingId).get();
        assertEquals(bookingId,booking.getBookingId());
    }

    @Test
    public void getBooking_invalidId(){
        assertThrows(Exception.class,()-> bookingRepository.findById(-1L).get());
    }

    @Test
    public void saveBooking_invalidCar() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Booking booking = new Booking();
        booking.setStartDate(df.parse("2023-12-25T06:30:00"));
        booking.setEndDate(df.parse("2023-12-26T20:30:00"));
        booking.setCar(new Car());
        assertThrows(IllegalStateException.class,()->testEntityManager.persist(booking));
    }
    @Test
    public void saveBooking_validCar() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Booking booking = new Booking();
        booking.setStartDate(df.parse("2023-12-25T06:30:00"));
        booking.setEndDate(df.parse("2023-12-26T20:30:00"));
        booking.setCar(testEntityManager.find(Car.class,carId));
        Long newBookingId = (Long) testEntityManager.persistAndGetId(booking);
        assertEquals(testEntityManager.find(Booking.class,newBookingId).getCar().getCarId(),carId);
    }
}