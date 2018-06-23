package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.IriCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.IriHasAnnotationCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class IriHasAnnotationsCriteriaPresenter implements CriteriaPresenter<IriHasAnnotationCriteria> {

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
    public Optional<IriHasAnnotationCriteria> getCriteria() {
        Optional<? extends AnnotationCriteria> annotationCriteria = delegate.getCriteria();
        return annotationCriteria
                       .map(IriHasAnnotationCriteria::get);
    }

    @Override
    public void setCriteria(@Nonnull IriHasAnnotationCriteria criteria) {
        delegate.setCriteria(criteria.getIriAnnotationCriteria());
    }
}
