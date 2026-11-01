package com.rsp.service;

import com.rsp.geo.GeoIndexService;
import com.rsp.model.entity.Driver;
import com.rsp.model.enums.DriverStatus;
import com.rsp.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Manages a driver's availability status and keeps the Redis geo index
 * in sync with their latest known location.
 */
@Service
public class DriverAvailabilityService {

    private final DriverRepository driverRepository;
    private final GeoIndexService geoIndexService;

    public DriverAvailabilityService(DriverRepository driverRepository, GeoIndexService geoIndexService) {
        this.driverRepository = driverRepository;
        this.geoIndexService = geoIndexService;
    }

    /**
     * Marks a driver as accepting new rides and records their position.
     */
    @Transactional
    public Driver markAvailable(Long driverId, double lat, double lng) {
        Driver driver = requireDriver(driverId);
        driver.setStatus(DriverStatus.AVAILABLE);
        driver.setCurrentLat(lat);
        driver.setCurrentLng(lng);
        Driver saved = driverRepository.save(driver);
        geoIndexService.addDriver(saved.getId(), lat, lng);
        return saved;
    }

    /**
     * Updates the driver's live position (does not change availability).
     */
    @Transactional
    public Driver updateLocation(Long driverId, double lat, double lng) {
        Driver driver = requireDriver(driverId);
        driver.setCurrentLat(lat);
        driver.setCurrentLng(lng);
        Driver saved = driverRepository.save(driver);
        if (saved.getStatus() == DriverStatus.AVAILABLE) {
            geoIndexService.addDriver(saved.getId(), lat, lng);
        }
        return saved;
    }

    /**
     * Marks a driver busy (currently on a ride) and removes them from the index.
     */
    @Transactional
    public Driver markBusy(Long driverId) {
        Driver driver = requireDriver(driverId);
        driver.setStatus(DriverStatus.BUSY);
        Driver saved = driverRepository.save(driver);
        geoIndexService.removeDriver(saved.getId());
        return saved;
    }

    /**
     * Marks a driver offline and removes them from the index.
     */
    @Transactional
    public Driver markOffline(Long driverId) {
        Driver driver = requireDriver(driverId);
        driver.setStatus(DriverStatus.OFFLINE);
        Driver saved = driverRepository.save(driver);
        geoIndexService.removeDriver(saved.getId());
        return saved;
    }

    private Driver requireDriver(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));
    }
}
