package com.tcognition.emailservice.business;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.tcognition.emailservice.dto.EmailRequestDTO;
import com.tcognition.emailservice.utils.AppConstants;

@Service
public class EmailBusiness {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(EmailBusiness.class);

    private final Map<String, Function<EmailRequestDTO, String>> templateProcessors = Map.of(
        AppConstants.TEMPLATE_SCHEDULE_INTERVIEW, this::contextInterviewInvite,
        AppConstants.TEMPLATE_RESET_PASSWORD, this::contextResetPassword
    );

    public String createContext(EmailRequestDTO request) {
        if (request == null || request.getTemplateName() == null) {
            logger.error("Invalid email request or missing template name.");
            return null;
        }

        return templateProcessors.getOrDefault(request.getTemplateName(), req -> {
            logger.warn("No matching template found for: {}", req.getTemplateName());
            return null;
        }).apply(request);
    }

    private String contextResetPassword(EmailRequestDTO request) {
        Context context = populateContext(request);
        return templateEngine.process(request.getTemplateName(), context);
    }

    private String contextInterviewInvite(EmailRequestDTO request) {
        Context context = populateContext(request);

        Map<String, String> data = request.getData();
        if (data == null) {
            logger.error("Missing data for interview invite email.");
            return null;
        }

        context.setVariable("role", data.getOrDefault("role", "N/A"));
        context.setVariable("meetingLink", data.getOrDefault("meetingLink", "#"));

        try {
            String dateTimeString = data.get("dateTime");
            if (dateTimeString != null && !dateTimeString.isEmpty()) {
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' hh:mm a");
                context.setVariable("dateTime", dateTime.format(formatter));
            } else {
                context.setVariable("dateTime", "Date not provided");
            }
        } catch (Exception e) {
            logger.error("Error parsing dateTime for interview invite: {}", e.getMessage());
            context.setVariable("dateTime", "Invalid Date");
        }

        return templateEngine.process(request.getTemplateName(), context);
    }

    private Context populateContext(EmailRequestDTO request) {
        Context context = new Context();
        context.setVariable("message", request.getMessage());
        context.setVariable("name", request.getName());
        return context;
    }
}
