package com.FYP.Fleet.Models;

import com.FYP.Fleet.Enums.Method;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
