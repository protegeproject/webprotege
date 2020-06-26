package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.RootCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSelector;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-16
 */
public class EntityFormSelectorPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormSelectorView view;

    @Nonnull
    private final EntityCriteriaPresenter criteriaPresenter;

    @Nonnull
    private Optional<EntityFormSelector> mostRecentSetSelector = Optional.empty();

    @Inject
    public EntityFormSelectorPresenter(@Nonnull ProjectId projectId,
                                       @Nonnull EntityFormSelectorView view,
                                       @Nonnull EntityCriteriaPresenter entityCriteriaPresenter) {
        this.projectId = projectId;
        this.view = checkNotNull(view);
        this.criteriaPresenter = entityCriteriaPresenter;
    }

    public void clear() {
        view.clear();
    }


    public void setSelectorCriteria(@Nonnull CompositeRootCriteria criteria) {
        criteriaPresenter.setCriteria(criteria);
    }

    @Nonnull
    public Optional<CompositeRootCriteria> getSelectorCriteria() {
        return criteriaPresenter.getCriteria().map(c -> c);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        AcceptsOneWidget criteriaContainer = view.getSelectorCriteriaContainer();
        criteriaPresenter.start(criteriaContainer);
    }
}
