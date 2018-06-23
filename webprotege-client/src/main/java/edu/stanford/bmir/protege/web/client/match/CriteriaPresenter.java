package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
public interface CriteriaPresenter<C extends Criteria> {

    void start(@Nonnull AcceptsOneWidget container);

    void stop();

    Optional<? extends C> getCriteria();

    void setCriteria(@Nonnull C criteria);
}
