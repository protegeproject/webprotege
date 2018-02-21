package edu.stanford.bmir.protege.web.server.mail;

import javax.annotation.Nonnull;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2017
 */
public class WebProtegeMimeMessage extends MimeMessage {

    private static final String MESSAGE_ID_FIELD = "Message-ID";

    private MessageId messageId;

    public WebProtegeMimeMessage(@Nonnull MessageId messageId,
                                 @Nonnull Session session) {
        super(session);
        this.messageId = checkNotNull(messageId);
    }

    @Override
    protected void updateMessageID() throws MessagingException {
        setHeader(MESSAGE_ID_FIELD, messageId.getId());
    }
}
