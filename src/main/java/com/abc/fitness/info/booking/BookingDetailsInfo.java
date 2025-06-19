package com.abc.fitness.info.booking;

import java.time.LocalDate;
import java.time.LocalTime;

/*
 * BookingDetailsInfo
 * Class to show details when searching for booking
 */
public class BookingDetailsInfo {
    private String className;
    private LocalTime classStartTime;
    private LocalDate bookingDate;
    private String memberName;

    public BookingDetailsInfo(String className, LocalTime classStartTime, LocalDate bookingDate, String memberName) {
        this.className = className;
        this.classStartTime = classStartTime;
        this.bookingDate = bookingDate;
        this.memberName = memberName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalTime getClassStartTime() {
        return classStartTime;
    }

    public void setClassStartTime(LocalTime classStartTime) {
        this.classStartTime = classStartTime;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
} 