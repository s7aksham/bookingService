package com.rentals.car.booking.controller;

import com.rentals.car.booking.entity.Booking;
import com.rentals.car.booking.service.BookingService;
import com.rentals.car.booking.service.CarService;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.InvalidAlgorithmParameterException;
import java.text.ParseException;
import java.util.List;

@RestController
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CarService carService;
    private final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    @GetMapping("/")
    public String rootEndpointContent(){
        return "Booking API for car rental";
    }

    @PostMapping("/bookings")
    public Booking saveBooking(@Valid @RequestBody Booking booking) throws InvalidAlgorithmParameterException {
        LOGGER.info("Request to save new booking:"+booking.toString());
        if(booking.getEndDate().compareTo(booking.getStartDate())<=0)
            throw new InvalidAlgorithmParameterException("End date should be after Start date");
        return bookingService.saveBooking(booking);
    }

    @GetMapping("/available-bookings")
    public List<Booking> getAvailableBookings(@Valid @RequestBody Booking booking) throws ParseException, InvalidAlgorithmParameterException {
        LOGGER.info("Request to get available bookings for period:"+booking.toString());
        if(booking.getEndDate().compareTo(booking.getStartDate())<=0)
            throw new InvalidAlgorithmParameterException("End date should be after Start date");
        return carService.getAvailableBookings(booking);
    }

    @GetMapping("/bookings")
    public List<Booking> getBooking(){
        LOGGER.info("Request to get all bookings");
        return bookingService.getAllBookings();
    }

    @GetMapping("/bookings/{id}")
    public Booking getBookingById(@PathVariable("id") Long bookingId) throws ObjectNotFoundException {
        LOGGER.info("Request to get booking by ID:"+bookingId);
        return bookingService.getBooking(bookingId);
    }

    @PutMapping("/bookings/{id}")
    public Booking updateBooking(@PathVariable("id") Long bookingId,@Valid @RequestBody Booking booking) throws InvalidAlgorithmParameterException {
        LOGGER.info("Request to Update booking ID:"+bookingId+" booking:"+booking.toString());
        if(booking.getEndDate().compareTo(booking.getStartDate())<=0)
            throw new InvalidAlgorithmParameterException("End date should be after Start date");
        return bookingService.updateBooking(bookingId,booking);
    }

    @GetMapping("/bookings/car/{id}")
    public List<Booking> getBookingByCarId(@PathVariable("id") Long carId){
        LOGGER.info("Request to get bookings for particular carId:"+carId);
        return carService.getCarBookings(carId);
    }

    @DeleteMapping("/bookings/{id}")
    public String deleteBookingById(@PathVariable("id") Long bookingId){
        LOGGER.info("Request to delete bookingId:"+bookingId);
        bookingService.deleteBooking(bookingId);
        return "Delete successful:"+bookingId;
    }

}
