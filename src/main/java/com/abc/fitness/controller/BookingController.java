package com.abc.fitness.controller;

import com.abc.fitness.model.Booking;
import com.abc.fitness.service.BookingService;
import com.abc.fitness.info.booking.BookingDetailsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return ResponseEntity.ok(createdBooking);
        } catch (IllegalArgumentException e) {
			String body = e.getMessage();
			if (body == null || (body.isEmpty()))
				return ResponseEntity.badRequest().build();
			else
				return ResponseEntity.badRequest().body(body);
		}
    }

    @GetMapping("/search")
    //Search with only member, date range or both
    public ResponseEntity<List<BookingDetailsInfo>> searchBookings(
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        if (memberName != null && startDate == null && endDate == null) {
            return ResponseEntity.ok(bookingService.getBookingsByMember(memberName));
        }
        if (memberName == null && startDate != null && endDate != null) {
            return ResponseEntity.ok(bookingService.getBookingsByDateRange(startDate, endDate));
        }
        if (memberName != null && startDate != null && endDate != null) {
            return ResponseEntity.ok(bookingService.getBookingsByMemberAndDateRange(memberName, startDate, endDate));
        }
        
        // If invalid parameter combination
        return ResponseEntity.badRequest().build();
    }
}
