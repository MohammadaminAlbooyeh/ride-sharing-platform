package com.rsp.repository;

import com.rsp.model.entity.RideEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideEventRepository extends JpaRepository<RideEvent, Long> {

    List<RideEvent> findByRideIdOrderByCreatedAtAsc(Long rideId);
}
