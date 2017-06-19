package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertiesResult extends AbstractCreateEntityInHierarchyResult<OWLObjectProperty> {


    /**
     * For serialization purposes only!
     */
    private CreateObjectPropertiesResult() {
    }

    public CreateObjectPropertiesResult(Map<OWLObjectProperty, String> entity2BrowserTextMap, ProjectId projectId, Optional<OWLObjectProperty> parent, EventList<ProjectEvent<?>> eventList) {
        super(checkNotNull(entity2BrowserTextMap), projectId, parent, eventList);
    }


}
