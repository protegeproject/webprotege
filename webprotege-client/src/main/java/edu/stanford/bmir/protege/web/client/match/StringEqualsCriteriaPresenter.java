package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.LiteralCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.StringEqualsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringEqualsCriteriaPresenter implements CriteriaPresenter<StringEqualsCriteria> {

    @Nonnull
    private final SimpleStringCriteriaView view;

    @Inject
    public StringEqualsCriteriaPresenter(@Nonnull SimpleStringCriteriaView view) {
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
    public Optional<StringEqualsCriteria> getCriteria() {
        return Optional.of(StringEqualsCriteria.get(view.getValue(), view.isIgnoreCase()));
    }

    @Override
    public void setCriteria(@Nonnull StringEqualsCriteria criteria) {
        view.setValue(criteria.getValue());
        view.setIgnoreCase(criteria.isIgnoreCase());
    }
}
