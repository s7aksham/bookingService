package com.rentals.car.booking.entity;

import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
@Entity
@Table(name = "Bookings")
public class Booking {
    public Booking(Long bookingId, Date startDate, Date endDate, Car car) {
        this.bookingId = bookingId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.car = car;
    }
    public Booking(){

    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookingId;
    @FutureOrPresent(message = "Booking should be for future dates!")
    private Date startDate;

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", startDate=" + startDate.toString() +
                ", endDate=" + endDate.toString() +
                ", car=" + car.getCarId() +
                ", car model=" + car.getModel() +
                '}';
    }

    @Future(message = "Booking end date cannot be of past or present!")
    private Date endDate;


@ManyToOne
@JoinColumn(name="carId",nullable = false)
    private Car car;
}
