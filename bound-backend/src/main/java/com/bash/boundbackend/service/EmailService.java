package com.bash.boundbackend.service;

import com.bash.boundbackend.common.constants.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private JavaMailSender javaMailSender;
    private SpringTemplateEngine templateEngine;


    @Async
    public void sendUserEmail(
            String to,
            String username,
            EmailTemplateName emailTemplateName,
            String connectionUrl,
            String activationCode,
            String subject
    ) throws MessagingException {

        String templateName;
        if (emailTemplateName == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplateName.name();
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()


        );
        // Email Template parameters
        Map<String,Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("connectionUrl", connectionUrl);
        properties.put("activationCode", activationCode);

        //Email context/subject/body TEMPLATE
        Context context = new Context();
        context.setVariables(properties);

        mimeMessageHelper.setFrom("bash@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        mimeMessageHelper.setText(template, true);
        javaMailSender.send(mimeMessage);


    }


}
