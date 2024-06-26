package com.intakhab.hospitalmanagementhackonit.Model;

import com.intakhab.hospitalmanagementhackonit.Enum.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate dateOfBirth;
    private String bloodGroup;
    private float weight;
    private boolean diseases;
    private String street;
    private String area;
    private String city;
    private int pincode;
    private String mobile;
}
