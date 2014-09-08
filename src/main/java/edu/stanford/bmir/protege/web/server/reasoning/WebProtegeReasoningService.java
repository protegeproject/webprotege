package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.util.concurrent.ListenableFuture;
import edu.stanford.protege.reasoning.Action;
import edu.stanford.protege.reasoning.ReasoningService;
import edu.stanford.protege.reasoning.Response;
import edu.stanford.protege.reasoning.action.ActionHandler;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/09/2014
 */
public class WebProtegeReasoningService implements ReasoningService {

    private ReasoningService delegate;



    @Inject
    public WebProtegeReasoningService(ReasoningService delegate) {
        this.delegate = delegate;
    }

    @Override
    public <A extends Action<R, H>, R extends Response, H extends ActionHandler> ListenableFuture<R> execute(A a) {
        return delegate.execute(a);
    }

    @Override
    public void shutDown() {
        delegate.shutDown();
    }
}
