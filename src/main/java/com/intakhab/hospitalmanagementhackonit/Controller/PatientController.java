package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public interface PatientController {
    ModelAndView home();
    ModelAndView bookAppointment();
    ModelAndView bookAppointmentByChatbot();
    ResponseEntity<?> bookAppointment1(Appointment appointment);
    ModelAndView appointmentHistory();
    ResponseEntity<?> allAppointments();
    ResponseEntity<?> allDoctors();
    ModelAndView chatbot();
    ResponseEntity<?> chatbot1(ChatBot chatBot);
    ModelAndView videoCall(String roomID, String appointmentid);
    ResponseEntity<?> recommend(MedicineSuggestion medicineSuggestion);
    ModelAndView recommend();
    ModelAndView diseaseDescription();
    ModelAndView messages();
    ModelAndView getMentalHealthPage();
    ModelAndView donateBloodPage();
    ResponseEntity<?> registerBloodDonation(BloodDonation bloodDonation);
    ModelAndView organDonationPage();
    ResponseEntity<?> donateOrgans(OrganDonation organDonation);
    ResponseEntity<?> getPatientData();
    ModelAndView nearbyHospital();
    ModelAndView insurance(int plan);
    ResponseEntity<?> saveInsurance(Insurance insurance);
    ResponseEntity<?> getInsurancePlan(int plan);
}