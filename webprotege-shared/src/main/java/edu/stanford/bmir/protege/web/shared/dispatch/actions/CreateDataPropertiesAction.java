package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateDataPropertiesAction extends CreateEntitiesInHierarchyAction<CreateDataPropertiesResult, OWLDataProperty> {

    public CreateDataPropertiesAction(@Nonnull ProjectId projectId,
                                      @Nonnull String sourceText,
                                      @Nonnull String langTag,
                                      @Nonnull ImmutableSet<OWLDataProperty> parents) {
        super(projectId, sourceText, langTag, parents);
    }

    @GwtSerializationConstructor
    private CreateDataPropertiesAction() {
    }
}
