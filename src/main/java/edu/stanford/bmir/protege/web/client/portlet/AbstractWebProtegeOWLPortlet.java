package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@SuppressWarnings("deprecated")
public abstract class AbstractWebProtegeOWLPortlet extends AbstractWebProtegePortlet {

    protected AbstractWebProtegeOWLPortlet(SelectionModel selectionModel, EventBus eventBus, ProjectId projectId, LoggedInUserProvider loggedInUserProvider) {
        super(selectionModel, eventBus, loggedInUserProvider, projectId);
    }

}
