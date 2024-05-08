package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Model.User;

public interface UserService {
    User findByUserName(String username);

    User findByMobile(String phoneNumber);

    User findByEmail(String emailId);

    boolean updateAppointmentStatus(String appointmentid, String roomID);
}
