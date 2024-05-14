package com.intakhab.hospitalmanagementhackonit.Service;

import com.intakhab.hospitalmanagementhackonit.Model.Email;
import jakarta.mail.MessagingException;

public interface EmailService {

    boolean sendEmail(Email email);

    void sendEmailWithAttachment(Email email, String attachmentPath) throws MessagingException;
}
