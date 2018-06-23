package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.DateIsAfterCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class DateIsAfterPresenter implements CriteriaPresenter<DateIsAfterCriteria> {

    @Nonnull
    private final DateView view;

    @Inject
    public DateIsAfterPresenter(@Nonnull DateView view) {
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
    public Optional<DateIsAfterCriteria> getCriteria() {
        DateIsAfterCriteria criteria = DateIsAfterCriteria.get(view.getYear(),
                                                               view.getMonth(),
                                                               view.getDay());
        return Optional.of(criteria);
    }

    @Override
    public void setCriteria(@Nonnull DateIsAfterCriteria criteria) {
        view.setYear(criteria.getYear());
        view.setMonth(criteria.getMonth());
        view.setDay(criteria.getDay());
    }
}
