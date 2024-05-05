package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.AppointmentRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.DoctorRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import com.intakhab.hospitalmanagementhackonit.Service.AppointmentService;
import com.intakhab.hospitalmanagementhackonit.Service.DoctorService;
import com.intakhab.hospitalmanagementhackonit.Service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final DoctorService doctorService;
    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;

    private final UserRepo userRepo;
    private final SecurityService securityService;

    public AppointmentServiceImpl(DoctorService doctorService, AppointmentRepo appointmentRepo, DoctorRepo doctorRepo, UserRepo userRepo, SecurityService securityService) {
        this.doctorService = doctorService;
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
        this.securityService = securityService;
    }

    @Override
    public void bookAppointment(Appointment appointment) {
        Appointment appointment1 = new Appointment();

        appointment1.setPatientName(appointment.getPatientName());
        appointment1.setAge(appointment.getAge());
        appointment1.setSymptoms(appointment.getSymptoms());
        appointment1.setGender(appointment.getGender());
        appointment1.setAppointmentDate(appointment.getAppointmentDate());
        appointment1.setAppointmentTime(appointment.getAppointmentTime());

        Doctor doctor = doctorService.getDoctor(appointment.getDoctorid());
        appointment1.setDoctor(doctor);
        appointment1.setConsultationFee(doctor.getConsultancyFee());

        User currentUser = securityService.currentUser();
        appointment1.setUser(currentUser);

        appointmentRepo.save(appointment1);

        List<Appointment> appointments = doctor.getAppointment();
        appointments.add(appointment1);
        doctor.setAppointment(appointments);
        doctorRepo.save(doctor);

        List<Appointment> userAppointments = currentUser.getAppointmentList();
        userAppointments.add(appointment1);
        currentUser.setAppointmentList(userAppointments);
        userRepo.save(currentUser);


    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = securityService.currentUser().getAppointmentList();
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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
        return appointmentDto;
    }
}
