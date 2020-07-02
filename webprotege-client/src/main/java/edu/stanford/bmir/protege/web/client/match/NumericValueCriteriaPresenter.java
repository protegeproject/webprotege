package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.NumericValueCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class NumericValueCriteriaPresenter implements CriteriaPresenter<NumericValueCriteria> {

    @Nonnull
    private final NumericValueCriteriaView view;

    @Inject
    public NumericValueCriteriaPresenter(@Nonnull NumericValueCriteriaView view) {
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
    public Optional<NumericValueCriteria> getCriteria() {
        if(!view.getValue().isPresent()) {
            return Optional.empty();
        }
        NumericValueCriteria lexicalValueCriteria = NumericValueCriteria.get(view.getNumericPredicate(), view.getValue().get());
        return Optional.of(lexicalValueCriteria);
    }

    @Override
    public void setCriteria(@Nonnull NumericValueCriteria criteria) {
        view.setValue(criteria.getValue());
        view.setNumericPredicate(criteria.getPredicate());
    }

    public void clear() {
        view.clear();
    }
}
