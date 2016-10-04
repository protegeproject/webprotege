package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.matcher.ChangeMatcher;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class ReverseEngineeredChangeDescriptionGeneratorFactory {

    @Nonnull
    private Set<ChangeMatcher> changeMatchers;

    @Inject
    public ReverseEngineeredChangeDescriptionGeneratorFactory(@Nonnull Set<ChangeMatcher> changeMatchers) {
        this.changeMatchers = ImmutableSet.copyOf(changeMatchers);
    }

    public ReverseEngineeredChangeDescriptionGenerator get(String defaultDescription) {
        return new ReverseEngineeredChangeDescriptionGenerator(defaultDescription, changeMatchers);
    }
}
