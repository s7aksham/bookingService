package com.rentals.car.booking.service;

import com.rentals.car.booking.entity.Booking;
import org.hibernate.ObjectNotFoundException;

import java.util.List;

public interface BookingService {
    public Booking saveBooking(Booking booking);

    List<Booking> getAllBookings();

    Booking getBooking(Long bookingId) throws ObjectNotFoundException;

    void deleteBooking(Long bookingId);

    Booking updateBooking(Long bookingId, Booking booking);
}
