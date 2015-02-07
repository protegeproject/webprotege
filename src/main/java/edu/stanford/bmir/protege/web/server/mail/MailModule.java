package edu.stanford.bmir.protege.web.server.mail;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.inject.MailProperties;

import java.util.Properties;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class MailModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MailManager.class).asEagerSingleton();
        bind(MessagingExceptionHandler.class).to(WebProtegeLoggerMessagingExceptionHandler.class);
        bind(Properties.class).annotatedWith(MailProperties.class).toProvider(MailPropertiesProvider.class).asEagerSingleton();
    }
}
