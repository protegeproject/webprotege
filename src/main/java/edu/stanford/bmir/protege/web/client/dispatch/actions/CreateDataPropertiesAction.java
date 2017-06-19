package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

import java.util.Optional;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateDataPropertiesAction extends AbstractCreateEntityInHierarchyAction<CreateDataPropertiesResult, OWLDataProperty> {

    public CreateDataPropertiesAction(ProjectId projectId, Set<String> browserTexts, Optional<OWLDataProperty> parent) {
        super(projectId, browserTexts, parent);
    }

    /**
     * For serialization
     */
    private CreateDataPropertiesAction() {
    }
}
