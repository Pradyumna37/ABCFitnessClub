package com.abc.fitness.repositories;

import com.abc.fitness.model.Booking;
import com.abc.fitness.model.FitnessClass;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    public synchronized Booking save(Booking booking) {
        bookings.add(booking);
        return booking;
    }

	/*
	 * public List<Booking> findAll() { return new ArrayList<>(bookings); }
	 */

    public List<Booking> findByMemberName(String memberName) {
        return bookings.stream()
                .filter(b -> b.getMemberName().equals(memberName))
                .collect(Collectors.toList());
    }

    public List<Booking> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookings.stream()
                .filter(b -> !b.getParticipationDate().isBefore(startDate) && 
                           !b.getParticipationDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Booking> findByMemberAndDateRange(String memberName, LocalDate startDate, LocalDate endDate) {
        return bookings.stream()
                .filter(b -> b.getMemberName().equals(memberName) &&
                           !b.getParticipationDate().isBefore(startDate) && 
                           !b.getParticipationDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
    public int countBookingsForClassAndDate(FitnessClass fitnessClass, LocalDate date) {
        return (int) bookings.stream()
                .filter(b -> b.getFitnessClass().getName().equals(fitnessClass.getName()) && 
                           b.getParticipationDate().equals(date))
                .count();
    }
}
