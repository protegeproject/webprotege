package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.SubClassOfCriteria;
import org.semanticweb.owlapi.model.OWLNamedObject;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class SubClassOfCriteriaPresenter implements CriteriaPresenter<SubClassOfCriteria> {

    @Nonnull
    private final ClassSelectorView view;

    @Inject
    public SubClassOfCriteriaPresenter(@Nonnull ClassSelectorView view) {
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
    public Optional<? extends SubClassOfCriteria> getCriteria() {
        return view.getOwlClass()
                   .map(SubClassOfCriteria::get);
    }
}
