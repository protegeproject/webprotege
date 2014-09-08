package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import edu.stanford.protege.reasoning.ReasoningService;
import edu.stanford.protege.reasoning.protocol.ReasoningClient;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/09/2014
 */
public class WebProtegeServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ReasoningService.class).to(ReasoningClient.class).in(Scopes.SINGLETON);
    }
}
