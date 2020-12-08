package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.IsLeafClassCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-12-07
 */
public class IsLeafClassCriteriaPresenter implements CriteriaPresenter<IsLeafClassCriteria> {

    @Nonnull
    private final BlankCriteriaView view;

    @Inject
    public IsLeafClassCriteriaPresenter(@Nonnull BlankCriteriaView view) {
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
    public Optional<? extends IsLeafClassCriteria> getCriteria() {
        return Optional.of(IsLeafClassCriteria.get());
    }

    @Override
    public void setCriteria(@Nonnull IsLeafClassCriteria criteria) {

    }
}
