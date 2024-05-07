package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Dto.DoctorDto;
import com.intakhab.hospitalmanagementhackonit.Enum.PaymentStatus;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Repository.DoctorRepo;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepo doctorRepo;

    public DoctorServiceImpl(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
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
