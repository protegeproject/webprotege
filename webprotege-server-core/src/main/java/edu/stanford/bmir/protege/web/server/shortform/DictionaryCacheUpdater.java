package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isDictionaryAnnotationAssertion;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryCacheUpdater {

    @Nonnull
    private final OWLOntology rootOntology;

    public DictionaryCacheUpdater(@Nonnull OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }

    public void update(@Nonnull DictionaryCache dictionaryCache,
                       @Nonnull List<? extends OWLOntologyChange> changes) {
        Set<IRI> affectedIris = changes.stream()
                                       .filter(OWLOntologyChange::isAxiomChange)
                                       .map(OWLOntologyChange::getAxiom)
                                       .filter(ax -> ax instanceof OWLAnnotationAssertionAxiom)
                                       .map(ax -> (OWLAnnotationAssertionAxiom) ax)
                                       .filter(ax -> isDictionaryAnnotationAssertion(dictionaryCache, ax))
                                       .map(ax -> (IRI) ax.getSubject())
                                       .collect(toSet());
        affectedIris.forEach(dictionaryCache::remove);
        affectedIris.forEach(iri -> {
            rootOntology.getAnnotationAssertionAxioms(iri).stream()
                        .filter(ax -> isDictionaryAnnotationAssertion(dictionaryCache, ax))
                        .forEach(ax -> {
                            OWLLiteral literal = (OWLLiteral) ax.getValue();
                            String lexicalValue = literal.getLiteral();
                            dictionaryCache.put(iri, lexicalValue);
                        });
        });
    }


}
