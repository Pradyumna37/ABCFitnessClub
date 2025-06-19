package com.abc.fitness.controller;

import com.abc.fitness.model.Booking;
import com.abc.fitness.model.FitnessClass;
import com.abc.fitness.service.BookingService;
import com.abc.fitness.info.booking.BookingDetailsInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private Booking testBookings;
    private FitnessClass testFitnessClass;

    @BeforeEach
    void setUp() {
    	//Setting up
        MockitoAnnotations.openMocks(this);
        testFitnessClass = new FitnessClass();
        testFitnessClass.setName("Boxing");
        testFitnessClass.setStartDate(LocalDate.now().plusDays(1));
        testFitnessClass.setEndDate(LocalDate.now().plusDays(30));
        testFitnessClass.setStartTime(LocalTime.of(9, 0));
        testFitnessClass.setCapacity(10);
        
        testBookings = new Booking();
        testBookings.setMemberName("Pradyumna");
        testBookings.setFitnessClass(testFitnessClass);
        testBookings.setParticipationDate(LocalDate.now().plusDays(1));
    }

    @Test
    void createBookingTest() {
        when(bookingService.createBooking(any(Booking.class))).thenReturn(testBookings);
        ResponseEntity<?> response = bookingController.createBooking(testBookings);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(bookingService).createBooking(any(Booking.class));
    }

    @Test
    void createBookingValidationTest() {
        when(bookingService.createBooking(any(Booking.class)))
            .thenThrow(new IllegalArgumentException("Invalid booking"));
        ResponseEntity<?> response = bookingController.createBooking(testBookings);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid booking", response.getBody());
    }

    @Test
    void searchBookingsByMemberTest() {
        List<BookingDetailsInfo> expectedBookings = Arrays.asList(
            new BookingDetailsInfo("Boxing", LocalTime.of(9, 0), LocalDate.now().plusDays(1), "Pradyumna")
        );
        when(bookingService.getBookingsByMember(anyString())).thenReturn(expectedBookings);

        ResponseEntity<List<BookingDetailsInfo>> response = bookingController.searchBookings("Pradyumna", null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Pradyumna", response.getBody().get(0).getMemberName());
    }

    @Test
    void searchBookingsByDateRangeTest() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        List<BookingDetailsInfo> expectedBookings = Arrays.asList(
            new BookingDetailsInfo("Boxing", LocalTime.of(9, 0), LocalDate.now().plusDays(1), "Pradyumna")
        );
        when(bookingService.getBookingsByDateRange(any(), any())).thenReturn(expectedBookings);
        ResponseEntity<List<BookingDetailsInfo>> response = bookingController.searchBookings(null, startDate, endDate);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void searchBookingsInvalidParametersTest() {
        ResponseEntity<List<BookingDetailsInfo>> response = bookingController.searchBookings("Pradyumna", LocalDate.now(), null);

        assertEquals(400, response.getStatusCodeValue());
    }
} 