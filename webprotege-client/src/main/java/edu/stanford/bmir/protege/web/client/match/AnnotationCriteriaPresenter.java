package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.renderer.AnnotationPropertyIriRenderer;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class AnnotationCriteriaPresenter implements CriteriaPresenter<AnnotationCriteria> {

    @Nonnull
    private final AnnotationCriteriaView view;

    @Nonnull
    private final AnnotationValueListCriteriaPresenter valuePresenter;

    @Nonnull
    private final AnnotationPropertyIriRenderer renderer;

    @Inject
    public AnnotationCriteriaPresenter(@Nonnull AnnotationCriteriaView view,
                                       @Nonnull AnnotationValueListCriteriaPresenter valuePresenter,
                                       @Nonnull AnnotationPropertyIriRenderer renderer) {
        this.view = checkNotNull(view);
        this.valuePresenter = checkNotNull(valuePresenter);
        this.renderer = checkNotNull(renderer);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        valuePresenter.start(view.getValueCriteriaViewContainer());
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends AnnotationCriteria> getCriteria() {
        AnnotationPropertyCriteria propertyCriteria = view.getSelectedProperty()
                                                          .map(prop -> (AnnotationPropertyCriteria) IriEqualsCriteria.get(prop))
                                                          .orElse(AnyAnnotationPropertyCriteria.get());
        Optional<? extends AnnotationValueCriteria> valueCriteria = valuePresenter.getCriteria();
        if(!valueCriteria.isPresent()) {
            return Optional.empty();
        }
        AnnotationCriteria annotationComponentCriteria = AnnotationComponentsCriteria.get(
                propertyCriteria,
                valueCriteria.get(),
                AnyAnnotationSetCriteria.get()
        );
        return Optional.of(annotationComponentCriteria);
    }

    @Override
    public void setCriteria(@Nonnull AnnotationCriteria criteria) {
        GWT.log("[AnnotationCriteriaPresenter] Setting criteria " + criteria);
        criteria.accept(new AnnotationCriteriaVisitor<Object>() {
            @Nonnull
            @Override
            public Object visit(@Nonnull AnnotationComponentsCriteria criteria) {
                criteria.getAnnotationPropertyCriteria().accept(new AnnotationPropertyCriteriaVisitor<Object>() {
                    @Override
                    public Object visit(@Nonnull AnyAnnotationPropertyCriteria criteria) {
                        view.clearProperty();
                        return null;
                    }

                    @Override
                    public Object visit(@Nonnull IriEqualsCriteria criteria) {
                        GWT.log("[AnnotationCriteriaPresenter] Setting property " + criteria);
                        renderer.renderAnnotationPropertyIri(criteria.getIri(), view::setSelectedProperty);
                        return null;
                    }
                });
                valuePresenter.setCriteria(criteria.getAnnotationValueCriteria().asCompositeAnnotationValueCriteria());
                return null;
            }
        });
    }
}
