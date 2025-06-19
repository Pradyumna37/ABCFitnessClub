package com.abc.fitness.service;

import com.abc.fitness.model.FitnessClass;
import com.abc.fitness.repositories.FitnessClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FitnessClassService {
    private final FitnessClassRepository classRepository;

    @Autowired
    public FitnessClassService(FitnessClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public FitnessClass createClass(FitnessClass fitnessClass) {
        validateClass(fitnessClass);
        return classRepository.save(fitnessClass);
    }

    private void validateClass(FitnessClass fitnessClass) {
        if (fitnessClass.getName() == null || fitnessClass.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Class name is required");
        }
        if (fitnessClass.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }
        if (fitnessClass.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required");
        }
        if (fitnessClass.getStartTime() == null) {
            throw new IllegalArgumentException("Start time is required");
        }
        if (fitnessClass.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        if (fitnessClass.getCapacity() < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }
        if (fitnessClass.getEndDate().isBefore(fitnessClass.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (fitnessClass.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("End date must be in the future");
        }
    }
} 