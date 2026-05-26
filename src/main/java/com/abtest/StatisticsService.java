package com.abtest;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.*;

@Service
public class StatisticsService {

    public Map<String, Object> analyze(List<ExperimentResult> results) {
        List<Double> control = results.stream()
            .filter(r -> r.getVariant().equals("control"))
            .map(ExperimentResult::getValue)
            .collect(Collectors.toList());

        List<Double> treatment = results.stream()
            .filter(r -> r.getVariant().equals("treatment"))
            .map(ExperimentResult::getValue)
            .collect(Collectors.toList());

        Map<String, Object> analysis = new LinkedHashMap<>();

        if (control.isEmpty() || treatment.isEmpty()) {
            analysis.put("error", "Need both control and treatment results");
            return analysis;
        }

        double controlMean = mean(control);
        double treatmentMean = mean(treatment);
        double controlStd = std(control);
        double treatmentStd = std(treatment);

        double zScore = zScore(controlMean, treatmentMean, controlStd, treatmentStd,
                               control.size(), treatment.size());
        double pValue = pValue(zScore);

        analysis.put("control_count", control.size());
        analysis.put("treatment_count", treatment.size());
        analysis.put("control_mean", round(controlMean));
        analysis.put("treatment_mean", round(treatmentMean));
        analysis.put("control_std", round(controlStd));
        analysis.put("treatment_std", round(treatmentStd));
        analysis.put("z_score", round(zScore));
        analysis.put("p_value", round(pValue));
        analysis.put("significant", pValue < 0.05);
        analysis.put("verdict", pValue < 0.05
            ? "Treatment is statistically significant at 95% confidence"
            : "No significant difference detected");

        return analysis;
    }

    private double mean(List<Double> data) {
        return data.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private double std(List<Double> data) {
        double mean = mean(data);
        double variance = data.stream()
            .mapToDouble(v -> Math.pow(v - mean, 2))
            .average().orElse(0);
        return Math.sqrt(variance);
    }

    private double zScore(double m1, double m2, double s1, double s2, int n1, int n2) {
        double se = Math.sqrt((s1 * s1 / n1) + (s2 * s2 / n2));
        return se == 0 ? 0 : (m2 - m1) / se;
    }

    private double pValue(double z) {
        double absZ = Math.abs(z);
        // Two-tailed p-value approximation
        return 2 * (1 - normalCDF(absZ));
    }

    private double normalCDF(double z) {
        return 0.5 * (1 + erf(z / Math.sqrt(2)));
    }

    private double erf(double x) {
        double t = 1.0 / (1.0 + 0.3275911 * Math.abs(x));
        double y = 1.0 - (((((1.061405429 * t - 1.453152027) * t)
            + 1.421413741) * t - 0.284496736) * t + 0.254829592) * t * Math.exp(-x * x);
        return x >= 0 ? y : -y;
    }

    private double round(double val) {
        return Math.round(val * 10000.0) / 10000.0;
    }
}