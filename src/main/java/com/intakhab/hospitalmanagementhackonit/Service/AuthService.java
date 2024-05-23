package com.intakhab.hospitalmanagementhackonit.Service;


import com.intakhab.hospitalmanagementhackonit.Model.User;

public interface AuthService {
    boolean registerNewUser(User newRegisterdUser);
    boolean sendResetPasswordMail(String emailUserPhone);
    String generateResetLink(String token);
    User findByToken(String token);
    boolean validateUserToken(String token);
    void updatePassword(User byToken, String password);
    boolean checkAvailabilityByMobile(String userId);
    boolean checkAvailabilityByUsername(String userId);
    boolean checkAvailabilityByEmail(String userId);
}
