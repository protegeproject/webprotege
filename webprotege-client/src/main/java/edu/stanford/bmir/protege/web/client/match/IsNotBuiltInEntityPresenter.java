package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.IsNotBuiltInEntityCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public class IsNotBuiltInEntityPresenter implements CriteriaPresenter<EntityMatchCriteria> {

    @Nonnull
    private final BlankCriteriaView view;

    @Inject
    public IsNotBuiltInEntityPresenter(@Nonnull BlankCriteriaView view) {
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
    public Optional<? extends EntityMatchCriteria> getCriteria() {
        return Optional.of(IsNotBuiltInEntityCriteria.get());
    }

    @Override
    public void setCriteria(@Nonnull EntityMatchCriteria criteria) {

    }
}
