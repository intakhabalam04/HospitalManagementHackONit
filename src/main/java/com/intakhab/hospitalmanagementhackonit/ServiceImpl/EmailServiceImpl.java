package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.Email;
import com.intakhab.hospitalmanagementhackonit.Service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final Configuration configuration;


    @Override
    public boolean sendEmail(Email email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Prepare the email content using FreeMarker template
            Template template = configuration.getTemplate(email.getTemplateName());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, email.getModel());

            mimeMessageHelper.setTo(email.getReceiver());
            mimeMessageHelper.setSubject(email.getSubject());
            mimeMessageHelper.setText(html, true);

            javaMailSender.send(mimeMessage);

            return true;

        } catch (Exception e) {
            System.out.println("Error in sending email: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void sendEmailWithAttachment(Email email, String attachmentPath) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email.getReceiver());
        helper.setSubject(email.getSubject());
        helper.setText("Prescription uploaded by doctor. Please find the attachment.");

        FileSystemResource file = new FileSystemResource(new File(attachmentPath));
        helper.addAttachment("prescription.pdf", file);

        javaMailSender.send(message);

    }
}
