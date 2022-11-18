package com.rentals.car.booking.service;

import com.rentals.car.booking.entity.Booking;
import com.rentals.car.booking.entity.Car;
import com.rentals.car.booking.repository.BookingRepository;
import com.rentals.car.booking.repository.CarRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService{
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CarRepository carRepository;

    @Override
    public Booking saveBooking(Booking booking) {
        Car car = carRepository.getById(booking.getCar().getCarId());
        booking.setCar(car);
        synchronized (car) {
            if (car.isAvailable(booking.getStartDate(),booking.getEndDate())) {
                Booking savedBooking = bookingRepository.save(booking);
                car.addBooking(savedBooking);
                return savedBooking;
            }
            throw new RuntimeException("Invalid Booking! Car not available.");
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBooking(Long bookingId) throws ObjectNotFoundException {
        Optional booking = bookingRepository.findById(bookingId);
        if(booking.isPresent())
            return (Booking) booking.get();
        throw new ObjectNotFoundException(bookingId,"Booking");
    }

    @Override
    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public Booking updateBooking(Long bookingId, Booking newBooking) {
        Booking oldBooking = getBooking(bookingId);
        Car car = oldBooking.getCar();
        synchronized (car) {
            if (car.isAvailable(newBooking.getStartDate(), newBooking.getEndDate())) {
                Booking updatedBooking = car.updateBooking(oldBooking.getStartDate(),
                        oldBooking.getEndDate(),
                        newBooking.getStartDate(),
                        newBooking.getEndDate());
                bookingRepository.save(updatedBooking);
                return updatedBooking;
            } else {
                throw new UnsupportedOperationException("Car not available for new dates");
            }
        }
    }
}
