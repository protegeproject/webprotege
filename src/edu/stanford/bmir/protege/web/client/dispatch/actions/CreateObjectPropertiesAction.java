package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertiesAction extends AbstractCreateEntityInHierarchyAction<CreateObjectPropertiesResult, OWLObjectProperty> {

    /**
     * For serialization purposes only!
     */
    private CreateObjectPropertiesAction() {

    }

    public CreateObjectPropertiesAction(ProjectId projectId, Set<String> browserTexts, Optional<OWLObjectProperty> parent) {
        super(projectId, browserTexts, parent);
    }
}
