package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Dto.DoctorDto;
import com.intakhab.hospitalmanagementhackonit.Enum.PaymentStatus;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.ChatBot;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Model.MedicineSuggestion;
import com.intakhab.hospitalmanagementhackonit.Repository.AppointmentRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.DoctorRepo;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepo doctorRepo;
    private static final String FLASK_SERVER_URL = "http://localhost:5001";
    private final AppointmentRepo appointmentRepo;

    public DoctorServiceImpl(DoctorRepo doctorRepo, AppointmentRepo appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public Doctor getDoctor(UUID id) {
        return doctorRepo.findById(id).orElse(null);
    }

    @Override
    public List<DoctorDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getDoctorsAppointments() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream()
                .flatMap(doctor -> doctor.getAppointment().stream().filter(appointment->appointment.getPaymentStatus().toString().equals(PaymentStatus.COMPLETED.toString()))
                        .map(appointment -> convertToDto(appointment, doctor)))
                .collect(Collectors.toList());
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
            return new MedicineSuggestion(medicine,response.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getTodayAppointmentsNo() {
        return (int) appointmentRepo.findAll().stream()
                .filter(appointment -> appointment.getAppointmentDate().equals(LocalDate.now()))
                .filter(appointment -> appointment.getPaymentStatus().toString().equals(PaymentStatus.COMPLETED.toString()))
                .count();
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
