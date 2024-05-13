package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Enum.AppointmentStatus;
import com.intakhab.hospitalmanagementhackonit.Model.*;
import com.intakhab.hospitalmanagementhackonit.Repository.AppointmentRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.BloodDonationRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.OrganDonationRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import com.intakhab.hospitalmanagementhackonit.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final AppointmentRepo appointmentRepo;
    private final OrganDonationRepo organDonationRepo;
    private final BloodDonationRepo bloodDonationRepo;

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
        if (!appointment.getDoctor().getRoomID().equals(roomID)) {
            return false;
        }
        if (appointment.getAppointmentStatus().toString().equals(AppointmentStatus.PENDING.toString())) {
            appointment.setAppointmentStatus(AppointmentStatus.COMPLETED);
            appointmentRepo.save(appointment);
            return true;
        }
        return false;
    }

    @Override
    public int getPatientTillDate() {
        return (int) userRepo.count();
    }

    @Override
    public OrganDonation saveOrganDonation(OrganDonation organDonation) {

        try {
            organDonation.setSignatureData("");
            return organDonationRepo.save(organDonation);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public BloodDonation saveBloodDonation(BloodDonation bloodDonation) {
        try{
            return bloodDonationRepo.save(bloodDonation);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public User findByUserName(String username) {
        return userRepo.findByUsername(username);
    }

}
