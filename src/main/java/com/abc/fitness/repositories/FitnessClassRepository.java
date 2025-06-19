package com.abc.fitness.repositories;

import com.abc.fitness.model.FitnessClass;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FitnessClassRepository {
    private final List<FitnessClass> classes = new ArrayList<>();

    public FitnessClass save(FitnessClass fitnessClass) {
        classes.add(fitnessClass);
        return fitnessClass;
    }

    public List<FitnessClass> findAll() {
        return new ArrayList<>(classes);
    }

    public List<FitnessClass> findByName(String name) {
        return classes.stream()
                .filter(c -> c.getName().equals(name))
                .collect(Collectors.toList());
    }
} 