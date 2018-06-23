package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.CompositeAnnotationValueCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class AnnotationValueListCriteriaPresenter extends CriteriaListPresenter<AnnotationValueCriteria, CompositeAnnotationValueCriteria> {

    @Inject
    public AnnotationValueListCriteriaPresenter(@Nonnull CriteriaListView view,
                                                @Nonnull Provider<CriteriaListCriteriaViewContainer> viewContainerProvider,
                                                @Nonnull AnnotationValueCriteriaPresenterFactory presenterFactory) {
        super(view, viewContainerProvider, presenterFactory);
        view.setMatchTextPrefix("matches");
    }

    @Override
    protected CompositeAnnotationValueCriteria createCompositeCriteria(@Nonnull ImmutableList<? extends AnnotationValueCriteria> criteriaList) {
        return CompositeAnnotationValueCriteria.get(criteriaList, getMultiMatchType());
    }

    @Override
    protected ImmutableList<? extends AnnotationValueCriteria> decomposeCompositeCriteria(CompositeAnnotationValueCriteria compositeCriteria) {
        return compositeCriteria.getAnnotationValueCriteria();
    }

    @Override
    protected MultiMatchType getMultiMatchType(CompositeAnnotationValueCriteria compositeCriteria) {
        return compositeCriteria.getMultiMatchType();
    }
}
