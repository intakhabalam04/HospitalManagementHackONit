package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.Email;
import com.intakhab.hospitalmanagementhackonit.Service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

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

            String fromEmail = "teaminnovate.api@gmail.com";
            String fromName = "Jansevak";
            mimeMessageHelper.setFrom(fromEmail, fromName);

            javaMailSender.send(mimeMessage);

            return true;

        } catch (Exception e) {
            System.out.println("Error in sending email: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void sendEmailWithAttachment(Email email, byte[] attachmentPath) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email.getReceiver());
            helper.setSubject(email.getSubject());

            String fromEmail = "teaminnovate.api@gmail.com";
            String fromName = "Jansevak";
            helper.setFrom(fromEmail, fromName);


            Template template = configuration.getTemplate(email.getTemplateName());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, email.getModel());
            helper.setText(html, true);

            helper.addAttachment("prescription.pdf", new ByteArrayResource(attachmentPath));

            javaMailSender.send(message);
        }catch (Exception e){
            System.out.println("Error in sending email with attachment: " + e.getMessage());
        }

    }
}
