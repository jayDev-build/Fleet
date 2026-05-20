package com.FYP.Fleet.Repository;

import com.FYP.Fleet.Models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByTripIdIn(List<Long> tripIds);

    @Query("""
            select e from Expense e\s
            where e.id = :expenseId\s
            and e.trip.user.id = :userId\s
            """)
    Optional<Expense> findByIdAndUserId(@Param("expenseId") long expenseId, @Param("userId") Long userId);

}
