package com.rsp.matching;

import com.rsp.model.dto.RideRequestDTO;
import com.rsp.model.entity.Driver;

import java.util.List;
import java.util.Optional;

/**
 * Strategy to pick the best driver from a set of candidates for a ride request.
 */
public interface MatchingStrategy {

    /** Stable identifier used by {@link MatchingStrategyFactory}. */
    String name();

    /**
     * Picks the preferred driver for the given request, if any candidate applies.
     */
    Optional<Driver> selectDriver(RideRequestDTO request, List<Driver> candidates);
}
