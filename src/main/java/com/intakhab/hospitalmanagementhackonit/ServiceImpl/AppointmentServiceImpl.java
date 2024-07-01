package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Enum.AppointmentStatus;
import com.intakhab.hospitalmanagementhackonit.Enum.PaymentStatus;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Model.Email;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.AppointmentRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.DoctorRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import com.intakhab.hospitalmanagementhackonit.Service.AppointmentService;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import com.intakhab.hospitalmanagementhackonit.Service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final DoctorService doctorService;
    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    private final UserRepo userRepo;
    private final SecurityService securityService;

    @Override
    public Appointment bookAppointment(Appointment appointment) {
        Appointment appointment1 = new Appointment();

        appointment1.setPatientName(appointment.getPatientName());
        appointment1.setAge(appointment.getAge());
        appointment1.setSymptoms(appointment.getSymptoms());
        appointment1.setGender(appointment.getGender());
        appointment1.setAppointmentDate(appointment.getAppointmentDate());
        appointment1.setAppointmentTime(appointment.getAppointmentTime());
        appointment1.setPaymentStatus(PaymentStatus.PENDING);
        appointment1.setAppointmentStatus(AppointmentStatus.PAYMENT_PENDING);

        Doctor doctor = doctorService.getDoctor(appointment.getDoctorid());
        appointment1.setDoctor(doctor);
        appointment1.setConsultationFee(doctor.getConsultancyFee());
        appointment1.setRoomID(doctor.getRoomID());

        User currentUser = securityService.currentUser();
        appointment1.setUser(currentUser);
        appointment1.setPrescriptionGiven(false);

        Appointment save = appointmentRepo.save(appointment1);

        List<Appointment> appointments = doctor.getAppointment();
        appointments.add(appointment1);
        doctor.setAppointment(appointments);
        doctorRepo.save(doctor);

        List<Appointment> userAppointments = currentUser.getAppointmentList();
        userAppointments.add(appointment1);
        currentUser.setAppointmentList(userAppointments);
        userRepo.save(currentUser);

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

//        emailService.sendEmail(email);

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
        message1.append("Best Regards,\n");
        message1.append("Your Hospital Management Team");

        email1.setMessage(message1.toString());
//        emailService.sendEmail(email1);
        return save;
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = securityService.currentUser().getAppointmentList();
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void uploadPatientReport(MultipartFile file, UUID appointmentId) {
        try {
            Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found"));

            byte[] prescriptionPdf = appointment.getPrescriptionPdf();
            byte[] reportPdf = file.getBytes();

            if (prescriptionPdf != null) {
                byte[] mergedPdf = mergeReport(prescriptionPdf, reportPdf);
                appointment.setPrescriptionPdf(mergedPdf);
            } else {
                appointment.setPrescriptionPdf(reportPdf);
            }

            appointmentRepo.save(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading report");
        }
    }

    private byte[] mergeReport(byte[] existingPdf, byte[] newPdf) {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationStream(new ByteArrayOutputStream());

        pdfMerger.addSource(new ByteArrayInputStream(existingPdf));
        pdfMerger.addSource(new ByteArrayInputStream(newPdf));

        ByteArrayOutputStream mergedPdfStream = new ByteArrayOutputStream();
        pdfMerger.setDestinationStream(mergedPdfStream);
        try {
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return mergedPdfStream.toByteArray();
    }

    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setPatientName(appointment.getPatientName());
        appointmentDto.setAge(appointment.getAge());
        appointmentDto.setSymptoms(appointment.getSymptoms());
        appointmentDto.setGender(appointment.getGender());
        appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDto.setDoctorId(appointment.getDoctor().getId());
        appointmentDto.setUserId(appointment.getUser().getId());
        appointmentDto.setDoctorName(appointment.getDoctor().getName());
        appointmentDto.setConsultationFee(appointment.getConsultationFee());
        appointmentDto.setPaymentStatus(appointment.getPaymentStatus().toString());
        appointmentDto.setAppointmentStatus(appointment.getAppointmentStatus().toString());
        appointmentDto.setRoomID(appointment.getRoomID());
        appointmentDto.setPrescriptionGiven(appointment.isPrescriptionGiven());
        return appointmentDto;
    }
}



