package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassesAction extends CreateEntitiesInHierarchyAction<CreateClassesResult, OWLClass> {

    @GwtSerializationConstructor
    private CreateClassesAction() {
    }

    public CreateClassesAction(@Nonnull ProjectId projectId,
                               @Nonnull ImmutableSet<String> browserTexts,
                               @Nonnull Optional<OWLClass> superClass) {
        super(projectId, browserTexts, superClass);
    }
}
