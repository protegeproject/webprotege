package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateDataPropertiesResult extends AbstractCreateEntityInHierarchyResult<OWLDataProperty> {

    public CreateDataPropertiesResult(Map<OWLDataProperty, String> entity2BrowserTextMap, ProjectId projectId, Optional<OWLDataProperty> parent, EventList<ProjectEvent<?>> eventList) {
        super(entity2BrowserTextMap, projectId, parent, eventList);
    }

    private CreateDataPropertiesResult() {
    }
}
