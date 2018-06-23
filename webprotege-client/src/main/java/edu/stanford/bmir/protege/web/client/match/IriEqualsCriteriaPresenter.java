package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.IriEqualsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public class IriEqualsCriteriaPresenter implements CriteriaPresenter<IriEqualsCriteria> {

    @Nonnull
    private final IriEqualsView view;

    @Inject
    public IriEqualsCriteriaPresenter(@Nonnull IriEqualsView view) {
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
    public Optional<? extends IriEqualsCriteria> getCriteria() {
        return view.getIri()
                .map(IriEqualsCriteria::get);
    }

    @Override
    public void setCriteria(@Nonnull IriEqualsCriteria criteria) {
        view.setIri(criteria.getIri());
    }
}
