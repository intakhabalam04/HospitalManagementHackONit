package com.intakhab.hospitalmanagementhackonit.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private LocalDate dob;
    private String email;
    private String gender;
    private int pincode;
    private int premium;
    private int sumInsured;
    private String insuranceName;
    private LocalDate policyEndDate;
    @ManyToOne
    private User user;
}
