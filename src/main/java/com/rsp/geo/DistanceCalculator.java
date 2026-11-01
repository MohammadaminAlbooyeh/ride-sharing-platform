package com.rsp.geo;

import com.rsp.model.entity.Driver;
import org.springframework.stereotype.Component;

/**
 * Haversine distance calculations on the Earth's surface.
 */
@Component
public class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;

    /**
     * Great-circle distance in kilometres between two coordinates.
     */
    public double distanceKm(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    /**
     * Distance from a driver's last known position to a target point.
     */
    public double distanceKm(Driver driver, double lat, double lng) {
        if (driver.getCurrentLat() == null || driver.getCurrentLng() == null) {
            return Double.MAX_VALUE;
        }
        return distanceKm(driver.getCurrentLat(), driver.getCurrentLng(), lat, lng);
    }
}
