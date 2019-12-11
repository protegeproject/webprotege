package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.server.viz.EdgeCriteriaSplitter;
import edu.stanford.bmir.protege.web.server.viz.SplitEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.viz.AnyEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.CompositeEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.NegatedEdgeCriteria;

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

    @Nonnull
    public EdgeCriteria getEdgeCriteria() {
        CompositeEdgeCriteria inclusionCriteria = getInclusionCriteria();
        CompositeEdgeCriteria exclusionCriteria = getExclusionCriteria();
        NegatedEdgeCriteria negatedExclusionCriteria = NegatedEdgeCriteria.get(exclusionCriteria);
        return CompositeEdgeCriteria.get(MultiMatchType.ALL, inclusionCriteria, negatedExclusionCriteria);
    }

    public void clear() {
        inclusionCriteriaPresenter.setCriteria(CompositeEdgeCriteria.get(MultiMatchType.ANY,
                                                                         AnyEdgeCriteria.get()));
        exclusionCriteriaPresenter.setCriteria(CompositeEdgeCriteria.empty());
    }

    public double getRankSpacing() {
        return view.getRankSpacing();
    }


    public void setEdgeCriteria(@Nonnull EdgeCriteria edgeCriteria) {
        GWT.log("[VizSettingsPresenter] setEdgeCriteria: " + edgeCriteria);
        clear();
        if(!(edgeCriteria instanceof CompositeEdgeCriteria)) {
            return;
        }
        EdgeCriteriaSplitter splitter = new EdgeCriteriaSplitter();
        Optional<SplitEdgeCriteria> splitEdgeCriteria = splitter.splitCriteria((CompositeEdgeCriteria) edgeCriteria);
        GWT.log("[VizSettingsPresenter] Split criteria: " + splitEdgeCriteria);
        splitEdgeCriteria.ifPresent(criteria -> {
            inclusionCriteriaPresenter.setCriteria(criteria.getInclusionCriteria());
            exclusionCriteriaPresenter.setCriteria(criteria.getExclusionCriteria());
        });
    }

    @Nonnull
    private CompositeEdgeCriteria getInclusionCriteria() {
        Optional<? extends CompositeEdgeCriteria> criteria = inclusionCriteriaPresenter.getCriteria();
        if(criteria.isPresent() && !criteria.get().getCriteria().isEmpty()) {
            return criteria.get();
        }
        else {
            return CompositeEdgeCriteria.get(MultiMatchType.ANY, AnyEdgeCriteria.get());
        }
    }

    @Nonnull
    private CompositeEdgeCriteria getExclusionCriteria() {
        Optional<? extends CompositeEdgeCriteria> criteria = exclusionCriteriaPresenter.getCriteria();
        if(criteria.isPresent()) {
            return criteria.get();
        }
        else {
            return CompositeEdgeCriteria.empty();
        }
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
