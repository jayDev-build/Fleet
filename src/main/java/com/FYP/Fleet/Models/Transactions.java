package com.FYP.Fleet.Models;

import com.FYP.Fleet.Enums.Method;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "transactions")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_seq")
    @SequenceGenerator(
            name = "transactions_seq",
            sequenceName = "transactions_seq",
            initialValue = 1,
            allocationSize = 50
    )
    private Long id;

    @NotNull
    private Long amount;

    @NotNull
    private LocalDate date;

    private String note;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Method method;

    @JoinColumn(name = "owner_id")
    @ManyToOne
    @NotNull
    private Owner owner;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @NotNull
    private User user;

    private LocalDateTime recordDateTime;

}
