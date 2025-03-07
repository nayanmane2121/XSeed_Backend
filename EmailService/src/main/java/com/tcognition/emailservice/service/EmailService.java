package com.tcognition.emailservice.service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tcognition.emailservice.business.EmailBusiness;
import com.tcognition.emailservice.dto.EmailRequestDTO;
import com.tcognition.emailservice.utils.ErrorMessagesConstants;
import com.tcognition.emailservice.utils.MessageConstants;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailBusiness emailBusiness;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email without attachments.
     */
    public String sendEmail(EmailRequestDTO request) {
        logger.info("Inside sendEmail()");
        try {
            String htmlContent = emailBusiness.createContext(request);
            if (htmlContent == null) {
                return ErrorMessagesConstants.ERROR_EMAIL_SENDING;
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            prepareMimeMessage(helper, request, htmlContent);

            logger.info("Sending email...");
            mailSender.send(mimeMessage);
            logger.info("Email sent successfully.");

            return MessageConstants.MSG_EMAIL_SENT_SUCCESSFULLY;
        } catch (MessagingException e) {
            logger.error("Error sending email: {}", e.getMessage(), e);
            return ErrorMessagesConstants.ERROR_EMAIL_SENDING;
        }
    }

    /**
     * Sends an email with attachments.
     */
    public String sendEmailWithAttachments(EmailRequestDTO request) {
        logger.info("Inside sendEmailWithAttachments()");
        try {
            String htmlContent = emailBusiness.createContext(request);
            if (htmlContent == null) {
                return ErrorMessagesConstants.ERROR_EMAIL_SENDING;
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            prepareMimeMessage(helper, request, htmlContent);
            attachFilesToEmail(helper, request);

            logger.info("Sending email with attachments...");
            mailSender.send(mimeMessage);
            logger.info("Email with attachments sent successfully.");

            return MessageConstants.MSG_EMAIL_SENT_SUCCESSFULLY;
        } catch (MessagingException e) {
            logger.error("Error sending email with attachments: {}", e.getMessage(), e);
            return ErrorMessagesConstants.ERROR_EMAIL_SENDING;
        }
    }

    /**
     * Prepares a MimeMessage with HTML content.
     */
    private void prepareMimeMessage(MimeMessageHelper helper, EmailRequestDTO request, String htmlContent) throws MessagingException {
        helper.setTo(request.getTo());

        if (request.getCc() != null && request.getCc().length > 0) {
            helper.setCc(request.getCc());
        }

        helper.setSubject(request.getSubject());
        helper.setText(htmlContent, true); // HTML content
    }

    /**
     * Attaches files to the email.
     */
    private void attachFilesToEmail(MimeMessageHelper helper, EmailRequestDTO request) throws MessagingException {
        Map<String, String> data = request.getData();
        if (data != null && data.containsKey("filePaths")) {
            List<String> filePaths = Arrays.asList(data.get("filePaths").split(","));
            for (String filePath : filePaths) {
                File file = new File(filePath.trim());
                if (file.exists() && file.isFile()) {
                    helper.addAttachment(file.getName(), new FileSystemResource(file));
                    logger.info("Attached file: {}", file.getAbsolutePath());
                } else {
                    logger.warn("File not found: {}", filePath);
                }
            }
        }
    }
}
