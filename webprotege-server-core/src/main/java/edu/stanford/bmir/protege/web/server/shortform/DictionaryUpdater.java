package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

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
        Set<OWLEntity> affectedEntities = changes.stream()
                                                 .filter(OWLOntologyChange::isAxiomChange)
                                                 .map(OWLOntologyChange::getAxiom)
                                                 .filter(ax -> ax instanceof OWLAnnotationAssertionAxiom)
                                                 .map(ax -> (OWLAnnotationAssertionAxiom) ax)
                                                 .filter(ax -> isAxiomForDictionary(ax, dictionary))
                                                 .map(ax -> (IRI) ax.getSubject())
                                                 .flatMap(iri -> rootOntology.getEntitiesInSignature(iri, Imports.INCLUDED).stream())
                                                 .collect(toSet());
        affectedEntities.forEach(dictionary::remove);
        affectedEntities.forEach(entity -> {
            rootOntology.getAnnotationAssertionAxioms(entity.getIRI()).stream()
                        .filter(ax -> isAxiomForDictionary(ax, dictionary))
                        .forEach(ax -> {
                            OWLLiteral literal = (OWLLiteral) ax.getValue();
                            String lexicalValue = literal.getLiteral();
                            dictionary.put(entity, lexicalValue);
                        });
        });
    }


}
