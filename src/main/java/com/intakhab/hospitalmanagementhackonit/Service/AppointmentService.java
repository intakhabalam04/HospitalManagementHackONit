package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;

import java.util.List;

public interface AppointmentService {
    void bookAppointment(Appointment appointment);

    List<AppointmentDto> getAllAppointments();
}
