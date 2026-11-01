package com.rsp.matching;

import com.rsp.geo.DistanceCalculator;
import com.rsp.model.dto.RideRequestDTO;
import com.rsp.model.entity.Driver;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Matches the driver closest to the pickup point.
 */
@Component
public class NearestDriverStrategy implements MatchingStrategy {

    public static final String NAME = "nearest";
    private final DistanceCalculator distanceCalculator;

    public NearestDriverStrategy(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Optional<Driver> selectDriver(RideRequestDTO request, List<Driver> candidates) {
        return candidates.stream()
                .min(Comparator.comparingDouble(driver ->
                        distanceCalculator.distanceKm(driver, request.pickupLat(), request.pickupLng())));
    }
}
