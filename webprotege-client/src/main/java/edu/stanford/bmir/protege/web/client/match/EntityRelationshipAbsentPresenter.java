package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.RelationshipPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityRelationshipCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-04
 */
public class EntityRelationshipAbsentPresenter implements CriteriaPresenter<EntityRelationshipCriteria>  {

    @Nonnull
    private final EntityRelationshipCriteriaPresenter delegate;

    @Inject
    public EntityRelationshipAbsentPresenter(@Nonnull EntityRelationshipCriteriaPresenter delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        delegate.start(container);
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public Optional<? extends EntityRelationshipCriteria> getCriteria() {
        return delegate.getCriteria()
                .map(criteria -> EntityRelationshipCriteria.get(RelationshipPresence.NONE,
                                                                criteria.getRelationshipPropertyCriteria(),
                                                                criteria.getRelationshipValueCriteria()));
    }

    @Override
    public void setCriteria(@Nonnull EntityRelationshipCriteria criteria) {
        delegate.setCriteria(criteria);
    }
}
