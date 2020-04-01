package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.matcher.ChangeMatcher;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
@ProjectSingleton
public class ReverseEngineeredChangeDescriptionGeneratorFactory {

    @Nonnull
    private Set<ChangeMatcher> changeMatchers;

    @Nonnull
    private OWLObjectStringFormatter formatter;

    @Inject
    public ReverseEngineeredChangeDescriptionGeneratorFactory(@Nonnull Set<ChangeMatcher> changeMatchers,
                                                              @Nonnull OWLObjectStringFormatter formatter) {
        this.changeMatchers = ImmutableSet.copyOf(changeMatchers);
        this.formatter = formatter;
    }

    public <S extends OWLEntity> ReverseEngineeredChangeDescriptionGenerator<S> get(String defaultDescription) {
        return new ReverseEngineeredChangeDescriptionGenerator<>(defaultDescription, changeMatchers, formatter);
    }
}
