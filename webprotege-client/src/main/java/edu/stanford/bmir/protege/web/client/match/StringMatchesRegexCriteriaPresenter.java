package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringContainsRegexMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringMatchesRegexCriteriaPresenter implements CriteriaPresenter<StringContainsRegexMatchCriteria> {

    @Nonnull
    private final SimpleStringCriteriaView view;

    @Inject
    public StringMatchesRegexCriteriaPresenter(@Nonnull SimpleStringCriteriaView view) {
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
    public Optional<StringContainsRegexMatchCriteria> getCriteria() {
        StringContainsRegexMatchCriteria lexicalValueCriteria = StringContainsRegexMatchCriteria.get(view.getValue(), view.isIgnoreCase());
        return Optional.of(lexicalValueCriteria);
    }

    @Override
    public void setCriteria(@Nonnull StringContainsRegexMatchCriteria criteria) {
        view.setValue(criteria.getPattern());
        view.setIgnoreCase(criteria.isIgnoreCase());
    }
}
