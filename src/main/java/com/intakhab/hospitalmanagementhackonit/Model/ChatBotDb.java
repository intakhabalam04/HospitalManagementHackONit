package com.intakhab.hospitalmanagementhackonit.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotDb {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String symptom;
    private String doctor;
    private LocalDateTime chatBotTime;
    private String patientName;
    private int age;
    private String gender;
}
