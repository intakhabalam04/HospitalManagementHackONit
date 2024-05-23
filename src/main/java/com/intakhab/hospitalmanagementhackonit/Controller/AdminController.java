package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

public interface AdminController{
    ModelAndView home();
    ModelAndView doctorList();
    ResponseEntity<?> getDoctors();
    ResponseEntity<?> getDoctor(UUID id);
    ResponseEntity<?> updateDoctor(UUID id, Doctor doctor);
    ResponseEntity<?> deleteDoctor(UUID id);
    ModelAndView patientList();
    ModelAndView appointmentDetails();
    ModelAndView addDoctor();
    ResponseEntity<?> addDoctorPost(Doctor doctor);
    ResponseEntity<?> getAppointments();
    ModelAndView bloodDonations();
    ModelAndView organDonations();
    ResponseEntity<?> getBloodDonations();
    ResponseEntity<?> getOrganDonations();
}
