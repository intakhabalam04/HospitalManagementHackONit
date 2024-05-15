package com.intakhab.hospitalmanagementhackonit.Dto;

import com.intakhab.hospitalmanagementhackonit.Enum.UserAction;
import com.intakhab.hospitalmanagementhackonit.Enum.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String mobile;
    private String username;
    private UserRole role;
    private UserAction action;
    private LocalDate registrationDate;
    private String token;
    private long tokenExpiryTime;
    private List<UUID> appointmentIds;
    private String gender;
}
