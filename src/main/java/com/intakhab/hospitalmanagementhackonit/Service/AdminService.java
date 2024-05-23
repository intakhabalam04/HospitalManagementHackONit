package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Dto.DoctorDto;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.BloodDonation;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Model.OrganDonation;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    boolean addDoctor(Doctor doctor);
    List<DoctorDto> getDoctors();
    void deleteDoctor(UUID id);
    void updateDoctor(Doctor existingDoctor, Doctor doctor);
    List<AppointmentDto> getAppointments();
    List<BloodDonation> getBloodDonations();
    List<OrganDonation> getOrganDonations();
}
