package com.dealing.billing.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_key", nullable = false)
    @JsonBackReference // Prevent infinite recursion during serialization
    private Account account;

    private String activityType; // Custody, Trade, Wire
    private double quantity;
    private LocalDate activityDate;
}
