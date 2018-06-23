package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationPropertyCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityAnnotationValuesAreNotDisjointCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.IriEqualsCriteria;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class EntityAnnotationValuesAreNotDisjointCriteriaPresenter implements CriteriaPresenter<EntityAnnotationValuesAreNotDisjointCriteria> {


    @Nonnull
    private final AnnotationPropertyPairView view;

    @Inject
    public EntityAnnotationValuesAreNotDisjointCriteriaPresenter(@Nonnull AnnotationPropertyPairView view) {
        this.view = checkNotNull(view);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends EntityAnnotationValuesAreNotDisjointCriteria> getCriteria() {
        Optional<OWLAnnotationProperty> firstProperty = view.getFirstProperty();
        Optional<OWLAnnotationProperty> secondProperty = view.getSecondProperty();
        if(!firstProperty.isPresent()) {
            return Optional.empty();
        }
        if(!secondProperty.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(
                EntityAnnotationValuesAreNotDisjointCriteria.get(
                        IriEqualsCriteria.get(firstProperty.get()),
                        IriEqualsCriteria.get(secondProperty.get())
                )
        );
    }

    @Override
    public void setCriteria(@Nonnull EntityAnnotationValuesAreNotDisjointCriteria criteria) {
        AnnotationPropertyCriteria firstProperty = criteria.getFirstProperty();
        AnnotationPropertyCriteria secondProperty = criteria.getSecondProperty();
    }
}
