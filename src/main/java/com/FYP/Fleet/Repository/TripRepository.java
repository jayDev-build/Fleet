package com.FYP.Fleet.Repository;

import com.FYP.Fleet.Models.Expense;
import com.FYP.Fleet.Models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("""
        SELECT t FROM Trip t\s
        WHERE t.vehicle.id IN :vehicleIds\s
        AND t.status = com.FYP.Fleet.Enums.Status.COMPLETED
   \s""")
    List<Trip> findCompletedTripsByVehicleIds(@Param("vehicleIds") List<Long> vehicleIds);

    List<Expense> findByIdIn(List<Long> tripIds);

    @Query("""
        SELECT t FROM Trip t\s
        WHERE t.user.id = :userId\s
        AND t.status = com.FYP.Fleet.Enums.Status.COMPLETED
   \s""")
    List<Trip> findCompletedTripsByUserId(@Param("userId")Long userId);

    @Query("""
        SELECT t FROM Trip t\s
        JOIN Vehicle v\s
        ON v.id = t.vehicle.id\s
        WHERE t.user.id = :userId\s
        AND v.owner.id = :ownerId\s
        AND t.status = com.FYP.Fleet.Enums.Status.COMPLETED
   \s""")
    List<Trip> getCompletedTripsByOwnerIdAndUserId(@Param("ownerId") Long ownerId, @Param("userId") Long userId);

    List<Trip> findByUserId(Long userId);

    @Query("""
            select t from Trip t\s
            where t.status = com.FYP.Fleet.Enums.Status.ACTIVE\s
            AND t.user.id = :userId\s
            """)
    List<Trip> findActiveTripsByUserId(@Param("userID") Long userId);

    @Query("""
            select t from Trip t\s
            where t.vehicle.owner.id = :ownerId\s
            and t.user.id = :userId\s
            """)
    List<Trip> getTripsByUserIdAndOwnerId(@Param("userId")Long userId, @Param("ownerId") Long ownerId);

    Optional<Trip> findByIdAndUserId(long tripId, long userId);

    @Query("""
    SELECT t FROM Trip t 
    WHERE t.status = com.FYP.Fleet.Enums.Status.ACTIVE 
    AND (
        SELECT MAX(e.recordDateTime) 
        FROM Expense e 
        WHERE e.trip = t
    ) < :boundaryTime
""")
    List<Trip> findTripsWhereLastExpenseIsOlderThan24Hours(@Param("boundaryTime") LocalDateTime boundaryTime);

    @Query("""
    SELECT COALESCE(SUM(t.ownerRate), 0L) 
    FROM Trip t 
    WHERE t.user.id = :userId 
    AND t.vehicle.owner.id = :ownerId 
""")
    Long sumOwnerRateByUserIdAndOwnerId(
            @Param("userId") Long userId,
            @Param("ownerId") Long ownerId
    );
}
