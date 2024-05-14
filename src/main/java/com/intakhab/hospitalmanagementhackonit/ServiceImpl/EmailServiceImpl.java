package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.Email;
import com.intakhab.hospitalmanagementhackonit.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    // Method to send an email using JavaMailSender
    @Override
    public boolean sendEmail(Email email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setTo(email.getReceiver());
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(email.getMessage(), true);

            javaMailSender.send(mimeMessage);

            return true;

        } catch (Exception e) {
            System.out.println("Error in sending email" + e.getMessage());
        }
        return false;
    }

    @Override
    public void sendEmailWithAttachment(Email email, String attachmentPath) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

//        helper.setFrom(email.getSender());
        helper.setTo(email.getReceiver());
        helper.setSubject(email.getSubject());
        helper.setText(email.getMessage());

        // Add the attachment
        FileSystemResource file = new FileSystemResource(new File(attachmentPath));
        helper.addAttachment("prescription.pdf", file);

        javaMailSender.send(message);

    }
}
