package edu.stanford.bmir.protege.web.client.match;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationComponentCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.IriCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.IriHasAnnotationsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class IriHasAnnotationsCriteriaPresenter implements CriteriaPresenter<IriCriteria> {

    @Nonnull
    private final AnnotationCriteriaPresenter delegate;

    @Inject
    public IriHasAnnotationsCriteriaPresenter(@Nonnull AnnotationCriteriaPresenter delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        delegate.start(container);
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public Optional<IriCriteria> getCriteria() {
        Optional<? extends AnnotationComponentCriteria> annotationCriteria = delegate.getCriteria();
        return annotationCriteria
                       .map(criteria -> {
                           ImmutableList<AnnotationComponentCriteria> annoCriteria = ImmutableList.of((AnnotationComponentCriteria) criteria);
                           return IriHasAnnotationsCriteria.get(annoCriteria);
                       });
    }
}
