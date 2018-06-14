package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityAnnotationCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class EntityAnnotationCriteriaPresenter implements CriteriaPresenter<EntityMatchCriteria> {

    @Nonnull
    private final AnnotationCriteriaPresenter delegate;

    @Inject
    public EntityAnnotationCriteriaPresenter(@Nonnull AnnotationCriteriaPresenter delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        delegate.start(container);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends EntityMatchCriteria> getCriteria() {
        return delegate.getCriteria()
                       .map(EntityAnnotationCriteria::get);
    }
}
