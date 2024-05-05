package com.intakhab.hospitalmanagementhackonit.Controller;

import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Value("${loginView}")
    private String loginView;

    @Value("${registerView}")
    private String registerView;

    @Value("${resetFormView}")
    private String resetFormView;

    @Value("${invalidView}")
    private String invalidView;

    @Value("${successView}")
    private String successView;

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        String viewName = registerView;
        Map<String,Object> model = new HashMap<>();
        model.put("NewUser", new User());
        return new ModelAndView(viewName,model);
    }

    @GetMapping("login")
    public ModelAndView getLoginPage() {
        String viewName = loginView;
        Map<String,Object> model = new HashMap<>();
        model.put("LoginUser", new User());
        model.put("ForgotUser", new User());

        return new ModelAndView(viewName,model);
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute("NewUser") User user) {
        Map<String ,Object> model = new HashMap<>();
        if (!authService.registerNewUser(user)) {
            return new ModelAndView(registerView, model);
        }
        model.put("success", "Your registration is successfully completed");
        return new ModelAndView(registerView, model);
    }

    @GetMapping("/forgot")
    @ResponseBody
    public boolean submitForgotPage(@RequestParam("userId") String emailUserPhone) {
        return authService.sendResetPasswordMail(emailUserPhone);
    }

    @GetMapping("/reset_password")
    public ModelAndView getResetPage(@RequestParam String token) {

        User user = authService.findByToken(token);
        Map<String, Object> model = new HashMap<>();

        if (user == null || !authService.validateUserToken(token)) {
            return new ModelAndView(invalidView);
        } else {
            model.put("forgot", user);
            return new ModelAndView(resetFormView, model);
        }
    }

    @PostMapping("/reset_password")
    public ModelAndView submitResetPage(@ModelAttribute("forgot") User user) {
        if (authService.validateUserToken(user.getToken())) {
            authService.updatePassword(authService.findByToken(user.getToken()), user.getPassword());
            return new ModelAndView(successView);
        } else {
            return new ModelAndView(invalidView);
        }
    }

    @GetMapping("checkAvailability")
    @ResponseBody
    public boolean checkAvailability(@RequestParam("userId") String userId, @RequestParam("type") String type) {
        return switch (type) {
            case "username" -> authService.checkAvailabilityByUsername(userId);
            case "email" -> authService.checkAvailabilityByEmail(userId);
            case "mobile" -> authService.checkAvailabilityByMobile(userId);
            default -> false;
        };
    }
}
