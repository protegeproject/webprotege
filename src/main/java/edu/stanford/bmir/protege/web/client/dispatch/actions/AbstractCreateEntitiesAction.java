package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public abstract class AbstractCreateEntitiesAction<R extends AbstractCreateEntityResult<E>, E extends OWLEntity> extends AbstractHasProjectAction<R> {

    private ImmutableSet<String> browserTexts;

    public AbstractCreateEntitiesAction(@Nonnull ProjectId projectId,
                                        @Nonnull ImmutableSet<String> browserTexts) {
        super(projectId);
        this.browserTexts = checkNotNull(browserTexts);
    }

    @GwtSerializationConstructor
    protected AbstractCreateEntitiesAction() {
    }

    @Nonnull
    public ImmutableSet<String> getBrowserTexts() {
        return browserTexts;
    }


}
