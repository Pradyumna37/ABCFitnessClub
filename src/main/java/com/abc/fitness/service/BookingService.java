package com.abc.fitness.service;

import com.abc.fitness.model.Booking;
import com.abc.fitness.model.FitnessClass;
import com.abc.fitness.repositories.BookingRepository;
import com.abc.fitness.repositories.FitnessClassRepository;
import com.abc.fitness.info.booking.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FitnessClassRepository classRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, FitnessClassRepository classRepository) {
        this.bookingRepository = bookingRepository;
        this.classRepository = classRepository;
    }

    public synchronized Booking createBooking(Booking booking) {
        validateBooking(booking);
        return bookingRepository.save(booking);
    }

    public List<BookingDetailsInfo> getBookingsByMember(String memberName) {
        return bookingRepository.findByMemberName(memberName).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<BookingDetailsInfo> getBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByDateRange(startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<BookingDetailsInfo> getBookingsByMemberAndDateRange(String memberName, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByMemberAndDateRange(memberName, startDate, endDate).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private BookingDetailsInfo convertToDTO(Booking booking) {
        return new BookingDetailsInfo(
            booking.getFitnessClass().getName(),
            booking.getFitnessClass().getStartTime(),
            booking.getParticipationDate(),
            booking.getMemberName()
        );
    }

    private void validateBooking(Booking booking) {
        if (booking.getMemberName() == null || booking.getMemberName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name is required");
        }
        if (booking.getFitnessClass() == null) {
            throw new IllegalArgumentException("Fitness class is required");
        }
        if (booking.getParticipationDate() == null) {
            throw new IllegalArgumentException("Participation date is required");
        }
        if (booking.getParticipationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Participation date must be in the future");
        }
        
        //Booking Validations
        // Check if the class exists
        List<FitnessClass> classes = classRepository.findByName(booking.getFitnessClass().getName());
        if (classes.isEmpty()) {
            throw new IllegalArgumentException("Fitness class does not exist");
        }

        // Check participation date is within the class schedule
        FitnessClass fitnessClass = classes.get(0);
        if (booking.getParticipationDate().isBefore(fitnessClass.getStartDate()) ||
            booking.getParticipationDate().isAfter(fitnessClass.getEndDate())) {
            throw new IllegalArgumentException("Participation date is outside of class schedule");
        }

        // Checking capacity 
        int currentBookings = bookingRepository.countBookingsForClassAndDate(fitnessClass, booking.getParticipationDate());
        if (currentBookings >= fitnessClass.getCapacity()) {
            throw new IllegalArgumentException("Class is at full capacity for this date");
        }
    }
}
