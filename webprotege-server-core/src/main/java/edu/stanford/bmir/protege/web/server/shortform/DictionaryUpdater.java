package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isAxiomForDictionary;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryUpdater {

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public DictionaryUpdater(@Nonnull OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }

    public void update(@Nonnull Dictionary dictionary,
                       @Nonnull List<? extends OWLOntologyChange> changes) {
        Set<IRI> affectedIris = changes.stream()
                                       .filter(OWLOntologyChange::isAxiomChange)
                                       .map(OWLOntologyChange::getAxiom)
                                       .filter(ax -> ax instanceof OWLAnnotationAssertionAxiom)
                                       .map(ax -> (OWLAnnotationAssertionAxiom) ax)
                                       .filter(ax -> isAxiomForDictionary(ax, dictionary))
                                       .map(ax -> (IRI) ax.getSubject())
                                       .collect(toSet());
        affectedIris.forEach(dictionary::remove);
        affectedIris.forEach(iri -> {
            rootOntology.getAnnotationAssertionAxioms(iri).stream()
                        .filter(ax -> isAxiomForDictionary(ax, dictionary))
                        .forEach(ax -> {
                            OWLLiteral literal = (OWLLiteral) ax.getValue();
                            String lexicalValue = literal.getLiteral();
                            dictionary.put(iri, lexicalValue);
                        });
        });
    }


}
