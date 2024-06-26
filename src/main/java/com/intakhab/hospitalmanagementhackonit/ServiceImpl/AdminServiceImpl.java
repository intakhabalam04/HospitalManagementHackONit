package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Dto.AppointmentDto;
import com.intakhab.hospitalmanagementhackonit.Dto.DoctorDto;
import com.intakhab.hospitalmanagementhackonit.Enum.PaymentStatus;
import com.intakhab.hospitalmanagementhackonit.Enum.UserAction;
import com.intakhab.hospitalmanagementhackonit.Enum.UserRole;
import com.intakhab.hospitalmanagementhackonit.Model.BloodDonation;
import com.intakhab.hospitalmanagementhackonit.Model.Doctor;
import com.intakhab.hospitalmanagementhackonit.Model.OrganDonation;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.*;
import com.intakhab.hospitalmanagementhackonit.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final DoctorRepo doctorRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final AppointmentRepo appointmentRepo;
    private final BloodDonationRepo bloodDonationRepo;
    private final OrganDonationRepo organDonationRepo;



    @Override
    public boolean addDoctor(Doctor doctor) {
        System.out.println("Doctor: " + doctor.toString());
        try {
            Doctor newDoctor = new Doctor();
            newDoctor.setName(doctor.getName());
            newDoctor.setEmail(doctor.getEmail());
            newDoctor.setSpecialization(doctor.getSpecialization());
            newDoctor.setConsultancyFee(doctor.getConsultancyFee());
            newDoctor.setMobile(doctor.getMobile());
            newDoctor.setRoomID("4569");

            User user = new User();
            user.setName(doctor.getName());
            user.setEmail(doctor.getEmail());
            user.setMobile(doctor.getMobile());
            user.setUsername(doctor.getEmail());
            user.setPassword(passwordEncoder.encode("1234"));
            user.setRole(UserRole.DOCTOR);
            user.setAction(UserAction.APPROVED);
            user.setRegistrationDate(java.time.LocalDate.now());
            newDoctor.setUser(user);

            userRepo.save(user);
            doctorRepo.save(newDoctor);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<DoctorDto> getDoctors() {
        try {
            return doctorRepo.findAll().stream().map(doctor -> {
                DoctorDto doctorDto = new DoctorDto();
                doctorDto.setId(doctor.getId());
                doctorDto.setName(doctor.getName());
                doctorDto.setEmail(doctor.getEmail());
                doctorDto.setSpecialization(doctor.getSpecialization());
                doctorDto.setConsultancyFee(doctor.getConsultancyFee());
                doctorDto.setMobile(doctor.getMobile());
                return doctorDto;
            }).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteDoctor(UUID id) {
        try {
            System.out.println("Doctor Id: " + id);
            doctorRepo.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDoctor(Doctor existingDoctor, Doctor doctor) {
        try {
            existingDoctor.setName(doctor.getName());
            existingDoctor.setEmail(doctor.getEmail());
            existingDoctor.setSpecialization(doctor.getSpecialization());
            existingDoctor.setConsultancyFee(doctor.getConsultancyFee());
            existingDoctor.setMobile(doctor.getMobile());

            User user = existingDoctor.getUser();
            user.setName(doctor.getName());
            user.setEmail(doctor.getEmail());
            user.setMobile(doctor.getMobile());
            user.setUsername(doctor.getEmail());

            userRepo.save(user);
            doctorRepo.save(existingDoctor);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<AppointmentDto> getAppointments() {
        try {
            return appointmentRepo.findAll().stream()
                    .filter(appointment -> appointment.getPaymentStatus().equals(PaymentStatus.COMPLETED))
                    .map(appointment -> {
                        AppointmentDto appointmentDto = new AppointmentDto();
                        appointmentDto.setId(appointment.getId());
                        appointmentDto.setPatientName(appointment.getPatientName());
                        appointmentDto.setAge(appointment.getAge());
                        appointmentDto.setSymptoms(appointment.getSymptoms());
                        appointmentDto.setGender(appointment.getGender());
                        appointmentDto.setConsultationFee(appointment.getConsultationFee());
                        appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
                        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
                        appointmentDto.setDoctorId(appointment.getDoctor().getId());
                        appointmentDto.setUserId(appointment.getUser().getId());
                        appointmentDto.setDoctorName(appointment.getDoctor().getName());
                        appointmentDto.setEmail(appointment.getDoctor().getEmail());
                        appointmentDto.setMobile(appointment.getDoctor().getMobile());
                        return appointmentDto;
                    }).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BloodDonation> getBloodDonations() {
        return bloodDonationRepo.findAll();
    }

    @Override
    public List<OrganDonation> getOrganDonations() {
        return organDonationRepo.findAll();
    }
}
