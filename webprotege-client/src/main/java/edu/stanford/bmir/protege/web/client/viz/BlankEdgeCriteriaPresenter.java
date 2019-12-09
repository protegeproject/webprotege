package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.BlankCriteriaView;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class BlankEdgeCriteriaPresenter<C extends EdgeCriteria> implements CriteriaPresenter<C> {

    @Nonnull
    private final BlankCriteriaView blankCriteriaView;

    private Optional<C> currentCriteria = Optional.empty();

    @Inject
    public BlankEdgeCriteriaPresenter(@Nonnull BlankCriteriaView blankCriteriaView) {
        this.blankCriteriaView = blankCriteriaView;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(blankCriteriaView);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends C> getCriteria() {
        return currentCriteria;
    }

    @Override
    public void setCriteria(@Nonnull C criteria) {
        currentCriteria = Optional.of(checkNotNull(criteria));
    }
}
