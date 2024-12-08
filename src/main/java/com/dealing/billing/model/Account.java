package com.dealing.billing.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Account {

    @Id
    @Column(unique = true, nullable = false)
    private String accountKey; // Set accountKey as the primary key

    @Column(nullable = false)
    private String clientName;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Ignore the activities field during serialization
    private List<Activity> activities;
}