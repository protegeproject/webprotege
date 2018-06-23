package edu.stanford.bmir.protege.web.client.match;

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
 * 14 Jun 2018
 */
public class NonUniqueLangTagsCriteriaPresenter implements CriteriaPresenter<EntityHasNonUniqueLangTagsCriteria> {

    @Nonnull
    private final AnnotationPropertyCriteriaView view;

    @Nonnull
    private final AnnotationPropertyIriRenderer renderer;

    @Inject
    public NonUniqueLangTagsCriteriaPresenter(@Nonnull AnnotationPropertyCriteriaView view,
                                              @Nonnull AnnotationPropertyIriRenderer renderer) {
        this.view = checkNotNull(view);
        this.renderer = checkNotNull(renderer);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<EntityHasNonUniqueLangTagsCriteria> getCriteria() {
        return view.getProperty()
                .map(prop -> EntityHasNonUniqueLangTagsCriteria.get(IriEqualsCriteria.get(prop)));
    }

    @Override
    public void setCriteria(@Nonnull EntityHasNonUniqueLangTagsCriteria criteria) {
        criteria.getPropertyCriteria().accept(new AnnotationPropertyCriteriaVisitor<Void>() {
            @Override
            public Void visit(@Nonnull AnyAnnotationPropertyCriteria criteria) {
                view.clear();
                return null;
            }

            @Override
            public Void visit(@Nonnull IriEqualsCriteria criteria) {
                renderer.renderAnnotationPropertyIri(criteria.getIri(),
                                                     view::setProperty);
                return null;
            }
        });
    }
}
