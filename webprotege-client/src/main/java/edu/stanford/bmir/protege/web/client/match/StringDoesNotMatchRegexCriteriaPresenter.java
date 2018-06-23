package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringDoesNotContainRegexMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class StringDoesNotMatchRegexCriteriaPresenter implements CriteriaPresenter<StringDoesNotContainRegexMatchCriteria> {

    @Nonnull
    private final SimpleStringCriteriaView view;

    @Inject
    public StringDoesNotMatchRegexCriteriaPresenter(@Nonnull SimpleStringCriteriaView view) {
        this.view = view;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<StringDoesNotContainRegexMatchCriteria> getCriteria() {
        return Optional.of(
                StringDoesNotContainRegexMatchCriteria.get(view.getValue(),
                                                           view.isIgnoreCase()
                ));
    }

    @Override
    public void setCriteria(@Nonnull StringDoesNotContainRegexMatchCriteria criteria) {
        view.setValue(criteria.getPattern());
        view.setIgnoreCase(criteria.isIgnoreCase());
    }
}
