package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralComponentsCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringDoesNotContainRegexMatchCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jun 2018
 */
public class StringDoesNotMatchRegexCriteriaPresenter implements CriteriaPresenter<LiteralCriteria> {

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
    public Optional<? extends LiteralCriteria> getCriteria() {
        return Optional.of(
                LiteralComponentsCriteria.lexicalValueMatches(
                        StringDoesNotContainRegexMatchCriteria.get(view.getValue(),
                                                                   view.isIgnoreCase()
                ))
        );
    }
}
