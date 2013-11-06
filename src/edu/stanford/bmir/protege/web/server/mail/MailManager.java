package edu.stanford.bmir.protege.web.server.mail;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/11/2013
 */
public class MailManager {

    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

    public static final String MAIL_SMTP_USER = "mail.smtp.user";

    public static final String MAIL_SMTP_PASSWORD = "mail.smtp.wp.password";

    public static final String MAIL_SMTP_FROM = "mail.smtp.from";

    public static final String MAIL_SMTP_FROM_PERSONALNAME = "mail.smtp.from.wp.personalName";

    public static final String UTF_8 = "utf-8";

    public static final String DEFAULT_FROM_VALUE = "no-reply@webprotege.stanford.edu";

    public static final String DEFAULT_FROM_PERSONALNAME = "WebProtégé";

    public static final WebProtegeLogger LOGGER = WebProtegeLoggerManager.get(MailManager.class);

    private final Properties properties;

    private final MessagingExceptionHandler messagingExceptionHandler;

    /**
     * Constructs a {@code MailManager} using the mail properties specified in the {@link Properties} object.  Note
     * modification of values in the {@link Properties} object will not modify mail settings after construction.
     * @param properties The mail properties.  Not {@code null}. These are the same properties as specified in the
     *                   Java mail spec.  Note that an additional property "mail.smtp.from.personalName" can be
     *                   used to specify a personal name in the from field of sent messages.  Also, if authentication
     *                   is required then the "mail.smtp.password" property should be set.
     * @throws NullPointerException if {@code properties} is {@code null}.
     */
    public MailManager(Properties properties) {
        this(checkNotNull(properties), new ConsoleMessagingExceptionHandler());
    }

    /**
     * Constructs a {@code MailManager} using the specified {@link Properties} object and the specified exception handler.
     * The {@link Properties} object should contain java mail properties e.g. mail.smtp.host etc.  See
     * <a href="https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html">https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html</a>
     * for more information.    Note
     * modification of values in the {@link Properties} object will not modify mail settings after construction.
     * @param properties The mail properties.  Not {@code null}. These are the same properties as specified in the
     *                   Java mail spec.  Note that an additional property "mail.smtp.from.personalName" can be
     *                   used to specify a personal name in the from field of sent messages.  Also, if authentication
     *                   is required then the "mail.smtp.password" property should be set.
     * @param messagingExceptionHandler An exception handler that handles {@link MessagingException}s.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public MailManager(Properties properties, MessagingExceptionHandler messagingExceptionHandler) {
        this.properties = new Properties(checkNotNull(properties));
        this.messagingExceptionHandler = checkNotNull(messagingExceptionHandler);
    }

    /**
     * Sends an email to the specified recipient.  The email will have the specified subject and specified content.
     * @param recipientEmailAddress The email address of the recipient.  Not {@code null}.
     * @param subject The subject of the email.  Not {@code null}.
     * @param text The content of the email.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public void sendMail(final String recipientEmailAddress, final String subject, final String text) {
        sendMail(recipientEmailAddress, subject, text, messagingExceptionHandler);
    }

    /**
     * Sends an email to the specified recipient.  The email will have the specified subject and specified content.
     * @param recipientEmailAddress The email address of the recipient.  Not {@code null}.
     * @param subject The subject of the email.  Not {@code null}.
     * @param text The content of the email.  Not {@code null}.
     * @param exceptionHandler An exception handler for handling {@link MessagingException}s.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public void sendMail(final String recipientEmailAddress, final String subject, final String text, MessagingExceptionHandler exceptionHandler) {
        try {
            checkNotNull(recipientEmailAddress);
            checkNotNull(subject);
            checkNotNull(text);
            final Session session = createMailSession();
            MimeMessage msg = new MimeMessage(session);
            Optional<String> fromAddress = getPropertyValue(MAIL_SMTP_FROM);
            if(fromAddress.isPresent()) {
                InternetAddress fromInternetAddress = new InternetAddress(fromAddress.get());
                msg.setFrom(fromInternetAddress);
            }
            msg.setRecipients(Message.RecipientType.TO, recipientEmailAddress);
            msg.setSubject(subject);
            msg.setText(text, UTF_8);
            msg.setHeader("Content-Type", String.format("text/plain; charset=\"%s\"", UTF_8));
            msg.setHeader("Content-Transfer-Encoding", "quoted-printable");
            InternetAddress from = getFromAddress();
            msg.setFrom(from);
            Transport.send(msg);
        } catch (MessagingException e) {
            exceptionHandler.handleMessagingException(e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.severe(e);
        }
    }

    /**
     * Determines whether or not the property mail.smtp.auth is set to "true".
     * @return {@code true} if the mail.smtp.auth is set to "true", otherwise {@code false}.
     */
    private boolean isSmtpAuth() {
        String value = properties.getProperty(MAIL_SMTP_AUTH);
        return value != null && "true".equals(value);
    }

    private Optional<String> getSmtpUser() {
        return getPropertyValue(MAIL_SMTP_USER);
    }

    private Optional<String> getSmtpPassword() {
        return getPropertyValue(MAIL_SMTP_PASSWORD);
    }

    /**
     * Creates a {@link Session} using the values stored in {@link MailManager#properties}.  If mail.smtp.auth is set
     * to {@code true} then an authentication handler will be set up to authenticate against the given user name
     * (specified by mail.smtp.user) and password (specified by mail.smtp.password).
     * @return A session with the settings in {@link MailManager#properties}.  Not {@code null}.
     */
    private Session createMailSession() {
        final Session session;
        if(isSmtpAuth()) {
            session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(getSmtpUser().get(), getSmtpPassword().get());
                }
            });
        }
        else {
            session = Session.getInstance(properties);
        }
        return session;
    }

    /**
     * Gets the from address as an {@link InternetAddress}.  The personal name will be set to either the value supplied
     * by the {@link MailManager#DEFAULT_FROM_PERSONALNAME} property, or by the default value (specified by
     * {@link MailManager#DEFAULT_FROM_PERSONALNAME}).
     * @return The from address.
     * @throws UnsupportedEncodingException
     */
    private InternetAddress getFromAddress() throws UnsupportedEncodingException {
        String from = getPropertyValue(MAIL_SMTP_FROM, DEFAULT_FROM_VALUE);
        String personalName = getPropertyValue(MAIL_SMTP_FROM_PERSONALNAME, DEFAULT_FROM_PERSONALNAME);
        return new InternetAddress(from, personalName, UTF_8);
    }

    /**
     * Gets the property value for the specified property name.
     * @param propertyName The property name. Not {@code null}.
     * @return The property value for the specified name.  If the property value is {@code null} or is not set
     * then {@link com.google.common.base.Optional#absent()} will be returned.
     * @throws NullPointerException if {@code propertyName} is {@code null}.
     */
    private Optional<String> getPropertyValue(String propertyName) {
        checkNotNull(propertyName);
        return Optional.fromNullable(properties.getProperty(propertyName));
    }

    /**
     * Gets the value (or the default value) for the specified property name.
     * @param propertyName The property name.  Not {@code null}.
     * @param defaultValue The default value.  May be {@code null}.
     * @return The value for the property if it exists, or the specified default value if the property does not exist.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    private String getPropertyValue(String propertyName, String defaultValue) {
        checkNotNull(propertyName);
        checkNotNull(defaultValue);
        String value = properties.getProperty(propertyName);
        if(value != null) {
            return value;
        }
        else {
            return defaultValue;
        }
    }
}
