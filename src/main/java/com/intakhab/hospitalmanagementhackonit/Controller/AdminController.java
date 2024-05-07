package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Repository.DoctorRepo;
import com.intakhab.hospitalmanagementhackonit.Service.AdminService;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final DoctorService doctorService;

    public AdminController(AdminService adminService, DoctorService doctorService) {
        this.adminService = adminService;
        this.doctorService = doctorService;
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("Admin/home");
    }

    @GetMapping("/doctor-list")
    public ModelAndView doctorList() {
        return new ModelAndView("Admin/doctor-list");
    }

    @GetMapping("/doctors")
    public ResponseEntity<?> getDoctors() {
        return ResponseEntity.ok().body(adminService.getDoctors());
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable UUID id) {
        Doctor doctor = doctorService.getDoctor(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false}");
        } else {
            return ResponseEntity.ok().body(doctor);
        }
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor) {
        Doctor existingDoctor = doctorService.getDoctor(id);
        if (existingDoctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false}");
        } else {
            adminService.updateDoctor(existingDoctor, doctor);
            return ResponseEntity.ok().body("{\"success\": true}");
        }
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {
        try {
            adminService.deleteDoctor(id);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false}");
        }
    }

    @GetMapping("/patient-list")
    public ModelAndView patientList() {
        return new ModelAndView("Admin/patient-list");
    }

    @GetMapping("/appointment-details")
    public ModelAndView appointmentDetails() {
        return new ModelAndView("Admin/appointment-details");
    }

    @GetMapping("/add-doctor")
    public ModelAndView addDoctor() {
        return new ModelAndView("Admin/add-doctor");
    }

    @PostMapping("/add-doctor")
    public ResponseEntity<?> addDoctorPost(@RequestBody Doctor doctor) {
        try {
            adminService.addDoctor(doctor);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false}");
        }
    }


    @GetMapping("/messages")
    public ModelAndView messages() {
        return new ModelAndView("Admin/messages");
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments() {
        return ResponseEntity.ok().body(adminService.getAppointments());
    }

}
