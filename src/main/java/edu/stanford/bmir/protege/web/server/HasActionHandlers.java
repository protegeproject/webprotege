package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.Action;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public interface HasActionHandlers {

    List<ActionHandler<? extends Action<? extends Result>, ? extends Result>> getActionHandlers();
}
