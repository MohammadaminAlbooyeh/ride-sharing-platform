package com.rsp.repository;

import com.rsp.model.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiderRepository extends JpaRepository<Rider, Long> {

    Optional<Rider> findByEmail(String email);

    Optional<Rider> findByPhone(String phone);

    boolean existsByEmail(String email);
}
