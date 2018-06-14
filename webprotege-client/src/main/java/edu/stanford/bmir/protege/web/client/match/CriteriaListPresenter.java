package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 *
 * Presents a list of criteria, delegating to a list of {@link CriteriaPresenter}s
 * to do this
 */
public class CriteriaListPresenter implements CriteriaPresenter {

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {

    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<Criteria> getCriteria() {
        return Optional.empty();
    }
}
