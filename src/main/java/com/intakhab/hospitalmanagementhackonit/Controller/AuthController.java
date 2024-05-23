package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.Contact;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public interface AuthController {
    ModelAndView getRegisterPage();
    ModelAndView goToHomPage();
    ModelAndView getLoginPage();
    ModelAndView registerUser(User user);
    boolean submitForgotPage(String emailUserPhone);
    ModelAndView getResetPage(String token);
    ModelAndView submitResetPage(User user);
    boolean checkAvailability(String userId, String type);
    ModelAndView getHomePage();
    ModelAndView getAboutPage();
    ModelAndView getContactPage();
    ModelAndView getDoctorPage();
    ResponseEntity<?> sendMessage(Contact contact);
}