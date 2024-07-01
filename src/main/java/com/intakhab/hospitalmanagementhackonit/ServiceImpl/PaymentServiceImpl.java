package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Enum.AppointmentStatus;
import com.intakhab.hospitalmanagementhackonit.Enum.PaymentStatus;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Model.Email;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.AppointmentRepo;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import com.intakhab.hospitalmanagementhackonit.Service.EmailService;
import com.intakhab.hospitalmanagementhackonit.Service.PaymentService;
import com.intakhab.hospitalmanagementhackonit.Service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final AppointmentRepo appointmentRepo;
    private final DoctorService doctorService;
    private final SecurityService securityService;
    private final EmailService emailService;

    @Override
    public void updatePaymentStatus(UUID patientId) {
        Appointment appointment = appointmentRepo.findById(patientId).orElseThrow();
        appointment.setPaymentStatus(PaymentStatus.COMPLETED);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING);
        appointment.setPrescriptionGiven(false);
        appointmentRepo.save(appointment);

        Doctor doctor = doctorService.getDoctor(appointment.getDoctor().getId());
        User currentUser = securityService.currentUser();


        Email email = new Email();
        email.setReceiver(currentUser.getEmail());
        email.setSubject("Appointment Confirmation");
        Map<String,Object> model = new HashMap<>();
        model.put("patientName", appointment.getPatientName());
        model.put("doctorName", doctor.getName());
        model.put("appointmentDate", appointment.getAppointmentDate());
        model.put("appointmentTime", appointment.getAppointmentTime());
        model.put("consultationFee", doctor.getConsultancyFee());
        model.put("currentYear", LocalDate.now().getYear());
        email.setModel(model);
        email.setTemplateName("appointment-confirmation-patient.ftl");

        emailService.sendEmail(email);

        email.setReceiver(doctor.getEmail());
        email.setSubject("New Appointment");
        email.setTemplateName("appointment-confirmation-doctor.ftl");

        emailService.sendEmail(email);

    }
}
