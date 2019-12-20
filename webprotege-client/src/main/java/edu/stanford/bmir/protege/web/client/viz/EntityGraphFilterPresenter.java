package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.viz.CompositeEdgeCriteria;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphFilter;
import edu.stanford.bmir.protege.web.shared.viz.FilterName;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.validation.constraints.Null;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public class EntityGraphFilterPresenter {

    @Null
    private final EntityGraphFilterView view;

    @Nonnull
    private final EdgeCriteriaListPresenter inclusionCriteriaPresenter;

    @Nonnull
    private final EdgeCriteriaListPresenter exclusionCriteriaPresenter;

    @Inject
    public EntityGraphFilterPresenter(EntityGraphFilterView view,
                                      @Nonnull EdgeCriteriaListPresenter inclusionCriteriaPresenter,
                                      @Nonnull EdgeCriteriaListPresenter exclusionCriteriaPresenter) {
        this.view = view;
        this.inclusionCriteriaPresenter = inclusionCriteriaPresenter;
        this.exclusionCriteriaPresenter = exclusionCriteriaPresenter;
    }

    public void clearValue() {
        this.view.setName("");
        this.view.setDescription("");
    }

    public void setFilter(@Null EntityGraphFilter filter) {
        this.view.setName(filter.getName().getName());
        this.view.setDescription(filter.getDescription());
        this.view.setActive(filter.isActive());
        inclusionCriteriaPresenter.setCriteria(filter.getInclusionCriteria());
        exclusionCriteriaPresenter.setCriteria(filter.getExclusionCriteria());

    }

    public Optional<EntityGraphFilter> getFilter() {
        CompositeEdgeCriteria inclusionCriteria = inclusionCriteriaPresenter.getCriteria()
                                                                            .orElse(CompositeEdgeCriteria.anyEdge());
        CompositeEdgeCriteria exclusionCriteria = exclusionCriteriaPresenter.getCriteria()
                                                                            .orElse(CompositeEdgeCriteria.noEdge());
        return Optional.of(
                EntityGraphFilter.get(
                        FilterName.get(view.getName()),
                        view.getDescription(),
                        inclusionCriteria,
                        exclusionCriteria,
                        view.isActive()
                )
        );
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        inclusionCriteriaPresenter.start(view.getInclusionCriteriaContainer());
        inclusionCriteriaPresenter.setMatchTextPrefix("");
        exclusionCriteriaPresenter.start(view.getExclusionCriteriaContainer());
        exclusionCriteriaPresenter.setMatchTextPrefix("");

    }
}
