package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public abstract class CreateEntitiesInHierarchyAction<R extends CreateEntitiesInHierarchyResult<E>, E extends OWLEntity> extends AbstractCreateEntitiesAction<R, E> {

    private ImmutableSet<E> parents;

    @GwtSerializationConstructor
    protected CreateEntitiesInHierarchyAction() {
    }

    public CreateEntitiesInHierarchyAction(@Nonnull ProjectId projectId,
                                           @Nonnull String createFromText,
                                           @Nonnull String langTag,
                                           @Nonnull ImmutableSet<E> parents) {
        super(projectId, createFromText, langTag);
        this.parents = checkNotNull(parents);
    }

    @Nonnull
    public ImmutableSet<E> getParents() {
        return parents;
    }
}
