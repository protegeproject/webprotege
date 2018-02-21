package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertiesAction extends CreateEntitiesInHierarchyAction<CreateObjectPropertiesResult, OWLObjectProperty> {

    public CreateObjectPropertiesAction(@Nonnull ProjectId projectId,
                                        @Nonnull String sourceText,
                                        @Nonnull Optional<OWLObjectProperty> parent) {
        super(projectId, sourceText, parent);
    }

    @GwtSerializationConstructor
    private CreateObjectPropertiesAction() {

    }
}
