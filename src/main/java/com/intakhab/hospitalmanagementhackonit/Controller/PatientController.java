package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.ChatBot;
import com.intakhab.hospitalmanagementhackonit.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    private final ChatBotService chatBotService;
    private final SecurityService securityService;

    public PatientController(UserService userService, DoctorService doctorService, AppointmentService appointmentService, ChatBotService chatBotService, SecurityService securityService) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.chatBotService = chatBotService;
        this.securityService = securityService;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        String viewName = "Patient/home";
        Map<String, Object> model = new HashMap<>();
        model.put("currentuser", securityService.currentUser());
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/book-appointment")
    public ModelAndView bookAppointment() {
        return new ModelAndView("Patient/book-appointment");
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<?> bookAppointment1(@RequestBody Appointment appointment) {
        try {
            Appointment appointment1 = appointmentService.bookAppointment(appointment);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("appointmentId", appointment1.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
        String viewName = "Patient/chatbot";
        Map<String, Object> model = new HashMap<>();
        model.put("currentuser", securityService.currentUser());
        return new ModelAndView(viewName, model);
    }

    @PostMapping("/chatbot")
    public ResponseEntity<?> chatbot1(@RequestBody ChatBot chatBot) {
        try {
            return ResponseEntity.ok().body(chatBotService.getResponse(chatBot.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/videocall")
    public ModelAndView videoCall(@RequestParam String roomID, @RequestParam String appointmentid) {
        boolean appointmentStatus = userService.updateAppointmentStatus(appointmentid,roomID);
        System.out.println(appointmentStatus);
        if (!appointmentStatus) {
            return new ModelAndView("redirect:/patient/appointment-history");
        }
        Map<String, Object> model = new HashMap<>();
        model.put("pName", securityService.currentUser().getName());
        return new ModelAndView("videocall", model);
    }
}
