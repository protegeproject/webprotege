package edu.stanford.bmir.protege.web.server;

import java.security.Security;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.stanford.smi.protege.util.Log;

public class EmailUtil {

    public static void sendEmail(String recipient, String subject, String message, String from) {
        String smtpHostName = ApplicationProperties.getSmtpHostName();
        if (smtpHostName == null || smtpHostName.length() == 0) {
            Log.getLogger().warning("Failed to send email message to " + recipient + ". Email not configured on the server.");
            return;
        }

        if (recipient == null) {
            Log.getLogger().warning("Cannot send email with subject: " + subject + " Email address is null");
            return;
        }

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        Properties props = new Properties();
        props.put("mail.smtp.host", ApplicationProperties.getSmtpHostName());
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", ApplicationProperties.getSmtpPort());
        props.put("mail.smtp.socketFactory.port", ApplicationProperties.getSmtpPort());
        props.put("mail.smtp.socketFactory.class", ApplicationProperties.getSslFactory());
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(ApplicationProperties.getEmailAccount(), ApplicationProperties.getEmailPassword());
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);

            InternetAddress[] addressTo = new InternetAddress[1];
            addressTo[0] = new InternetAddress(recipient);

            msg.setRecipients(Message.RecipientType.TO, addressTo);

            msg.setSubject(subject);
            msg.setText(message, "utf-8");
            msg.setHeader("Content-Type","text/plain; charset=\"utf-8\"");
            msg.setHeader("Content-Transfer-Encoding", "quoted-printable");
            //msg.setContent(message, "text/plain");

            Transport.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException("There was an error sending email to " + " " + recipient + ". Message: " + e.getMessage() , e);
        }
    }

}