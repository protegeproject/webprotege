package edu.stanford.bmir.protege.web.server.init;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.stanford.protege.reasoning.inject.ReasoningServerModule;
import edu.stanford.protege.reasoning.protocol.ReasoningServer;

import javax.servlet.ServletContext;
import java.net.InetSocketAddress;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class StartReasoningServerTask implements ConfigurationTask {

    @Override
    public void run(ServletContext servletContext) throws WebProtegeConfigurationException {
        try {
            Injector injector = Guice.createInjector(new ReasoningServerModule());
            ReasoningServer reasoningServer = injector.getInstance(ReasoningServer.class);
            reasoningServer.start(new InetSocketAddress(3456));
        } catch (Exception e) {
            throw new WebProtegeConfigurationException(e);
        }

    }
}
