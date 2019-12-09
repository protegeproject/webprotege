package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.viz.CompositeEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class VizSettingsPresenter {

    @Nonnull
    private final EdgeCriteriaListPresenter presenter;

    @Nonnull
    private final VizSettingsView view;

    @Inject
    public VizSettingsPresenter(@Nonnull EdgeCriteriaListPresenter presenter,
                                @Nonnull VizSettingsView view) {
        this.presenter = presenter;
        this.view = view;
    }

    public void setEdgeCriteria(CompositeEdgeCriteria criteria) {
        presenter.setCriteria(criteria);
    }

    public Optional<? extends EdgeCriteria> getEdgeCriteria() {
        return presenter.getCriteria();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        presenter.start(view.getEdgeCriteriaContainer());
    }
}
