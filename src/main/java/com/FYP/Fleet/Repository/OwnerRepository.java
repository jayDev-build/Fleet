package com.FYP.Fleet.Repository;

import com.FYP.Fleet.Models.Owner;
import com.FYP.Fleet.Models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    // All owners for a specific fleet operator
    List<Owner> findByUserId(Long userId);

    // Check duplicate phone per user
    boolean existsByPhoneAndUserId(String phone, Long userId);

    // For balance sheet query
    @Query("""
        SELECT o FROM Owner o
        LEFT JOIN FETCH o.vehicles v
        WHERE o.id = :ownerId AND o.user.id = :userId
    """)
    Optional<Owner> findByIdAndUserId(Long ownerId, Long userId);

    @Query("SELECT o FROM Owner o JOIN o.vehicles v WHERE v.number = :vehicleNumber")
    Owner findByVehicleNumber(@Param("vehicleNumber") String vehicleNumber);
}