package com.rentals.car.booking.service;

import com.rentals.car.booking.entity.Booking;
import com.rentals.car.booking.entity.Car;
import com.rentals.car.booking.repository.BookingRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.datetime.DateFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookingServiceTest {
    @Autowired
    private BookingService bookingService;
    @MockBean
    private BookingRepository bookingRepository;
    @BeforeEach
    void setUp() throws ParseException {
        Car car = new Car("BMW 650",1L);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        Booking booking = new Booking(1L,
                df.parse("2023-12-25T06:30:00"),
                df.parse("2023-12-26T20:30:00"),car);
        car.addBooking(booking);
        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(booking));
    }
    @Test
    public void getBooking_validId(){
        Long bookingId = 1L;
        Booking booking = bookingService.getBooking(bookingId);
        assertEquals(bookingId,booking.getBookingId());
    }
    @Test
    public void getBooking_invalidId(){
        assertThrows(ObjectNotFoundException.class,()->bookingService.getBooking(99L));
    }
}