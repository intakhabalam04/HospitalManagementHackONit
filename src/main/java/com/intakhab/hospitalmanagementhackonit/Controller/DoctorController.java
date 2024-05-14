package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import com.intakhab.hospitalmanagementhackonit.Service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final SecurityService securityService;

    public DoctorController(DoctorService doctorService, SecurityService securityService) {
        this.doctorService = doctorService;
        this.securityService = securityService;
    }

    @GetMapping("/home")
    public ModelAndView home(){
        String viewName = "Doctor/home";
        Map<String, Object> model = new HashMap<>();
        model.put("currentuser", securityService.currentUser());
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/appointments")
    public ModelAndView appointments(){
        return new ModelAndView("Doctor/appointments");
    }

    @GetMapping("/doctors-appointments")
    public ResponseEntity<?> getDoctorsAppointments(){
        return ResponseEntity.ok(doctorService.getDoctorsAppointments());
    }

    @GetMapping("/videocall")
    public ModelAndView videocall(@RequestParam String roomID){
        System.out.println("1");
        String viewName = "videocall";
        System.out.println("2");
        Map<String, Object> model = new HashMap<>();
        System.out.println("3");
        return new ModelAndView(viewName);
    }

    @GetMapping("/prescription")
    public ModelAndView prescription(){
        String viewName = "Doctor/prescription";
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(viewName,model);
    }

    @GetMapping("/prescription-needed")
    public ResponseEntity<?> prescriptionNeeded(){
        return ResponseEntity.ok(doctorService.prescriptionNeeded());
    }

    @PostMapping("/save-prescription")
    public ResponseEntity<?> savePrescription(@RequestBody Appointment appointment){

        return ResponseEntity.ok(doctorService.savePrescription(appointment.getId(),appointment.getPrescription()));
    }

}
