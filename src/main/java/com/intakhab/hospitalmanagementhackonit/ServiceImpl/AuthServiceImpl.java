package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Enum.UserAction;
import com.intakhab.hospitalmanagementhackonit.Enum.UserRole;
import com.intakhab.hospitalmanagementhackonit.Model.Email;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import com.intakhab.hospitalmanagementhackonit.Service.AuthService;
import com.intakhab.hospitalmanagementhackonit.Service.EmailService;
import com.intakhab.hospitalmanagementhackonit.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final UserService userService;

    @Value("${spring.mail.username}")
    private String sender;
    @Value("${website.domain.name}")
    private String websiteName;
    @Value("${token.expiry.time}")
    private long tokenExpiryTime;
    @Value("${server.port}")
    private int serverPort;
    @Value("${url}")
    private String url;

    @Override
    public boolean registerNewUser(User newRegisterdUser) {
        try {
            User user = new User();
            user.setName(newRegisterdUser.getName());
            user.setUsername(newRegisterdUser.getUsername());
            user.setPassword(passwordEncoder.encode(newRegisterdUser.getPassword()));
            user.setRole(UserRole.PATIENT);
            user.setAction(UserAction.APPROVED);
            user.setEmail(newRegisterdUser.getEmail());
            user.setMobile(newRegisterdUser.getMobile());
            user.setRegistrationDate(LocalDate.now());
            user.setGender(newRegisterdUser.getGender());

            userRepo.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean sendResetPasswordMail(String emailUserPhone) {
        User user = userRepo.findByEmailOrMobileOrUsername(emailUserPhone, emailUserPhone, emailUserPhone);
        if (user != null) {
            UUID uuid = UUID.randomUUID();
            String token = uuid.toString().replace("-", "");
            String subject = "Here's the link to reset your password";

            String contactUs = "mailto:"+sender;
            Map<String, Object> model = new HashMap<>();
            model.put("userName", user.getName());  // Replace with actual user name
            model.put("resetLink", url+"/reset_password?token=" + token);
            model.put("currentYear", LocalDateTime.now().getYear());
            model.put("email",contactUs);
            model.put("websiteName",websiteName);

            Email email = new Email();
            email.setSender(sender);
            email.setReceiver(user.getEmail());
            email.setSubject(subject);
            email.setModel(model);
            email.setTemplateName("reset_password.ftl");

            user.setToken(token);
            user.setTokenExpiryTime((System.currentTimeMillis() + tokenExpiryTime));
            userRepo.save(user);
            return emailService.sendEmail(email);
        }
        return false;
    }
    @Override
    public String generateResetLink(String token) {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (Exception e) {
        }
        assert localHost != null;
        String prodLink = "https://hospitalmanagementhackonit-production.up.railway.app/reset_password?token=" + token;
        String devLink = "http://localhost:8080/reset_password?token=" + token;

        String msg = "<html>" + "<body>" + "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>" +
                "<p>Click the link below to change your password:</p>" +
                "<p><a href=" + prodLink + ">Change my password (Production)</a></p>" +
                "<p><a href=" + devLink + ">Change my password (Development)</a></p>" + "<br>" +
                "<p>Ignore this email if you do remember your password, " + "or you have not made the request.</p>" + "</body>" + "</html>";
        return msg;
    }
    @Override
    public User findByToken(String token) {
        return userRepo.findByToken(token);
    }
    @Override
    public boolean validateUserToken(String token) {
        User user = userRepo.findByToken(token);
        return user != null && user.getTokenExpiryTime() > System.currentTimeMillis();
    }
    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString().replace("-", "");
        user.setToken(token);
        userRepo.save(user);
    }
    @Override
    public boolean checkAvailabilityByMobile(String phoneNumber) {
        return userService.findByMobile(phoneNumber) != null;
    }
    @Override
    public boolean checkAvailabilityByUsername(String username) {
        return userService.findByUserName(username) != null;
    }
    @Override
    public boolean checkAvailabilityByEmail(String emailId) {
        return userService.findByEmail(emailId) != null;
    }
}
