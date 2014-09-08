package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.stanford.protege.reasoning.ReasoningService;
import edu.stanford.protege.reasoning.inject.ReasoningServerModule;
import edu.stanford.protege.reasoning.protocol.ReasoningClient;

import java.net.InetSocketAddress;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/09/2014
 */
public class ReasoningServerManager {

    private static ReasoningServerManager instance = new ReasoningServerManager();

    private ReasoningClient client;

    public ReasoningServerManager() {

    }

    private void bind() {
        try {
            Injector injector = Guice.createInjector(new ReasoningServerModule());
            client = injector.getInstance(ReasoningClient.class);
            client.connect(new InetSocketAddress(3456));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ReasoningService getReasoningService() {
        return client;
    }

    public static synchronized ReasoningServerManager get() {
        if(instance.client == null) {
            instance.bind();
        }
        return instance;
    }
}
