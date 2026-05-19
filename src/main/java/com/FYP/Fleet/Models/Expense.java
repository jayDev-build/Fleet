package com.FYP.Fleet.Models;

import com.FYP.Fleet.Enums.ExpenseType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
