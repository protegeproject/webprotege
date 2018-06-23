package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.AnnotationPresence;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityAnnotationCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class EntityAnnotationCriteriaAbsentPresenter implements CriteriaPresenter<EntityAnnotationCriteria> {

    @Nonnull
    private final AnnotationCriteriaPresenter delegate;

    @Inject
    public EntityAnnotationCriteriaAbsentPresenter(@Nonnull AnnotationCriteriaPresenter delegate) {
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
    public Optional<? extends EntityAnnotationCriteria> getCriteria() {
        Optional<? extends AnnotationCriteria> criteria = delegate.getCriteria();
        return criteria
                .map(c -> EntityAnnotationCriteria.get(c, AnnotationPresence.NONE));
    }

    @Override
    public void setCriteria(@Nonnull EntityAnnotationCriteria criteria) {
        delegate.setCriteria(criteria.getAnnotationCriteria());
    }
}
