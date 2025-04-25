package dev.aman.jobportalnotificationservice.Consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aman.jobportalnotificationservice.DTO.SendEmailDTO;
import dev.aman.jobportalnotificationservice.Util.EmailUtil;
import org.springframework.kafka.annotation.KafkaListener;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class SendEmailConsumer {
    private final ObjectMapper objectMapper;
    public SendEmailConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @KafkaListener(topics = "WelcomeEmail", groupId = "emailService")
    public void handleSendEmail(String message) {
        SendEmailDTO sendEmailDTO;
        try {
            //Converting our string message into DTO
            sendEmailDTO = objectMapper.readValue(message,
                    SendEmailDTO.class);
            String to = sendEmailDTO.getTo();
            String subject = sendEmailDTO.getSubject();
            String body = sendEmailDTO.getBody();

            final String fromEmail = "myemailid@gmail.com"; //requires valid gmail id
            final String password = "mypassword"; // correct password for gmail id
            final String toEmail = "myemail@yahoo.com"; // can be any email id

            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("codewithaman20@gmail.com", "mccmygvojtifzsol");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, to, subject, body);
    } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
