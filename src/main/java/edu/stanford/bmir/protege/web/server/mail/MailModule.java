package edu.stanford.bmir.protege.web.server.mail;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.inject.ApplicationHost;
import edu.stanford.bmir.protege.web.server.inject.ApplicationName;
import edu.stanford.bmir.protege.web.server.inject.MailProperties;

import javax.inject.Singleton;
import java.util.Properties;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class MailModule {

    @Provides
    @MailProperties
    @Singleton
    public Properties provideMailProperties(MailPropertiesProvider provider) {
        return provider.get();
    }

    @Provides
    @Singleton
    public MailManager provideMailManager(@ApplicationName String appName,
                                          @ApplicationHost String appHost,
                                          @MailProperties Properties properties,
                                          MessagingExceptionHandler exceptionHandler) {
        return new MailManager(appName, appHost, properties, exceptionHandler);
    }

    @Provides
    public SendMail provideSendMail(MailManager manager) {
        return manager;
    }

    @Provides
    public MessagingExceptionHandler provideMessagingExceptionHandler(MessagingExceptionHandlerImpl handler) {
        return handler;
    }
}
