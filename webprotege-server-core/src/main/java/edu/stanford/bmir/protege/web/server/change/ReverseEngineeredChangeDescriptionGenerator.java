package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.matcher.ChangeMatcher;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class ReverseEngineeredChangeDescriptionGenerator<S> implements ChangeDescriptionGenerator<S> {

    @Nonnull
    private final String defaultDescription;

    @Nonnull
    private final List<ChangeMatcher> matchers;

    @Inject
    public ReverseEngineeredChangeDescriptionGenerator(@Nonnull String defaultDescription,
                                                       @Nonnull Set<ChangeMatcher> matchers) {
        this.defaultDescription = defaultDescription;
        this.matchers = new ArrayList<>(matchers);
    }

    @Override
    public String generateChangeDescription(ChangeApplicationResult<S> result) {
        var changeData = result.getChangeList().stream().map(OWLOntologyChange::getChangeData).collect(toImmutableList());
        for(ChangeMatcher matcher : matchers) {
            Optional<String> description = matcher.getDescription(changeData);
            if(description.isPresent()) {
                return description.get();
            }
        }
        return defaultDescription;
    }
}
