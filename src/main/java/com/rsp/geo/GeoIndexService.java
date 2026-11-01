package com.rsp.geo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains a real-time index of available drivers in a Redis GEO key.
 * Operations degrade gracefully (returning empty/neutral values) when Redis
 * is unavailable so callers can fall back to a database query.
 */
@Service
public class GeoIndexService {

    private static final Logger log = LoggerFactory.getLogger(GeoIndexService.class);
    private static final String AVAILABLE_DRIVERS_KEY = "geo:drivers:available";

    private final StringRedisTemplate redisTemplate;

    public GeoIndexService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Adds (or updates) the driver's position in the geo index.
     * Returns {@code false} if the index is not currently reachable.
     */
    public boolean addDriver(Long driverId, double lat, double lng) {
        try {
            redisTemplate.opsForGeo().add(AVAILABLE_DRIVERS_KEY, new Point(lng, lat), driverId.toString());
            return true;
        } catch (RedisConnectionFailureException e) {
            log.warn("Redis unavailable; skipping geo index update for driver {}", driverId);
            return false;
        }
    }

    /**
     * Removes a driver from the geo index.
     */
    public boolean removeDriver(Long driverId) {
        try {
            redisTemplate.opsForGeo().remove(AVAILABLE_DRIVERS_KEY, driverId.toString());
            return true;
        } catch (RedisConnectionFailureException e) {
            log.warn("Redis unavailable; skipping geo index removal for driver {}", driverId);
            return false;
        }
    }

    /**
     * Finds driver ids within {@code radiusKm} of a point (inclusive).
     * Returns an empty list when Redis is unreachable.
     */
    public List<Long> findNearbyDriverIds(double lat, double lng, double radiusKm) {
        try {
            Circle circle = new Circle(new Point(lng, lat), new Distance(radiusKm, Metrics.KILOMETERS));
            var results = redisTemplate.opsForGeo()
                    .search(AVAILABLE_DRIVERS_KEY, circle,
                            RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs()
                                    .sortAscending());

            List<Long> ids = new ArrayList<>();
            if (results != null) {
                for (RedisGeoCommands.GeoLocation<String> loc : results.getContent()) {
                    ids.add(Long.valueOf(loc.getName()));
                }
            }
            return ids;
        } catch (RedisConnectionFailureException | NumberFormatException e) {
            log.warn("Redis unavailable or returned invalid data; returning empty geo results");
            return List.of();
        }
    }
}
