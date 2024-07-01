package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    Appointment bookAppointment(Appointment appointment);
    List<AppointmentDto> getAllAppointments();

    void uploadPatientReport(MultipartFile file, UUID appointmentId);

}
