package com.rsp.repository;

import com.rsp.model.entity.Driver;
import com.rsp.model.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);

    Optional<Driver> findByPhone(String phone);

    boolean existsByEmail(String email);

    List<Driver> findByStatus(DriverStatus status);

    /**
     * Finds drivers whose status is AVAILABLE and whose last known position
     * falls within a bounding box. Used as the JPA fallback for driver matching.
     */
    @Query("""
            select d from Driver d
            where d.status = :status
              and d.currentLat is not null
              and d.currentLng is not null
              and d.currentLat between :minLat and :maxLat
              and d.currentLng between :minLng and :maxLng
            """)
    List<Driver> findAvailableNear(@Param("status") DriverStatus status,
                                   @Param("minLat") double minLat,
                                   @Param("maxLat") double maxLat,
                                   @Param("minLng") double minLng,
                                   @Param("maxLng") double maxLng);
}
