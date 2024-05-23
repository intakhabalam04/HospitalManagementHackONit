package com.intakhab.hospitalmanagementhackonit.ControllerImpl;

import com.intakhab.hospitalmanagementhackonit.Controller.AdminController;
import com.intakhab.hospitalmanagementhackonit.Dto.DoctorDto;
import com.intakhab.hospitalmanagementhackonit.Model.BloodDonation;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Model.OrganDonation;
import com.intakhab.hospitalmanagementhackonit.Service.AdminService;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import com.intakhab.hospitalmanagementhackonit.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;
    private final DoctorService doctorService;
    private final UserService userService;

    @GetMapping("/home")
    public ModelAndView home() {
        String viewName = "Admin/home";
        Map<String, Object> model = new HashMap<>();
        model.put("doctorCount", doctorService.getAllDoctors().size());
//        model.put("appointmentCount", doctorService.getTodayAppointmentsNo());
        model.put("patientCount", userService.getPatientTillDate());

        return new ModelAndView(viewName, model);
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
        DoctorDto doctor = doctorService.getDoctorDto(id);
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


    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments() {
        return ResponseEntity.ok().body(adminService.getAppointments());
    }

    @GetMapping("/blood-donations")
    public ModelAndView bloodDonations() {
        return new ModelAndView("Admin/blood_donation_list");
    }

    @GetMapping("/organ-donations")
    public ModelAndView organDonations() {
        return new ModelAndView("Admin/organ-donation-list");
    }


    @GetMapping("/blood-donations-list")
    public ResponseEntity<?> getBloodDonations() {
        List<BloodDonation> bloodDonations = adminService.getBloodDonations();
        return ResponseEntity.ok().body(bloodDonations);
    }

    @GetMapping("/organ-donations-list")
    public ResponseEntity<?> getOrganDonations() {
        List<OrganDonation> organDonations = adminService.getOrganDonations();
        return ResponseEntity.ok().body(organDonations);
    }
}