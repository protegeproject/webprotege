package edu.stanford.bmir.protege.web.server.mail;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 07/02/15
 */
public interface SendMail {

    /**
     * Sends an email to the specified recipient.  The email will have the specified subject and specified content.
     * @param recipientEmailAddress The email address of the recipient.  Not {@code null}.
     * @param subject The subject of the email.  Not {@code null}.
     * @param text The content of the email.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void sendMail(String recipientEmailAddress, String subject, String text);

    /**
     * Sends an email to the specified recipient.  The email will have the specified subject and specified content.
     * @param recipientEmailAddress The email address of the recipient.  Not {@code null}.
     * @param subject The subject of the email.  Not {@code null}.
     * @param text The content of the email.  Not {@code null}.
     * @param exceptionHandler An exception handler for handling {@link javax.mail.MessagingException}s.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    void sendMail(String recipientEmailAddress, String subject, String text, MessagingExceptionHandler exceptionHandler);
}
