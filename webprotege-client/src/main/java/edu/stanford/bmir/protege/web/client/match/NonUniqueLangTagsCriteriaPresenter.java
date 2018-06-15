package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.AnnotationPropertyCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityHasNonUniqueLangTagsCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.IriEqualsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class NonUniqueLangTagsCriteriaPresenter implements CriteriaPresenter<EntityMatchCriteria> {

    @Nonnull
    private final AnnotationPropertyCriteriaView view;

    @Inject
    public NonUniqueLangTagsCriteriaPresenter(@Nonnull AnnotationPropertyCriteriaView view) {
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
    public Optional<? extends EntityMatchCriteria> getCriteria() {
        return view.getProperty()
                .map(prop -> EntityHasNonUniqueLangTagsCriteria.get(IriEqualsCriteria.get(prop)));
    }
}
