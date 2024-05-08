package com.intakhab.hospitalmanagementhackonit.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {
    private UUID id;
    private String name;
    private String email;
    private String specialization;
    private String mobile;
    private int consultancyFee;
    private UUID userId;
    private List<UUID> appointmentIds;
    private String roomID;
}