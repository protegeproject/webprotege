package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.match.RelationshipValueCriteriaListPresenter;
import edu.stanford.bmir.protege.web.client.match.RelationshipValueCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnyRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRelationshipValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class OwlBindingPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OwlBindingView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final RelationshipValueCriteriaListPresenter relationshipValueCriteriaListPresenter;

    @Inject
    public OwlBindingPresenter(@Nonnull ProjectId projectId,
                               @Nonnull OwlBindingView view,
                               @Nonnull DispatchServiceManager dispatchServiceManager,
                               @Nonnull RelationshipValueCriteriaListPresenter relationshipValueCriteriaListPresenter) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.relationshipValueCriteriaListPresenter = relationshipValueCriteriaListPresenter;
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    public void clear() {
        view.clear();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        relationshipValueCriteriaListPresenter.start(view.getValuesFilterViewContainer());
    }

    public void setBinding(@Nonnull OwlBinding binding) {
        view.clear();
        binding.getOwlProperty()
               .ifPresent(property -> {
                   dispatchServiceManager.execute(new GetEntityRenderingAction(projectId,
                                                                               property),
                                                  result -> view.setProperty(result.getEntityData()));
                   ((OwlPropertyBinding) binding).getValuesCriteria()
                                                 .ifPresent(relationshipValueCriteriaListPresenter::setCriteria);
               });
        if(binding instanceof OwlClassBinding) {
            view.setOwlClassBinding(true);
        }
        else if(binding instanceof OwlInstanceBinding) {
            view.setOwlInstanceBinding(true);
        }
        else if(binding instanceof OwlSubClassBinding) {
            view.setOwlSubClassBinding(true);
        }
    }

    @Nonnull
    public Optional<OwlBinding> getBinding() {
        if(view.isOwlClassBinding()) {
            return Optional.of(OwlClassBinding.get());
        }
        else if(view.isOwlInstanceBinding()) {
            return Optional.of(OwlInstanceBinding.get());
        }
        else if(view.isOwlSubClassBinding()) {
            return Optional.of(OwlSubClassBinding.get());
        }
        return view.getEntity().map(OWLEntityData::getEntity)
            .map(entity -> {
                CompositeRelationshipValueCriteria filterCriteria = relationshipValueCriteriaListPresenter.getCriteria()
                                                                                                          .orElse(null);

                return OwlPropertyBinding.get((OWLProperty) entity, filterCriteria);
            });
    }
}
