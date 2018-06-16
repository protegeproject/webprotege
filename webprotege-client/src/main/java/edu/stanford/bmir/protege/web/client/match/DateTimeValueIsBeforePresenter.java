package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class DateTimeValueIsBeforePresenter implements CriteriaPresenter<LiteralCriteria> {

    @Nonnull
    private final DateTimeView view;

    @Inject
    public DateTimeValueIsBeforePresenter(@Nonnull DateTimeView view) {
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
    public Optional<LiteralCriteria> getCriteria() {
        DateIsBeforeCriteria criteria = DateIsBeforeCriteria.get(
                view.getYear(),
                view.getMonth(),
                view.getDay()
        );
        return Optional.of(
                LiteralComponentCriteria.lexicalValueMatches(criteria)
        );
    }
}
