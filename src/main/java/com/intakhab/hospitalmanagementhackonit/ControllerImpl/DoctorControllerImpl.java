package com.intakhab.hospitalmanagementhackonit.ControllerImpl;

import com.intakhab.hospitalmanagementhackonit.Controller.DoctorController;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Repository.AppointmentRepo;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import com.intakhab.hospitalmanagementhackonit.Service.SecurityService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorControllerImpl implements DoctorController {
    private final DoctorService doctorService;
    private final SecurityService securityService;
    private final AppointmentRepo appointmentRepo;

    @GetMapping("/home")
    public ModelAndView home() {
        String viewName = "Doctor/home";
        Map<String, Object> model = new HashMap<>();
        model.put("currentuser", securityService.currentUser());
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/appointments")
    public ModelAndView appointments() {
        return new ModelAndView("Doctor/appointments");
    }

    @GetMapping("/doctors-appointments")
    public ResponseEntity<?> getDoctorsAppointments() {
        return ResponseEntity.ok(doctorService.getDoctorsAppointments());
    }

    @GetMapping("/videocall")
    public ModelAndView videocall(@RequestParam String roomID) {
        System.out.println("1");
        String viewName = "videocall";
        System.out.println("2");
        Map<String, Object> model = new HashMap<>();
        System.out.println("3");
        return new ModelAndView(viewName);
    }

    @GetMapping("/prescription")
    public ModelAndView prescription() {
        String viewName = "Doctor/prescription";
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView(viewName, model);
    }

    @GetMapping("/prescription-needed")
    public ResponseEntity<?> prescriptionNeeded() {
        return ResponseEntity.ok(doctorService.prescriptionNeeded());
    }

    @PostMapping("/save-prescription")
    public ResponseEntity<?> savePrescription(@RequestBody Appointment appointment) throws MessagingException {

        return ResponseEntity.ok(doctorService.savePrescription(appointment.getId(), appointment.getDrugsName()));
    }

    // In DoctorControllerImpl.java

    @PostMapping("/update-appointment")
    public ResponseEntity<?> updateAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(doctorService.updateAppointment(appointment.getId(), appointment.getDays()));
    }

    @GetMapping("/{id}/prescription")
    public ResponseEntity<byte[]> getPrescriptionPdf(@PathVariable UUID id) {
        Appointment appointment = appointmentRepo.findById(id).orElse(null);
        if (appointment == null || appointment.getPrescriptionPdf() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] pdfBytes = appointment.getPrescriptionPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=prescription.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }



}
