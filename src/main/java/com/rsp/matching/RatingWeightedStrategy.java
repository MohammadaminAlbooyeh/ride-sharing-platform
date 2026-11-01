package com.rsp.matching;

import com.rsp.geo.DistanceCalculator;
import com.rsp.model.dto.RideRequestDTO;
import com.rsp.model.entity.Driver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Matches drivers by balancing rating and proximity.
 * Score = ratingWeight * (rating / 5) + (1 - ratingWeight) * (1 - normalizedDistance),
 * where normalizedDistance is the fraction of the maximum candidate distance.
 */
@Component
public class RatingWeightedStrategy implements MatchingStrategy {

    public static final String NAME = "rating_weighted";
    private static final double RATING_WEIGHT = 0.6;

    private final DistanceCalculator distanceCalculator;

    public RatingWeightedStrategy(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Optional<Driver> selectDriver(RideRequestDTO request, List<Driver> candidates) {
        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        double maxDistance = candidates.stream()
                .mapToDouble(d -> distanceCalculator.distanceKm(d, request.pickupLat(), request.pickupLng()))
                .max()
                .orElse(0.0);

        Driver best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Driver driver : candidates) {
            double distance = distanceCalculator.distanceKm(driver, request.pickupLat(), request.pickupLng());
            double normalizedDistance = maxDistance > 0 ? distance / maxDistance : 0.0;
            double rating = driver.getRating() != null ? driver.getRating() : 5.0;
            rating = Math.max(0.0, Math.min(rating, 5.0)) / 5.0;

            double score = RATING_WEIGHT * rating + (1 - RATING_WEIGHT) * (1 - normalizedDistance);
            if (score > bestScore) {
                bestScore = score;
                best = driver;
            }
        }
        return Optional.ofNullable(best);
    }
}
