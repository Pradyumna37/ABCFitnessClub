package com.abc.fitness.service;

import com.abc.fitness.model.Booking;
import com.abc.fitness.model.FitnessClass;
import com.abc.fitness.repositories.BookingRepository;
import com.abc.fitness.repositories.FitnessClassRepository;
import com.abc.fitness.info.booking.BookingDetailsInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FitnessClassRepository classRepository;

    @InjectMocks
    private BookingService bookingService;

    private FitnessClass testFitnessClass;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testFitnessClass = new FitnessClass();
        testFitnessClass.setName("Pilates");
        testFitnessClass.setStartDate(LocalDate.now().plusDays(1));
        testFitnessClass.setEndDate(LocalDate.now().plusDays(30));
        testFitnessClass.setStartTime(LocalTime.of(9, 0));
        testFitnessClass.setCapacity(10);
        testBooking = new Booking();
        testBooking.setMemberName("Pradyumna");
        testBooking.setFitnessClass(testFitnessClass);
        testBooking.setParticipationDate(LocalDate.now().plusDays(1));
    }

    @Test
    void createBookingTest() {
        when(classRepository.findByName(anyString())).thenReturn(Arrays.asList(testFitnessClass));
        when(bookingRepository.countBookingsForClassAndDate(any(), any())).thenReturn(5);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.createBooking(testBooking);

        assertNotNull(result);
        assertEquals("Pradyumna", result.getMemberName());
        assertEquals("Pilates", result.getFitnessClass().getName());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBookingWithClassFullCapacityTest() {
        when(classRepository.findByName(anyString())).thenReturn(Arrays.asList(testFitnessClass));
        when(bookingRepository.countBookingsForClassAndDate(any(), any())).thenReturn(10);

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(testBooking);
        });
    }

    @Test
    void createBookingWithInvalidDateTest() {
        testBooking.setParticipationDate(LocalDate.now().minusDays(1));
        when(classRepository.findByName(anyString())).thenReturn(Arrays.asList(testFitnessClass));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(testBooking);
        });
    }

    @Test
    void getBookingsByMemberTest() {
        when(bookingRepository.findByMemberName(anyString()))
            .thenReturn(Arrays.asList(testBooking));

        List<BookingDetailsInfo> results = bookingService.getBookingsByMember("Pradyumna");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Pradyumna", results.get(0).getMemberName());
        assertEquals("Pilates", results.get(0).getClassName());
    }

    @Test
    void getBookingsByDateRangeTest() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        when(bookingRepository.findByDateRange(any(), any())).thenReturn(Arrays.asList(testBooking));
        List<BookingDetailsInfo> results = bookingService.getBookingsByDateRange(startDate, endDate);
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Pradyumna", results.get(0).getMemberName());
        assertEquals("Pilates", results.get(0).getClassName());
        assertEquals(LocalDate.now().plusDays(1), results.get(0).getBookingDate());
        assertEquals(LocalTime.of(9, 0), results.get(0).getClassStartTime());
    }

    @Test
    void getBookingsByMemberAndDateRangeTest() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        when(bookingRepository.findByMemberAndDateRange(anyString(), any(), any()))
            .thenReturn(Arrays.asList(testBooking));

        List<BookingDetailsInfo> results = bookingService.getBookingsByMemberAndDateRange("Pradyumna", startDate, endDate);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Pradyumna", results.get(0).getMemberName());
        assertEquals("Pilates", results.get(0).getClassName());
        assertEquals(LocalDate.now().plusDays(1), results.get(0).getBookingDate());
        assertEquals(LocalTime.of(9, 0), results.get(0).getClassStartTime());
    }
} 