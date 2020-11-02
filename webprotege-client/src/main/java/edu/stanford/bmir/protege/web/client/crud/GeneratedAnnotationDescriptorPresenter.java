package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyPositionCriteriaPresenter;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationDescriptor;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedValueDescriptor;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeRootCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyPositionCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-01
 */
public class GeneratedAnnotationDescriptorPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final GeneratedAnnotationDescriptorView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ImmutableList<GeneratedValueDescriptorPresenter> presenters;

    private IncrementingPatternDescriptorPresenter incrementingPatternDescriptorPresenter;

    @Nonnull
    private final HierarchyPositionCriteriaPresenter criteriaPresenter;

    @Inject
    public GeneratedAnnotationDescriptorPresenter(@Nonnull ProjectId projectId,
                                                  @Nonnull GeneratedAnnotationDescriptorView view,
                                                  @Nonnull DispatchServiceManager dispatch,
                                                  @Nonnull IncrementingPatternDescriptorPresenter incrementingPatternDescriptorPresenter,
                                                  @Nonnull HierarchyPositionCriteriaPresenter criteriaPresenter) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.presenters = ImmutableList.of(incrementingPatternDescriptorPresenter);
        this.incrementingPatternDescriptorPresenter = checkNotNull(incrementingPatternDescriptorPresenter);
        this.criteriaPresenter = checkNotNull(criteriaPresenter);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setSelectedTypeNameChangedHandler(typeName -> setPresenterFromTypeName());
        ImmutableList<String> typeNames = presenters.stream()
                                                  .map(GeneratedValueDescriptorPresenter::getName)
                                                  .collect(toImmutableList());
        view.setTypeNames(typeNames);
        setPresenterFromTypeName();

        criteriaPresenter.start(view.getActivatedByCriteriaContainer());
    }

    public void setValue(@Nonnull GeneratedAnnotationDescriptor descriptor) {
        dispatch.execute(new GetEntityRenderingAction(projectId, descriptor.getProperty()),
                         result -> view.setProperty((OWLAnnotationPropertyData) result.getEntityData()));
        GeneratedValueDescriptor valueDescriptor = descriptor.getValueDescriptor();
        displayValueDescriptor(valueDescriptor);
        descriptor.getActivatedBy()
                  .ifPresent(criteriaPresenter::setCriteria);
    }

    public Optional<GeneratedAnnotationDescriptor> getValue() {
        Optional<OWLAnnotationProperty> property = view.getProperty()
                                                                    .map(OWLAnnotationPropertyData::getEntity);
        if(!property.isPresent()) {
            return Optional.empty();
        }
        Optional<? extends HierarchyPositionCriteria> criteria = criteriaPresenter.getCriteria();

        String selectedTypeName = view.getSelectedTypeName();
        return presenters.stream()
                  .filter(p -> p.getName().equals(selectedTypeName))
                  .findFirst()
                  .flatMap(GeneratedValueDescriptorPresenter::getValue)
                  .map(v -> GeneratedAnnotationDescriptor.get(property.get(), v, criteria.orElse(null)));
    }

    private void setPresenterFromTypeName() {
        String typeName = view.getSelectedTypeName();
        presenters.stream()
                  .filter(p -> p.getName().equals(typeName))
                  .findFirst()
                  .ifPresent(this::setSelectedPresenter);
    }

    private void displayValueDescriptor(GeneratedValueDescriptor valueDescriptor) {
        valueDescriptor.accept(incrementingPatternDescriptor -> {
            incrementingPatternDescriptorPresenter.start(view.getValueDescriptorContainer());
            incrementingPatternDescriptorPresenter.setValue(incrementingPatternDescriptor);
            setSelectedPresenter(incrementingPatternDescriptorPresenter);
        });
    }

    private void setSelectedPresenter(GeneratedValueDescriptorPresenter presenter) {
        presenter.start(view.getValueDescriptorContainer());
    }
}
