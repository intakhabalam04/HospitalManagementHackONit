package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Dto.DoctorDto;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Model.MedicineSuggestion;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    Doctor getDoctor(UUID id);
    List<DoctorDto> getAllDoctors();
    List<AppointmentDto> getDoctorsAppointments();
    DoctorDto getDoctorDto(UUID id);
    MedicineSuggestion recommend(String medicine);
    int getTodayAppointmentsNo();
    List<AppointmentDto> prescriptionNeeded();
    Object savePrescription(UUID prescription,String drugsName) throws MessagingException;
    Object updateAppointment(UUID id, int days);
}
