package edu.stanford.bmir.protege.web.server.mail;

import edu.stanford.bmir.protege.web.server.app.ApplicationHostSupplier;
import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jvnet.mock_javamail.Mailbox;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/05/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class SendMailImplTestCase {

    public static final String SUBJECT = "Test subject";

    public static final String BODY = "Body of test message";

    public static final String TO = "admin.email@webprotege.stanford.edu";

    public static final String EXPECTED_REPLY_TO_ADDRESS_PREFIX = "no-reply@";

    public static final String APP_NAME = "TestAppName";

    public static final String HOST_NAME = "test.host.name";

    private Message message;

    @Mock
    private WebProtegeLogger logger;

    @Mock
    private ApplicationNameSupplier applicationNameSupplier;

    @Mock
    private ApplicationHostSupplier applicationHostSupplier;

    @Mock
    private MessageIdGenerator messageIdGenerator;

    @Before
    public void setUp() throws MessagingException {
        Mailbox.clearAll();
        Properties mailProperties = new Properties();
        MessagingExceptionHandler messagingExceptionHandler = mock(MessagingExceptionHandler.class);
        when(applicationNameSupplier.get()).thenReturn(APP_NAME);
        when(applicationHostSupplier.get()).thenReturn(HOST_NAME);
        when(messageIdGenerator.generateUniqueMessageId()).thenReturn(new MessageId(UUID.randomUUID().toString()));
        SendMailImpl sendMailImpl = new SendMailImpl(applicationNameSupplier,
                                                     applicationHostSupplier,
                                                     mailProperties, messagingExceptionHandler,
                                                     messageIdGenerator);
        sendMailImpl.sendMail(singletonList(TO), SUBJECT, BODY, messagingExceptionHandler);
        List<Message> messageList = Mailbox.get(TO);
        message = messageList.get(0);
    }

    @Test
    public void shouldSendEmailFromNoReplyAtHostName() throws MessagingException, IOException {
        Address[] from = message.getFrom();
        assertThat(from.length, is(1));
        InternetAddress address = (InternetAddress) from[0];
        String expected = EXPECTED_REPLY_TO_ADDRESS_PREFIX + HOST_NAME;
        assertThat(address.getAddress(), is(equalTo(expected)));
    }

    @Test
    public void shouldSendEmailWithSpecifiedSubject() throws MessagingException, IOException {
        assertThat(message.getSubject(), is(equalTo(SUBJECT)));
    }

    @Test
    public void shouldSendEmailWithSpecifiedBody() throws MessagingException, IOException {
        assertThat(message.getContent().toString(), is(equalTo(BODY)));
    }

    @Test
    public void shouldSendEmailToSpecifiedRecipient()  throws MessagingException, IOException {
        Address[] recipients = message.getRecipients(Message.RecipientType.TO);
        assertThat(recipients.length, is(1));
        InternetAddress address = (InternetAddress) recipients[0];
        assertThat(address.getAddress(), is(equalTo(TO)));
    }
}
