package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.RootCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.viz.TailNodeMatchesCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class TailNodeMatchesCriteriaPresenter implements CriteriaPresenter<TailNodeMatchesCriteria> {

    @Nonnull
    private final RootCriteriaPresenter rootCriteriaPresenter;

    @Inject
    public TailNodeMatchesCriteriaPresenter(@Nonnull RootCriteriaPresenter rootCriteriaPresenter) {
        this.rootCriteriaPresenter = checkNotNull(rootCriteriaPresenter);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        rootCriteriaPresenter.start(container);
    }

    @Override
    public void stop() {
        rootCriteriaPresenter.stop();
    }

    @Override
    public Optional<? extends TailNodeMatchesCriteria> getCriteria() {
        return rootCriteriaPresenter.getCriteria()
                             .map(TailNodeMatchesCriteria::get);
    }

    @Override
    public void setCriteria(@Nonnull TailNodeMatchesCriteria criteria) {
        rootCriteriaPresenter.setCriteria(criteria.getNodeCriteria());
    }
}
