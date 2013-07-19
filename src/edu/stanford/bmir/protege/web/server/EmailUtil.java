package edu.stanford.bmir.protege.web.server;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.init.WebProtegeConfigurationException;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

public class EmailUtil {


    public static void sendEmail(String recipient, String subject, String message) {
        checkNotNull(recipient);
        checkNotNull(subject);
        checkNotNull(message);

        Optional<String> host = WebProtegeProperties.get().getEmailHostName();
        Optional<String> account = WebProtegeProperties.get().getEmailAccount();
        Optional<String> port = WebProtegeProperties.get().getEmailPort();
        if (!host.isPresent() || !account.isPresent() || !port.isPresent()) {
            throw new WebProtegeConfigurationException("Cannot send email.  Email has not been configured via webprotege.properties.");
        }

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        Properties props = new Properties();
        props.put("mail.smtp.host", host.get());
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", port.get());
        props.put("mail.smtp.socketFactory.port", port.get());
        props.put("mail.smtp.socketFactory.class", WebProtegeProperties.get().getSslFactory());
        props.put("mail.smtp.socketFactory.fallback", "false");


        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(WebProtegeProperties.get().getEmailAccount().get(), WebProtegeProperties.get().getEmailPassword().get());
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(account.get());
            msg.setFrom(addressFrom);

            InternetAddress[] addressTo = new InternetAddress[1];
            addressTo[0] = new InternetAddress(recipient);

            msg.setRecipients(Message.RecipientType.TO, addressTo);

            msg.setSubject(subject);
            msg.setText(message, "utf-8");
            msg.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
            msg.setHeader("Content-Transfer-Encoding", "quoted-printable");
            //msg.setContent(message, "text/plain");

            Transport.send(msg);
        }
        catch (MessagingException e) {
            throw new RuntimeException("There was an error sending email to " + " " + recipient + ". Message: " + e.getMessage(), e);
        }
    }

}