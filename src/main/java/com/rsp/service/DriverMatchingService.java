package com.rsp.service;

import com.rsp.geo.DistanceCalculator;
import com.rsp.geo.GeoIndexService;
import com.rsp.matching.MatchingStrategy;
import com.rsp.matching.MatchingStrategyFactory;
import com.rsp.model.dto.RideRequestDTO;
import com.rsp.model.entity.Driver;
import com.rsp.model.enums.DriverStatus;
import com.rsp.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Orchestrates candidate discovery (Redis GEO with a database fallback) and
 * driver selection using the configured {@link MatchingStrategy}.
 */
@Service
public class DriverMatchingService {

    private static final Logger log = LoggerFactory.getLogger(DriverMatchingService.class);
    private static final double EARTH_KM_PER_DEGREE = 111.0;

    private final DriverRepository driverRepository;
    private final GeoIndexService geoIndexService;
    private final DistanceCalculator distanceCalculator;
    private final MatchingStrategyFactory strategyFactory;
    private final double searchRadiusKm;

    public DriverMatchingService(DriverRepository driverRepository,
                                 GeoIndexService geoIndexService,
                                 DistanceCalculator distanceCalculator,
                                 MatchingStrategyFactory strategyFactory,
                                 @Value("${rsp.matching.search-radius-km:5.0}") double searchRadiusKm) {
        this.driverRepository = driverRepository;
        this.geoIndexService = geoIndexService;
        this.distanceCalculator = distanceCalculator;
        this.strategyFactory = strategyFactory;
        this.searchRadiusKm = searchRadiusKm;
    }

    /**
     * Finds the best matching driver for a request using the given strategy
     * (falls back to the default strategy when the name is unknown).
     */
    @Transactional(readOnly = true)
    public Optional<Driver> matchDriver(RideRequestDTO request, String strategyName) {
        MatchingStrategy strategy = strategyFactory.get(strategyName)
                .orElseGet(strategyFactory::defaultStrategy);
        if (strategy == null) {
            throw new IllegalStateException("No default matching strategy configured");
        }

        List<Driver> candidates = findCandidates(request);
        log.debug("Found {} candidate driver(s) for request at ({}, {})",
                candidates.size(), request.pickupLat(), request.pickupLng());
        return strategy.selectDriver(request, candidates);
    }

    private List<Driver> findCandidates(RideRequestDTO request) {
        List<Long> nearbyIds = geoIndexService.findNearbyDriverIds(
                request.pickupLat(), request.pickupLng(), searchRadiusKm);

        if (!nearbyIds.isEmpty()) {
            return filterByVehicleType(driverRepository.findAllById(nearbyIds), request)
                    .stream()
                    .filter(Driver::isAvailable)
                    .toList();
        }

        // Fallback: bounding-box query against the database.
        double dLat = searchRadiusKm / EARTH_KM_PER_DEGREE;
        double dLng = searchRadiusKm
                / (EARTH_KM_PER_DEGREE * Math.max(Math.abs(Math.cos(Math.toRadians(request.pickupLat()))), 0.01));

        List<Driver> nearby = driverRepository.findAvailableNear(
                DriverStatus.AVAILABLE,
                request.pickupLat() - dLat, request.pickupLat() + dLat,
                request.pickupLng() - dLng, request.pickupLng() + dLng);

        return filterByVehicleType(nearby, request);
    }

    private List<Driver> filterByVehicleType(List<Driver> drivers, RideRequestDTO request) {
        return drivers.stream()
                .filter(driver -> driver.getVehicle() != null
                        && driver.getVehicle().getType() == request.vehicleType())
                .toList();
    }
}
