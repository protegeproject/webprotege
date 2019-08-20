package edu.stanford.bmir.protege.web.server.trigger;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class TriggerRunner {

    @Nonnull
    private final ImmutableList<Trigger> triggers;

    @Nonnull
    private final List<List<Object>> contexts = new ArrayList<>();

    public TriggerRunner(@Nonnull ImmutableList<Trigger> triggers) {
        this.triggers = checkNotNull(triggers);
    }

    public void execute(@Nonnull Stream<OWLEntity> entities) {
        checkNotNull(entities);
        begin();
        entities.forEach(this::execute);
        end();
    }

    private void begin() {
        contexts.clear();
        triggers.forEach(trigger -> {
            List<Object> triggerContexts = new ArrayList<>();
            trigger.getTriggerActions().forEach(triggerAction -> {
                Object context = triggerAction.begin();
                triggerContexts.add(context);
            });
            contexts.add(triggerContexts);
        });
    }

    @SuppressWarnings("unchecked")
    private void execute(@Nonnull OWLEntity entity) {
        // Unchecked because we ask the trigger to create the context
        // and it handles the same type of context in the begin, execute,
        // and end methods.
        checkContextsSize();
        for (int i = 0; i < triggers.size(); i++) {
            Trigger trigger = triggers.get(i);
            Matcher<OWLEntity> matcher = trigger.getMatcher();
            if (matcher.matches(checkNotNull(entity))) {
                List<TriggerAction> triggerActions = trigger.getTriggerActions();
                List<Object> triggerContexts = contexts.get(i);
                for (int j = 0; j < triggerActions.size(); j++) {
                    TriggerAction triggerAction = triggerActions.get(j);
                    Object context = triggerContexts.get(j);
                    triggerAction.execute(entity, context);
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    private void end() {
        // Unchecked because we ask the trigger to create the context
        // and it handles the same type of context in the begin, execute,
        // and end methods.
        checkContextsSize();
        for (int i = 0; i < triggers.size(); i++) {
            Trigger trigger = triggers.get(i);
            List<TriggerAction> triggerActions = trigger.getTriggerActions();
            List<Object> triggerContexts = contexts.get(i);
            for (int j = 0; j < triggerActions.size(); j++) {
                TriggerAction triggerAction = triggerActions.get(j);
                Object context = triggerContexts.get(j);
                triggerAction.end(context);
            }
        }

    }

    private void checkContextsSize() {
        if (contexts.size() != triggers.size()) {
            throw new IllegalStateException("begin has not been called");
        }
    }
}
