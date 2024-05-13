package com.intakhab.hospitalmanagementhackonit.Model;

import com.intakhab.hospitalmanagementhackonit.Enum.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrganDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate dob;
    private String blood_type;
    private float weight;
    private String address;
    private String mobile;
    private String organs;
    private boolean registered_donor;
    private String reason;
    private boolean discussion;
    private boolean awareness;
    private String medical_condition;
    private String preferences;
    private boolean diseases;
    @Lob
    private String signatureData;
}
