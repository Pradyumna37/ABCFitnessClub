package com.abc.fitness.service;

import com.abc.fitness.model.FitnessClass;
import com.abc.fitness.repositories.FitnessClassRepository;
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

class FitnessClassServiceTest {

    @Mock
    private FitnessClassRepository classRepo;

    @InjectMocks
    private FitnessClassService classService;

    private FitnessClass testFitnessClass;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testFitnessClass = new FitnessClass();
        testFitnessClass.setName("Yoga");
        testFitnessClass.setStartDate(LocalDate.now().plusDays(1));
        testFitnessClass.setEndDate(LocalDate.now().plusDays(30));
        testFitnessClass.setStartTime(LocalTime.of(9, 0));
        testFitnessClass.setDurationMinutes(60);
        testFitnessClass.setCapacity(10);
    }

    @Test
    void createClassTest() {
        when(classRepo.save(any(FitnessClass.class))).thenReturn(testFitnessClass);
        FitnessClass result = classService.createClass(testFitnessClass);
        assertNotNull(result);
        assertEquals("Yoga", result.getName());
        assertEquals(10, result.getCapacity());
        verify(classRepo).save(any(FitnessClass.class));
    }

    @Test
    void createClassInvalidNameTest() {
        testFitnessClass.setName("");
        assertThrows(IllegalArgumentException.class, () -> {
            classService.createClass(testFitnessClass);
        });
    }

    @Test
    void createClassWithInvalidCapacityTest() {
        testFitnessClass.setCapacity(0);
        assertThrows(IllegalArgumentException.class, () -> {
            classService.createClass(testFitnessClass);
        });
    }

    @Test
    void createClassWithInvalidDateRangeTets() {
        testFitnessClass.setStartDate(LocalDate.now().plusDays(30));
        testFitnessClass.setEndDate(LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class, () -> {
            classService.createClass(testFitnessClass);
        });
    }
} 