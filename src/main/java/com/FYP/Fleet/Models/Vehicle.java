package com.FYP.Fleet.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "Vehicle",
    uniqueConstraints = {
            @UniqueConstraint(
                    name = "vehicle_number_user_constriant",
                    columnNames = {"user_id", "number"}
            )
    }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_seq_gen")
    @SequenceGenerator(
            name = "vehicle_seq_gen",
            sequenceName = "vehicle_sequence",
            initialValue = 1,
            allocationSize = 50
    )
    private long id;

    @Column(name = "number")
    @NotNull
    private String number;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = true)
    private Owner owner;

    @OneToMany(mappedBy = "vehicle")
    private List<Trip> tripList = new ArrayList<>();


}
