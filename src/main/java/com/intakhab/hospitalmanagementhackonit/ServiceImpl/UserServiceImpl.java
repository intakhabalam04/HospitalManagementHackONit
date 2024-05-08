package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Enum.AppointmentStatus;
import com.intakhab.hospitalmanagementhackonit.Model.Appointment;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.AppointmentRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import com.intakhab.hospitalmanagementhackonit.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final AppointmentRepo appointmentRepo;

    public UserServiceImpl(UserRepo userRepo, AppointmentRepo appointmentRepo) {
        this.userRepo = userRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    public User findByMobile(String phoneNumber) {
        return userRepo.findByMobile(phoneNumber);
    }

    @Override
    public User findByEmail(String emailId) {
        return userRepo.findByEmail(emailId);
    }

    @Override
    public boolean updateAppointmentStatus(String appointmentId, String roomID) {
        UUID uuid = UUID.fromString(appointmentId);
        Optional<Appointment> optionalAppointment = appointmentRepo.findById(uuid);
        if (optionalAppointment.isEmpty()) {
            return false;
        }
        Appointment appointment = optionalAppointment.get();
        if(!appointment.getDoctor().getRoomID().equals(roomID)){
            return false;
        }
        if (appointment.getAppointmentStatus().toString().equals(AppointmentStatus.PENDING.toString())) {
//            appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
//            appointmentRepo.save(appointment);
            return true;
        }
        return false;
    }

    @Override
    public User findByUserName(String username) {
        return userRepo.findByUsername(username);
    }

}
