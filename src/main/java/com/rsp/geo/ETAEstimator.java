package com.rsp.geo;

import org.springframework.stereotype.Component;

/**
 * Estimates travel time in minutes for a given distance using a
 * configurable average speed (default 25 km/h, typical for urban rides).
 */
@Component
public class ETAEstimator {

    private final double averageSpeedKmh;

    public ETAEstimator() {
        this(25.0);
    }

    public ETAEstimator(double averageSpeedKmh) {
        if (averageSpeedKmh <= 0) {
            throw new IllegalArgumentException("average speed must be positive");
        }
        this.averageSpeedKmh = averageSpeedKmh;
    }

    /**
     * Estimated minutes to cover the given distance (minimum 1 minute).
     */
    public int minutesForDistance(double distanceKm) {
        double minutes = (distanceKm / averageSpeedKmh) * 60.0;
        return Math.max(1, (int) Math.ceil(minutes));
    }
}
