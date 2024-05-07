package com.intakhab.hospitalmanagementhackonit.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private UUID id;
    private String patientName;
    private int age;
    private String symptoms;
    private String gender;
    private int consultationFee;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private UUID doctorId;
    private UUID userId;
    private String doctorName;
    private String email;
    private String mobile;
}