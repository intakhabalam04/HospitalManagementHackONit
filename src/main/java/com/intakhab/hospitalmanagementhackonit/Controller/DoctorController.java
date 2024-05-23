package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public interface DoctorController {
    ModelAndView home();
    ModelAndView appointments();
    ResponseEntity<?> getDoctorsAppointments();
    ModelAndView videocall(String roomID);
    ModelAndView prescription();
    ResponseEntity<?> prescriptionNeeded();
    ResponseEntity<?> savePrescription(Appointment appointment) throws MessagingException;
}