package com.rsp.model.dto;

import com.rsp.model.enums.VehicleType;
import jakarta.validation.constraints.NotNull;

/**
 * Input payload for requesting a new ride.
 */
public record RideRequestDTO(
        @NotNull Double pickupLat,
        @NotNull Double pickupLng,
        @NotNull Double dropoffLat,
        @NotNull Double dropoffLng,
        @NotNull VehicleType vehicleType
) {
}
