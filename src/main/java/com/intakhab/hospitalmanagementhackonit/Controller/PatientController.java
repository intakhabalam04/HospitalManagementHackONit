package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.ChatBot;
import com.intakhab.hospitalmanagementhackonit.Service.AppointmentService;
import com.intakhab.hospitalmanagementhackonit.Service.ChatBotService;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    private final ChatBotService chatBotService;

    public PatientController(DoctorService doctorService, AppointmentService appointmentService, ChatBotService chatBotService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.chatBotService = chatBotService;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("Patient/home");
    }

    @GetMapping("/book-appointment")
    public ModelAndView bookAppointment() {
        return new ModelAndView("Patient/book-appointment");
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<?> bookAppointment1(@RequestBody Appointment appointment) {
        try {
            appointmentService.bookAppointment(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/appointment-history")
    public ModelAndView appointmentHistory() {
        return new ModelAndView("Patient/appointment-history");
    }

    @GetMapping("/all-appointments")
    public ResponseEntity<?> allAppointments() {
        try {
            return ResponseEntity.ok(appointmentService.getAllAppointments());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> allDoctors() {
        try {
            System.out.println("All Doctors");
            return ResponseEntity.ok(doctorService.getAllDoctors());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/chatbot")
    public ModelAndView chatbot() {
        return new ModelAndView("Patient/chatbot");
    }

    @PostMapping("/chatbot")
    public ResponseEntity<?> chatbot1(@RequestBody ChatBot chatBot) {
        try {
            return ResponseEntity.ok().body(chatBotService.getResponse(chatBot.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
