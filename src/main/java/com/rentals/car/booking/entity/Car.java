package com.rentals.car.booking.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {
    private String model;
    @OneToMany(mappedBy = "car")
    @JsonBackReference
    private List<Booking> bookingList;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carId;

    public Car(String model, Long carId) {
        this.model = model;
        this.carId = carId;
        this.bookingList = new ArrayList<>();
    }

    public Car(){
        this.bookingList = new ArrayList<>();
    }

    public String getModel() {
        return model;
    }
    public void setModel(String s) {this.model = s;}

    public List<Booking> getBookingList() {
        return new ArrayList<>(bookingList);
    }

    public Long getCarId() {
        return carId;
    }

    public void addBooking(Booking booking) {
        this.bookingList.add(booking);
    }

    public boolean isAvailable(Date startDate, Date endDate){
        boolean available = true;
        // check if any saved bookings overlaps with new requested booking
        for(Booking savedBooking : this.bookingList) {
            if (savedBooking.getEndDate().compareTo(startDate)==0||
                    savedBooking.getEndDate().compareTo(endDate)==0||
                    savedBooking.getStartDate().compareTo(startDate)==0||
                    savedBooking.getStartDate().compareTo(endDate)==0||
                    (savedBooking.getStartDate().before(endDate)&&savedBooking.getEndDate().after(startDate))){
                available = false;
                break;
            }
        }
        return available;
    }

    public Booking updateBooking(Date startDate, Date endDate, Date startDateNew, Date endDateNew) {
        for(Booking savedBooking : this.bookingList) {
            if(startDate.compareTo(savedBooking.getStartDate())==0 &&
            endDate.compareTo(savedBooking.getEndDate())==0){
                savedBooking.setStartDate(startDateNew);
                savedBooking.setEndDate(endDateNew);
                return savedBooking;
            }
        }
        throw new RuntimeException("Booking not found for start date:"+startDate.toString()+" end date:"+endDate.toString());
    }

}
