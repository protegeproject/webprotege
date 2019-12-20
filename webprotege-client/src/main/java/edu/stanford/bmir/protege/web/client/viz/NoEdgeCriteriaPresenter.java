package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.BlankCriteriaView;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.viz.NoEdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-19
 */
public class NoEdgeCriteriaPresenter implements CriteriaPresenter<NoEdgeCriteria> {

    private final BlankCriteriaView view;

    @Inject
    public NoEdgeCriteriaPresenter(BlankCriteriaView view) {
        this.view = view;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends NoEdgeCriteria> getCriteria() {
        return Optional.of(NoEdgeCriteria.get());
    }

    @Override
    public void setCriteria(@Nonnull NoEdgeCriteria criteria) {

    }
}
