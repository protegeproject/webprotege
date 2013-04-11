package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateAnnotationPropertiesResult extends AbstractCreateEntityInHierarchyResult<OWLAnnotationProperty> {

    public CreateAnnotationPropertiesResult(Map<OWLAnnotationProperty, String> entity2BrowserTextMap, ProjectId projectId, Optional<OWLAnnotationProperty> parent, EventList<ProjectEvent<?>> eventList) {
        super(entity2BrowserTextMap, projectId, parent, eventList);
    }

    private CreateAnnotationPropertiesResult() {
    }
}
