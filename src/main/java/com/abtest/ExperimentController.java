package com.abtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/experiments")
public class ExperimentController {

    @Autowired
    private ExperimentStore store;

    @Autowired
    private StatisticsService statsService;

    @PostMapping
    public ResponseEntity<Experiment> create(@RequestBody Map<String, String> body) {
        Experiment exp = new Experiment(body.get("name"), body.get("description"));
        return ResponseEntity.ok(store.save(exp));
    }

    @GetMapping
    public ResponseEntity<List<Experiment>> getAll() {
        return ResponseEntity.ok(store.findAll());
    }

    @PostMapping("/{id}/results")
    public ResponseEntity<?> addResult(@PathVariable String id,
                                       @RequestBody Map<String, Object> body) {
        Optional<Experiment> exp = store.findById(id);
        if (exp.isEmpty()) return ResponseEntity.notFound().build();

        String variant = (String) body.get("variant");
        double value = Double.parseDouble(body.get("value").toString());
        store.addResult(id, new ExperimentResult(variant, value));
        return ResponseEntity.ok(Map.of("message", "Result added"));
    }

    @GetMapping("/{id}/analysis")
    public ResponseEntity<?> analyze(@PathVariable String id) {
        Optional<Experiment> exp = store.findById(id);
        if (exp.isEmpty()) return ResponseEntity.notFound().build();

        List<ExperimentResult> results = store.getResults(id);
        Map<String, Object> analysis = statsService.analyze(results);
        return ResponseEntity.ok(analysis);
    }
}