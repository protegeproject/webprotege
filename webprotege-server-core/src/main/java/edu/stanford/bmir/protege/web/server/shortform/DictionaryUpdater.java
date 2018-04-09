package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isAxiomForDictionary;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryUpdater {

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final LocalNameExtractor extractor = new LocalNameExtractor();

    @Inject
    public DictionaryUpdater(@Nonnull OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }

    public void update(@Nonnull Dictionary dictionary,
                       @Nonnull Collection<OWLEntity> entities) {
        checkNotNull(entities).forEach(entity -> {
            dictionary.remove(entity);
            if (dictionary.getLanguage().isAnnotationBased()) {
                rootOntology.getImportsClosure().stream()
                            .flatMap(ont -> ont.getAnnotationAssertionAxioms(entity.getIRI()).stream())
                            .filter(ax -> isAxiomForDictionary(ax, dictionary))
                            .forEach(ax -> {
                                OWLLiteral literal = (OWLLiteral) ax.getValue();
                                String lexicalValue = literal.getLiteral();
                                dictionary.put(entity, lexicalValue);
                            });
            }
            else {
                String localName = extractor.getLocalName(entity.getIRI());
                dictionary.put(entity, localName);
            }
        });

    }


}
