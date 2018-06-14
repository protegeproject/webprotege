package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class IsDeprecatedCriteriaPresenter implements CriteriaPresenter<EntityMatchCriteria> {

    @Inject
    public IsDeprecatedCriteriaPresenter() {
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(new Label("Is Deprecated View"));
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<EntityIsDeprecatedCriteria> getCriteria() {
        return Optional.of(EntityIsDeprecatedCriteria.get());
    }
}
