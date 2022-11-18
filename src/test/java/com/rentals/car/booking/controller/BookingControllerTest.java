package com.rentals.car.booking.controller;

import com.rentals.car.booking.entity.Booking;
import com.rentals.car.booking.entity.Car;
import com.rentals.car.booking.service.BookingService;
import com.rentals.car.booking.service.CarService;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private CarService carService;
    private Booking booking;
    private Car car;
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    @BeforeEach
    void setUp() throws ParseException {
        booking = new Booking();
        car = new Car("BMW 650",1L);

        booking = new Booking(1L,
                df.parse("2023-12-25T06:30:00"),
                df.parse("2023-12-26T20:30:00"),car);
        car.addBooking(booking);
    }

    @Test
    public void saveValidBooking() throws Exception {
        Booking newBooking = new Booking();
        newBooking.setStartDate(df.parse("2023-12-25T06:30:00"));
        newBooking.setEndDate(df.parse("2023-12-26T20:30:00"));
        newBooking.setCar(car);

        Mockito.when(bookingService.saveBooking(newBooking)).thenReturn(booking);

        mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"startDate\":\"2023-12-25T06:30:00\",\n" +
                        "    \"endDate\":\"2023-12-26T20:30:00\",\n" +
                        "    \"car\" :{\n" +
                        "        \"carId\":\"1\"\n" +
                        "    }\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void saveInvalidBooking() throws Exception {
        assertThrows(NestedServletException.class,()->mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"startDate\":\"2023-12-27T06:30:00\",\n" +
                                "    \"endDate\":\"2023-12-26T20:30:00\",\n" +
                                "    \"car\" :{\n" +
                                "        \"carId\":\"1\"\n" +
                                "    }\n" +
                                "}")));
    }

    @Test
    public void getBooking_validId() throws Exception {
        Mockito.when(bookingService.getBooking(1L)).thenReturn(booking);
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookingId").value(1L));
    }

    @Test
    public void getBooking_invalidId() throws Exception {
        Mockito.when(bookingService.getBooking(99L)).thenThrow(new ObjectNotFoundException(1L,"Booking"));
        assertThrows(NestedServletException.class,()->mockMvc.perform(MockMvcRequestBuilders.get("/bookings/99")
                .contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    public void getAvailableBookings_validPeriod() throws Exception {
        ArrayList<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking);
        Mockito.when(carService.getAvailableBookings(booking)).thenReturn(bookingList);
        mockMvc.perform(MockMvcRequestBuilders.get("/available-bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"startDate\":\"2023-12-25T06:30:00\",\n" +
                                "    \"endDate\":\"2023-12-26T20:30:00\",\n" +
                                "    \"car\" :{\n" +
                                "    }\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void getAvailableBookings_invalidPeriod() throws Exception {
        assertThrows(NestedServletException.class,()->mockMvc.perform(MockMvcRequestBuilders.get("/available-bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"startDate\":\"2023-12-27T06:30:00\",\n" +
                                "    \"endDate\":\"2023-12-26T20:30:00\",\n" +
                                "    \"car\" :{\n" +
                                "    }\n" +
                                "}")));
    }

    @Test
    public void updateBooking_validPeriod() throws Exception {
        Mockito.when(bookingService.updateBooking(1L,booking)).thenReturn(booking);
        mockMvc.perform(MockMvcRequestBuilders.put("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"bookingId\":\"1\",\n"+
                                "    \"startDate\":\"2023-12-25T06:30:00\",\n" +
                                "    \"endDate\":\"2023-12-27T20:30:00\",\n" +
                                "    \"car\" :{\n" +
                                "        \"carId\":\"1\"\n" +
                                "    }\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    public void updateBooking_invalidPeriod() throws Exception {
        assertThrows(NestedServletException.class,()->mockMvc.perform(MockMvcRequestBuilders.put("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"bookingId\":\"1\",\n"+
                                "    \"startDate\":\"2023-12-28T06:30:00\",\n" +
                                "    \"endDate\":\"2023-12-27T20:30:00\",\n" +
                                "    \"car\" :{\n" +
                                "        \"carId\":\"1\"\n" +
                                "    }\n" +
                                "}")));

    }

    @Test
    public void getAllBookings_validCar() throws Exception {
        Mockito.when(carService.getCarBookings(car.getCarId())).thenReturn(car.getBookingList());
        mockMvc.perform(MockMvcRequestBuilders.get("/bookings/car/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].bookingId").value(1L));
    }
    @Test
    public void getAllBookings_invalidCar() throws Exception {
        Mockito.when(carService.getCarBookings(99L)).thenThrow(new ObjectNotFoundException(99L,"not found"));
        assertThrows(NestedServletException.class,()->mockMvc.perform(MockMvcRequestBuilders.get("/bookings/car/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()));
    }


    @Test
    public void deleteBooking_validId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Delete successful:1"));
    }

}