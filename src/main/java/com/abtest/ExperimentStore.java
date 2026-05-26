package com.abtest;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ExperimentStore {
    private Map<String, Experiment> experiments = new HashMap<>();
    private Map<String, List<ExperimentResult>> results = new HashMap<>();

    public Experiment save(Experiment exp) {
        experiments.put(exp.getId(), exp);
        results.put(exp.getId(), new ArrayList<>());
        return exp;
    }

    public Optional<Experiment> findById(String id) {
        return Optional.ofNullable(experiments.get(id));
    }

    public List<Experiment> findAll() {
        return new ArrayList<>(experiments.values());
    }

    public void addResult(String id, ExperimentResult result) {
        results.getOrDefault(id, new ArrayList<>()).add(result);
    }

    public List<ExperimentResult> getResults(String id) {
        return results.getOrDefault(id, new ArrayList<>());
    }
}