package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateAnnotationPropertiesAction extends CreateEntitiesInHierarchyAction<CreateAnnotationPropertiesResult, OWLAnnotationProperty> {

    public CreateAnnotationPropertiesAction(@Nonnull ProjectId projectId,
                                            @Nonnull String sourceText,
                                            @Nonnull String langTag,
                                            @Nonnull ImmutableSet<OWLAnnotationProperty> parents) {
        super(projectId, sourceText, langTag, parents);
    }

    /**
     * For serialization purposes only
     */
    private CreateAnnotationPropertiesAction() {
    }
}
