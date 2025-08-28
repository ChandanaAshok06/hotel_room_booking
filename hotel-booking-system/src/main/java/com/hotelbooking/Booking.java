package com.hotelbooking;

import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private int customerId;
    private String roomNumber;   // Changed from roomId
    private int days;
    private double totalAmount;
    private Timestamp bookedAt;

    public Booking() {}

    public Booking(int customerId, String roomNumber, int days, double totalAmount) {
        this.customerId = customerId;
        this.roomNumber = roomNumber;
        this.days = days;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public Timestamp getBookedAt() { return bookedAt; }
    public void setBookedAt(Timestamp bookedAt) { this.bookedAt = bookedAt; }
}
