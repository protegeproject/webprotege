package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Jun 2018
 */
public class EntityTypeCriteriaPresenter implements CriteriaPresenter<EntityTypeCriteria> {

    @Nonnull
    private final EntityTypeCriteriaView view;

    @Inject
    public EntityTypeCriteriaPresenter(@Nonnull EntityTypeCriteriaView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends EntityTypeCriteria> getCriteria() {
        return Optional.of(EntityTypeCriteria.get(view.getEntityType()));
    }
}
