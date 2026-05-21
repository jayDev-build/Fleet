package com.FYP.Fleet.Models;

import com.FYP.Fleet.Enums.ExpenseType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "Expense")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_seq_gen")
    @SequenceGenerator(
            name = "expense_seq_gen",
            sequenceName = "expense_sequence",
            initialValue = 1,
            allocationSize = 50
    )
    private long id;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    @JsonBackReference
    private Trip trip;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ExpenseType expenseType;

    private String note;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long amount;

    private LocalDateTime recordDateTime;
}
