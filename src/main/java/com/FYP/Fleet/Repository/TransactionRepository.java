package com.FYP.Fleet.Repository;

import com.FYP.Fleet.Models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    List<Transactions> findTransactionByUserIdAndOwnerId(Long userId,Long ownerId);

    @Query("""
    SELECT COALESCE(SUM(t.amount), 0L) 
    FROM transactions t 
    WHERE t.user.id = :userId 
    AND t.owner.id = :ownerId
""")
    Long sumAmountByUserIdAndOwnerId(
            @Param("userId") Long userId,
            @Param("ownerId") Long ownerId
    );
}
