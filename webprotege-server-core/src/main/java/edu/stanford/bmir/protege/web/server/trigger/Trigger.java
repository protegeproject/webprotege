package edu.stanford.bmir.protege.web.server.trigger;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class Trigger {

    @Nonnull
    private final Matcher<OWLEntity> matcher;

    @Nonnull
    private final ImmutableList<TriggerAction> triggerActions;

    public Trigger(@Nonnull Matcher<OWLEntity> matcher,
                   @Nonnull ImmutableList<TriggerAction> triggerActions) {
        this.matcher = checkNotNull(matcher);
        this.triggerActions = checkNotNull(triggerActions);
    }

    @Nonnull
    public Matcher<OWLEntity> getMatcher() {
        return matcher;
    }

    @Nonnull
    public ImmutableList<TriggerAction> getTriggerActions() {
        return triggerActions;
    }
}
