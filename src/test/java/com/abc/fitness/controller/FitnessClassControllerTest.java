package com.abc.fitness.controller;

import com.abc.fitness.model.FitnessClass;
import com.abc.fitness.service.FitnessClassService;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FitnessClassControllerTest {

    @Mock
    private FitnessClassService classService;

    @InjectMocks
    private FitnessClassController classController;

    private FitnessClass testMyFitnessClass;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup 
        testMyFitnessClass = new FitnessClass();
        testMyFitnessClass.setName("Pilates");
        testMyFitnessClass.setStartDate(LocalDate.now().plusDays(1));
        testMyFitnessClass.setEndDate(LocalDate.now().plusDays(30));
        testMyFitnessClass.setStartTime(LocalTime.of(9, 0));
        testMyFitnessClass.setDurationMinutes(60);
        testMyFitnessClass.setCapacity(10);
    }

    @Test
    void createClassSuccessfully() {
        when(classService.createClass(any(FitnessClass.class))).thenReturn(testMyFitnessClass);
        ResponseEntity<?> response = classController.createClass(testMyFitnessClass);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(classService).createClass(any(FitnessClass.class));
    }

    @Test
    void createClassValidationTest() {
        when(classService.createClass(any(FitnessClass.class))).thenThrow(new IllegalArgumentException("Invalid class"));
        ResponseEntity<?> response = classController.createClass(testMyFitnessClass);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> error = (Map<String, String>) response.getBody();
        assertEquals("Invalid class", error.get("error"));
    }
} 