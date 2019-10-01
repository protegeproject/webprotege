package edu.stanford.bmir.protege.web.server.chgpwd;

import edu.stanford.bmir.protege.web.server.app.ApplicationNameSupplier;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.filemanager.FileContents;
import edu.stanford.bmir.protege.web.server.mail.MessagingExceptionHandler;
import edu.stanford.bmir.protege.web.server.mail.SendMailImpl;
import edu.stanford.bmir.protege.web.server.templates.TemplateEngine;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordMailer_TestCase {

    private static final String EMAIL_ADDRESS = "email.address";

    private static final String PWD = "xzy123xzy";

    private static final String THE_USER_NAME = "the user name";

    private static final String THE_APPLICATION_URL = "The Application URL";

    private static final String THE_POPULATED_TEMPLATE = "The populated template";

    private static final String THE_APP_NAME = "TheAppName";

    private static final String TEMPLATE = "Template";

    private ResetPasswordMailer mailer;

    @Mock
    private SendMailImpl sendMailImpl;

    @Mock
    private UserId userId;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private FileContents templateFile;

    @Mock
    private PlaceUrl placeUrl;

    @Mock
    private ApplicationNameSupplier appNameSupplier;


    @Captor
    private ArgumentCaptor<Map<String, Object>> objectMapCaptor;

    @Before
    public void setUp() throws Exception {
        mailer = new ResetPasswordMailer(sendMailImpl, templateEngine, templateFile, placeUrl, appNameSupplier);
        when(templateFile.getContents())
                .thenReturn(TEMPLATE);
        when(userId.getUserName())
                .thenReturn(THE_USER_NAME);
        when(placeUrl.getApplicationUrl())
                .thenReturn(THE_APPLICATION_URL);
        when(templateEngine.populateTemplate(anyString(), anyMap()))
                .thenReturn(THE_POPULATED_TEMPLATE);
        when(appNameSupplier.get())
                .thenReturn(THE_APP_NAME);
    }

    @Test
    public void shouldPopulateUserId() {
        mailer.sendEmail(userId, EMAIL_ADDRESS, PWD, e -> {});
        verify(templateEngine, times(1)).populateTemplate(anyString(), objectMapCaptor.capture());
        Map<String, Object> objectMap = objectMapCaptor.getValue();
        assertThat(objectMap, Matchers.hasEntry("userId", THE_USER_NAME));
    }

    @Test
    public void shouldPopulatePassword() {
        mailer.sendEmail(userId, EMAIL_ADDRESS, PWD, e -> {});
        verify(templateEngine, times(1)).populateTemplate(any(), objectMapCaptor.capture());
        Map<String, Object> objectMap = objectMapCaptor.getValue();
        assertThat(objectMap, Matchers.hasEntry("pwd", PWD));
    }

    @Test
    public void shouldPopulateApplicationUrl() {
        mailer.sendEmail(userId, EMAIL_ADDRESS, PWD, e -> {});
        verify(templateEngine, times(1)).populateTemplate(any(), objectMapCaptor.capture());
        Map<String, Object> objectMap = objectMapCaptor.getValue();
        assertThat(objectMap, Matchers.hasEntry("application.url", THE_APPLICATION_URL));
    }

    @Test
    public void shouldSendEmailToSpecifiedAddress() {
        mailer.sendEmail(userId, EMAIL_ADDRESS, PWD, e -> {});
        verify(sendMailImpl, times(1)).sendMail(
                eq(singletonList(EMAIL_ADDRESS)),
                anyString(),
                eq(THE_POPULATED_TEMPLATE), any(MessagingExceptionHandler.class));
    }
}
