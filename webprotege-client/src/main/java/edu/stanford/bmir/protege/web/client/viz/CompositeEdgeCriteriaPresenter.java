package edu.stanford.bmir.protege.web.client.viz;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.match.criteria.MultiMatchType;
import edu.stanford.bmir.protege.web.shared.viz.CompositeEdgeCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class CompositeEdgeCriteriaPresenter implements CriteriaPresenter<CompositeEdgeCriteria> {

    @Nonnull
    private final EdgeCriteriaListPresenter delegate;

    @Inject
    public CompositeEdgeCriteriaPresenter(@Nonnull EdgeCriteriaListPresenter delegate) {
        this.delegate = delegate;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        delegate.start(container);
        delegate.setMatchTextPrefix("");
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public Optional<? extends CompositeEdgeCriteria> getCriteria() {
        return delegate.getCriteria()
                .map(criteria -> (CompositeEdgeCriteria) criteria);
    }

    @Override
    public void setCriteria(@Nonnull CompositeEdgeCriteria criteria) {
        delegate.setCriteria(criteria);
    }
}
