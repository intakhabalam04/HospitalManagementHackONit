package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.*;
import com.intakhab.hospitalmanagementhackonit.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ChatBotService chatBotService;
    private final SecurityService securityService;

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
        if (!appointmentStatus) {
            return new ModelAndView("redirect:/patient/appointment-history");
        }
        Map<String, Object> model = new HashMap<>();
        model.put("pName", securityService.currentUser().getName());
        return new ModelAndView("videocall", model);
    }

    @PostMapping("/recommend")
    public ResponseEntity<?> recommend(@RequestBody MedicineSuggestion medicineSuggestion) {
        try {
            return ResponseEntity.ok().body(doctorService.recommend(medicineSuggestion.getMedicine()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/recommend")
    public ModelAndView recommend() {
        return new ModelAndView("Patient/medicine-recommend");
    }

    @GetMapping("/diseases")
    public ModelAndView diseaseDescription() {
        return new ModelAndView("Patient/disease_descriptions");
    }

    @GetMapping("/messages")
    public ModelAndView messages() {
        return new ModelAndView("Admin/messages");
    }

    @GetMapping("/mental-health")
    public ModelAndView getMentalHealthPage(){
        return new ModelAndView("Patient/Mental-Health/act");
    }

    @GetMapping("/donate-blood")
    public ModelAndView donateBloodPage(){
        String viewName = "Patient/blood_donation";
        Map<String, Object> model = new HashMap<>();
        model.put("bloodDonate", new BloodDonation());
        return new ModelAndView(viewName,model);
    }

    @PostMapping("/blood-donation")
    public ResponseEntity<?> registerBloodDonation(@RequestBody BloodDonation bloodDonation) {
        try{
            System.out.println(bloodDonation);
            BloodDonation savedBloodDonation = userService.saveBloodDonation(bloodDonation);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("bloodDonationId", savedBloodDonation.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/donate-organs")
    public ModelAndView organDonationPage(){
        String viewName = "Patient/organ_donation";
        Map<String, Object> model = new HashMap<>();
        model.put("bloodDonate", new BloodDonation());
        return new ModelAndView(viewName,model);
    }

    @PostMapping("/donate-organs")
    public ResponseEntity<?> donateOrgans(@RequestBody OrganDonation organDonation) {
        try {
            System.out.println(organDonation);
            OrganDonation savedOrganDonation = userService.saveOrganDonation(organDonation);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("organDonationId", savedOrganDonation.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }






}
