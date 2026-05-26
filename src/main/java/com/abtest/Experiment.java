package com.abtest;

import java.util.UUID;

public class Experiment {
    private String id;
    private String name;
    private String description;

    public Experiment(String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}