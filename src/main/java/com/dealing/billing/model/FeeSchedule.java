package com.dealing.billing.model;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FeeSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_key", referencedColumnName = "accountKey", nullable = false)
    private Account account;

    private double bps;
    private double tradeRate;
    private double wireRate;
}