package com.FYP.Fleet.Repository;

import com.FYP.Fleet.Models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByNumber(String number);

    List<Vehicle> findByUserId(Long id);

    Optional<Vehicle> findByNumberAndUserId(String number, Long userId);

    Optional<Vehicle> findByIdAndUserId(long id, long userId);
}
