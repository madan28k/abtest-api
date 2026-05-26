package com.abtest;

public class ExperimentResult {
    private String variant; // "control" or "treatment"
    private double value;

    public ExperimentResult(String variant, double value) {
        this.variant = variant;
        this.value = value;
    }

    public String getVariant() { return variant; }
    public double getValue() { return value; }
}