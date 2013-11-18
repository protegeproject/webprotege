package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public abstract class AbstractCreateEntityInHierarchyAction<R extends AbstractCreateEntityInHierarchyResult<E>, E extends OWLEntity> extends AbstractCreateEntityAction<R, E> {

    private E parent;

    /**
     * For serialization purposes only!
     */
    protected AbstractCreateEntityInHierarchyAction() {
    }

    public AbstractCreateEntityInHierarchyAction(ProjectId projectId, Set<String> browserTexts, Optional<E> parent) {
        super(projectId, browserTexts);
        this.parent = parent.orNull();
    }

    public Optional<E> getParent() {
        return Optional.fromNullable(parent);
    }
}
