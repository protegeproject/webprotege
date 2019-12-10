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
    private final EdgeCriteriaListPresenter inclusionCriteriaPresenter;

    @Nonnull
    private final EdgeCriteriaListPresenter exclusionCriteriaPresenter;

    @Nonnull
    private final VizSettingsView view;

    @Inject
    public VizSettingsPresenter(@Nonnull EdgeCriteriaListPresenter inclusionCriteriaPresenter,
                                @Nonnull EdgeCriteriaListPresenter exclusionCriteriaPresenter,
                                @Nonnull VizSettingsView view) {
        this.inclusionCriteriaPresenter = inclusionCriteriaPresenter;
        this.exclusionCriteriaPresenter = exclusionCriteriaPresenter;
        this.view = view;
    }

    public void setInclusionCriteria(CompositeEdgeCriteria criteria) {
        inclusionCriteriaPresenter.setCriteria(criteria);
    }

    public void setExclusionCriteria(CompositeEdgeCriteria criteria) {
        exclusionCriteriaPresenter.setCriteria(criteria);
    }

    @Nonnull
    public Optional<? extends EdgeCriteria> getInclusionCriteria() {
        return inclusionCriteriaPresenter.getCriteria();
    }

    @Nonnull
    public Optional<? extends EdgeCriteria> getExclusionCriteria() {
        return exclusionCriteriaPresenter.getCriteria();
    }

    public void setApplySettingsHandler(Runnable runnable) {
        view.setApplySettingsHandler(runnable);
    }

    public void setCancelHandler(Runnable runnable) {
        view.setCancelSettingsHandler(runnable);
    }


    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        inclusionCriteriaPresenter.start(view.getIncludeCriteriaContainer());
        inclusionCriteriaPresenter.setMatchTextPrefix("");
        exclusionCriteriaPresenter.start(view.getExclusionCriteriaContainer());
        exclusionCriteriaPresenter.setMatchTextPrefix("");
    }
}
