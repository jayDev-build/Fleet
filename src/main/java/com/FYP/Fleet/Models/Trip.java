package com.FYP.Fleet.Models;

import com.FYP.Fleet.Enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
import java.util.List;
import java.util.ArrayList;


@Entity(name = "Trip")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_seq_gen")
    @SequenceGenerator(
            name = "trip_seq_gen",
            sequenceName = "trip_sequence",
            initialValue = 1,
            allocationSize = 50
    )
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonBackReference
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    @JsonBackReference
    private Vehicle vehicle;

    @NotNull
    private String source;

    @NotNull
    private String destination;

    @NotNull
    private Long freightPrice;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime recordDateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;

    @Column(name = "owner_rate")
    private Long ownerRate;

//    @Column(name = "owner_advance")
//    private Long ownerAdvance;

    @Column(name = "settled")
    @Builder.Default
    private Boolean settled = false;

    @OneToMany(mappedBy = "trip")
    @Builder.Default
    private List<Expense> expenseList = new ArrayList<>();


}
