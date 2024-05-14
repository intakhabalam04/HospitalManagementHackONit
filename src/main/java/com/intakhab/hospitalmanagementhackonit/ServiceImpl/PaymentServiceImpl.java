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
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final AppointmentRepo appointmentRepo;
    private final DoctorService doctorService;
    private final SecurityService securityService;
    private final EmailService emailService;

    public PaymentServiceImpl(AppointmentRepo appointmentRepo, DoctorService doctorService, SecurityService securityService, EmailService emailService) {
        this.appointmentRepo = appointmentRepo;
        this.doctorService = doctorService;
        this.securityService = securityService;
        this.emailService = emailService;
    }

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

        StringBuilder message = new StringBuilder();
        message.append("Dear ").append(appointment.getPatientName()).append(",\n\n");
        message.append("Your appointment has been successfully booked. Here are the details:\n\n");
        message.append("Doctor: Dr. ").append(doctor.getName()).append("\n");
        message.append("Date: ").append(appointment.getAppointmentDate()).append("\n");
        message.append("Time: ").append(appointment.getAppointmentTime()).append("\n");
        message.append("Consultation Fee: ").append(doctor.getConsultancyFee()).append("\n\n");
        message.append("Please arrive 10 minutes before your scheduled time. If you need to cancel or reschedule, please contact our office as soon as possible.\n\n");
        message.append("Thank you for choosing our services.\n\n");
        message.append("Best Regards,\n");
        message.append("Your Hospital Management Team");

        email.setMessage(message.toString());

        emailService.sendEmail(email);

        Email email1 = new Email();
        email1.setReceiver(doctor.getEmail());
        email1.setSubject("New Appointment");
        StringBuilder message1 = new StringBuilder();
        message1.append("Dear Dr. ").append(doctor.getName()).append(",\n\n");
        message1.append("You have a new appointment scheduled.\n\n");
        message1.append("Patient Name: ").append(appointment.getPatientName()).append("\n");
        message1.append("Date: ").append(appointment.getAppointmentDate()).append("\n");
        message1.append("Time: ").append(appointment.getAppointmentTime()).append("\n");
        message1.append("Consultation Fee: ").append(doctor.getConsultancyFee()).append("\n\n");
        message1.append("Please be prepared for the appointment.\n\n");
        message1.append("You can join the video call using the following link:\n");
        message1.append("http://localhost:8080/doctor/videocall?roomID=4569\n\n");

        message1.append("Best Regards,\n");
        message1.append("Your Hospital Management Team");

        email1.setMessage(message1.toString());
        emailService.sendEmail(email1);

    }
}
