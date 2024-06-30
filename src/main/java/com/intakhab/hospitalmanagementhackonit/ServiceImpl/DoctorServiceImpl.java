package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Dto.DoctorDto;
import com.intakhab.hospitalmanagementhackonit.Enum.AppointmentStatus;
import com.intakhab.hospitalmanagementhackonit.Enum.PaymentStatus;
import com.intakhab.hospitalmanagementhackonit.Model.*;
import com.intakhab.hospitalmanagementhackonit.Repository.AppointmentRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.DoctorRepo;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import com.intakhab.hospitalmanagementhackonit.Service.EmailService;
import com.intakhab.hospitalmanagementhackonit.Service.UserService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;
    private final EmailService emailService;
    private static final String FLASK_SERVER_URL = "http://localhost:5001";

    @Override
    public Doctor getDoctor(UUID id) {
        return doctorRepo.findById(id).orElse(null);
    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getDoctorsAppointments() {
        List<Doctor> doctors = doctorRepo.findAll();

        return doctors.stream().flatMap(doctor -> doctor.getAppointment().stream().filter(appointment -> appointment.getPaymentStatus().toString().equals(PaymentStatus.COMPLETED.toString())).map(appointment -> convertToDto(appointment, doctor))).collect(Collectors.toList());
    }

    @Override
    public DoctorDto getDoctorDto(UUID id) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) {
            return null;
        }
        return convertToDto(doctor);
    }

    @Override
    public MedicineSuggestion recommend(String medicine) {
        System.out.println(medicine);
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = FLASK_SERVER_URL + "/recommend";
            String requestPayLoad = "{\"medicine\": \"" + medicine + "\"}";
            ChatBotServiceImpl.ChatBotResponse response = restTemplate.postForObject(url, requestPayLoad, ChatBotServiceImpl.ChatBotResponse.class);
            assert response != null;
            System.out.println(response);
            return new MedicineSuggestion(medicine, response.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getTodayAppointmentsNo() {
        return (int) appointmentRepo.findAll().stream()
                .filter(appointment -> appointment.getAppointmentDate().equals(LocalDate.now()))
                .filter(appointment -> appointment.getPaymentStatus().toString().equals(PaymentStatus.COMPLETED.toString())).count();
    }

    @Override
    public List<AppointmentDto> prescriptionNeeded() {
        return appointmentRepo.findAll().stream().filter(appointment -> appointment.getPaymentStatus().toString().equals(PaymentStatus.COMPLETED.toString())).filter(appointment -> appointment.getAppointmentStatus().toString().equals(AppointmentStatus.COMPLETED.toString())).filter(appointment -> !appointment.isPrescriptionGiven()).map(appointment -> convertToDto(appointment, appointment.getDoctor())).collect(Collectors.toList());
    }

    @Override
    public Object savePrescription(UUID prescription, String drugsName) throws MessagingException {

        Appointment appointment = appointmentRepo.findById(prescription).orElse(null);
        assert appointment != null;
        appointment.setDrugsName(drugsName);
        appointment.setPrescriptionGiven(true);
        String pdfPath = "prescription.pdf";
        Email email = new Email();
        email.setSubject("Prescription for your appointment");
        email.setMessage(drugsName);
        email.setReceiver(appointment.getUser().getEmail());
        try {

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Add an image
//            Image image = Image.getInstance("C:\\Users\\Intakhab Alam\\Desktop\\HospitalManagementHackONit\\src\\main\\resources\\static\\images\\logo_main.png");
//            image.scaleAbsolute(100f, 100f);
//            document.add(image);

            // Add a heading
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD);
            Phrase heading = new Phrase("JANSEVAK", font);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, heading, 300, 800, 0);

            Font font1 = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
            Phrase heading1 = new Phrase("BRINGING THE FUTURE OF HEALTHCARE \n", font1);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, heading1, 225, 785, 0);


            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Phrase heading2 = new Phrase("teaminnovate.api@gmail.com\n", font2);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, heading2, 240, 770, 0);

            String patientName = "Patient Name : " + appointment.getPatientName();
            Font patient = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Phrase patient1 = new Phrase(patientName, patient);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, patient1, 150, 755, 0);

            String appAge = "Age : " + appointment.getAge();
            Font age = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Phrase age1 = new Phrase(appAge, age);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, age1, 150, 740, 0);

            String appGender = "Gender : " + appointment.getGender();
            Font gender = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Phrase gender1 = new Phrase(appGender, gender);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, gender1, 150, 725, 0);

            String doctorName = "Doctor Name : " + appointment.getDoctor().getName();
            Font d = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Phrase d1 = new Phrase(doctorName, d);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, d1, 350, 755, 0);

            PdfContentByte canvas = writer.getDirectContent();
            canvas.setLineWidth(3f);
            canvas.moveTo(40, 700);
            canvas.lineTo(550, 700);
            canvas.stroke();
//
            canvas.setLineWidth(1f);
            canvas.moveTo(300, 700);
            canvas.lineTo(300, 100);
            canvas.stroke();

            Font p = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Phrase p1 = new Phrase("PRESCRIPTION \n", p);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, p1, 130, 680, 0);

            System.out.println(drugsName);
            // Split drugsName by commas
            String[] details = drugsName.split(",");

            // Create a new Phrase
            Phrase prescriptionPhrase = new Phrase();

            // Add each detail to the Phrase on a new line
            Font prescriptionFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (String detail : details) {
                prescriptionPhrase.add(new Chunk(detail.trim() + "\n", prescriptionFont));
            }

            // Create a column and add prescriptionPhrase to the column
            ColumnText column = new ColumnText(writer.getDirectContent());
            column.setSimpleColumn(130, 660, 500, 36); // Adjust the rectangle as needed
            column.addElement(prescriptionPhrase);
            column.go();

            // Split drugsName by commas
            String[] details1 = appointment.getSymptoms().split(",");

            // Create a new Phrase
            Phrase prescriptionPhrase1 = new Phrase();

            // Add each detail to the Phrase on a new line
            Font prescriptionFont1 = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (String detail : details1) {
                prescriptionPhrase1.add(new Chunk(detail.trim() + "\n", prescriptionFont1));
            }

            // Create a column and add prescriptionPhrase to the column
            ColumnText column1 = new ColumnText(writer.getDirectContent());
            column.setSimpleColumn(350, 660, 500, 36);
            column.addElement(prescriptionPhrase1);
            column.go();

            Font s = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Phrase s1 = new Phrase("SYMPTOMS \n", s);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, s1, 350, 680, 0);


            document.close();
            byte[] pdfBytes = Files.readAllBytes(Paths.get(pdfPath));
            appointment.setPrescriptionPdf(pdfBytes);


        } catch (Exception e) {
            e.printStackTrace();
        }

        email.setAttachmentPath(pdfPath);
        emailService.sendEmailWithAttachment(email, pdfPath);


        return appointmentRepo.save(appointment);

    }

    private AppointmentDto convertToDto(Appointment appointment, Doctor doctor) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setPatientName(appointment.getPatientName());
        appointmentDto.setAge(appointment.getAge());
        appointmentDto.setSymptoms(appointment.getSymptoms());
        appointmentDto.setGender(appointment.getGender());
        appointmentDto.setConsultationFee(appointment.getConsultationFee());
        appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDto.setDoctorId(doctor.getId());
        appointmentDto.setUserId(appointment.getUser().getId());
        appointmentDto.setDoctorName(doctor.getName());
        appointmentDto.setEmail(doctor.getEmail());
        appointmentDto.setMobile(doctor.getMobile());
        return appointmentDto;
    }

    private DoctorDto convertToDto(Doctor doctor) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(doctor.getId());
        doctorDto.setName(doctor.getName());
        doctorDto.setEmail(doctor.getEmail());
        doctorDto.setSpecialization(doctor.getSpecialization());
        doctorDto.setMobile(doctor.getMobile());
        doctorDto.setConsultancyFee(doctor.getConsultancyFee());
        doctorDto.setUserId(doctor.getUser().getId());
        doctorDto.setAppointmentIds(doctor.getAppointment().stream().map(Appointment::getId).collect(Collectors.toList()));
        return doctorDto;
    }
}


