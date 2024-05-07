package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Enum.UserRole;
import com.intakhab.hospitalmanagementhackonit.Model.Contact;
import com.intakhab.hospitalmanagementhackonit.Model.Email;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.UserRepo;
import com.intakhab.hospitalmanagementhackonit.Service.ContactService;
import com.intakhab.hospitalmanagementhackonit.Service.EmailService;
import com.intakhab.hospitalmanagementhackonit.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ContactServiceImpl implements ContactService {

    private final UserRepo userRepo;
    private final EmailService emailService;

    public ContactServiceImpl(UserRepo userRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    @Override
    public void sendMessage(Contact contact) {
        List<User> adminUsers = userRepo.findAll().
                stream()
                .filter(
                        user -> user.getRole().equals(UserRole.ADMIN)).toList();
        if (!adminUsers.isEmpty()) {
            Random random = new Random();
            User randomAdmin = adminUsers.get(random.nextInt(adminUsers.size()));
            Email email = new Email();
            email.setSubject("New Message from " + contact.getName());
            email.setMessage(contact.getMessage());
            email.setReceiver(randomAdmin.getEmail());
            emailService.sendEmail(email);


        }
    }
}