package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Dto.DoctorDto;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    Doctor getDoctor(UUID id);

    List<DoctorDto> getAllDoctors();

    List<AppointmentDto> getDoctorsAppointments();

    DoctorDto getDoctorDto(UUID id);
}
