package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.ObjectPresenter;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPositionCriteriaListPresenter;
import edu.stanford.bmir.protege.web.shared.crud.ConditionalIriPrefix;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeHierarchyPositionCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-07
 */
public class ConditionalIriPrefixPresenter implements ObjectPresenter<ConditionalIriPrefix> {

    @Nonnull
    private final ConditionalIriPrefixView view;

    @Nonnull
    private final HierarchyPositionCriteriaListPresenter criteriaListPresenter;

    @Inject
    public ConditionalIriPrefixPresenter(@Nonnull ConditionalIriPrefixView view,
                                         @Nonnull HierarchyPositionCriteriaListPresenter criteriaListPresenter) {
        this.view = checkNotNull(view);
        this.criteriaListPresenter = checkNotNull(criteriaListPresenter);
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return "Rule";
    }

    @Nonnull
    @Override
    public Optional<ConditionalIriPrefix> getValue() {
        CompositeHierarchyPositionCriteria criteria =
                criteriaListPresenter.getCriteria()
                                     .map(c -> (CompositeHierarchyPositionCriteria) c)
                                     .orElse(CompositeHierarchyPositionCriteria.get());
        return Optional.of(ConditionalIriPrefix.get(view.getIriPrefix(),
                                                    criteria));
    }

    @Override
    public void setValue(@Nonnull ConditionalIriPrefix value) {
        view.setIriPrefix(value.getIriPrefix());
        criteriaListPresenter.setCriteria(value.getCriteria());
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {

    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        criteriaListPresenter.start(view.getHierarchyPositionCriteriaViewContainer());
    }
}
