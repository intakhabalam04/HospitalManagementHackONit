package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Model.*;

public interface UserService {
    User findByUserName(String username);

    User findByMobile(String phoneNumber);

    User findByEmail(String emailId);

    boolean updateAppointmentStatus(String appointmentid, String roomID);

    int getPatientTillDate();

    OrganDonation saveOrganDonation(OrganDonation organDonation);

    BloodDonation saveBloodDonation(BloodDonation bloodDonation);

    ChatBotDb getPatientData();
}
