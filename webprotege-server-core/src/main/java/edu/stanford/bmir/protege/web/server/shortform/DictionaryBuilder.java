package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isAxiomForDictionary;
import static java.util.Collections.enumeration;
import static java.util.Collections.singletonList;
import static org.semanticweb.owlapi.model.AxiomType.ANNOTATION_ASSERTION;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryBuilder.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public DictionaryBuilder(@Nonnull ProjectId projectId,
                             @Nonnull OWLOntology rootOntology) {
        this.projectId = checkNotNull(projectId);
        this.rootOntology = checkNotNull(rootOntology);
    }

    /**
     * Builds a dictionary.
     */
    public void build(@Nonnull Dictionary dictionary) {
        buildAll(singletonList(dictionary));
    }

    /**
     * Builds the specified dictionaries
     */
    public void buildAll(List<Dictionary> dictionaries) {
        List<Dictionary> annotationBasedDictionaries = new ArrayList<>();
        Dictionary localNameDictionary = null;
        for (Dictionary dictionary : dictionaries) {
            if (dictionary.getLanguage().isAnnotationBased()) {
                annotationBasedDictionaries.add(dictionary);
            }
            else {
                localNameDictionary = dictionary;
            }
        }
        buildAnnotationBasedDictionaries(annotationBasedDictionaries);
        if (localNameDictionary != null) {
            buildLocalNameDictionary(localNameDictionary);
        }
    }

    private void buildLocalNameDictionary(Dictionary localNameDictionary) {
        LocalNameExtractor extractor = new LocalNameExtractor();
        rootOntology.getSignature(Imports.INCLUDED)
                    .forEach(entity -> {
                        String shortForm = extractor.getLocalName(entity.getIRI());
                        if (!shortForm.isEmpty()) {
                            localNameDictionary.put(entity, shortForm);
                        }
                    });
    }

    private void buildAnnotationBasedDictionaries(List<Dictionary> annotationBasedDictionaries) {
        if (annotationBasedDictionaries.isEmpty()) {
            return;
        }
        Counter counter = new Counter();
        rootOntology.getImportsClosure().stream()
                    .flatMap(ont -> ont.getAxioms(ANNOTATION_ASSERTION).stream())
                    .peek(counter::increment)
                    .forEach(ax -> {
                        annotationBasedDictionaries.stream()
                                                   .filter(dictionary -> isAxiomForDictionary(ax, dictionary))
                                                   .forEach(dictionary -> {
                                                       IRI iri = (IRI) ax.getSubject();
                                                       String literal = ((OWLLiteral) ax.getValue()).getLiteral();
                                                       rootOntology.getEntitiesInSignature(iri, Imports.INCLUDED).forEach(entity -> {
                                                           dictionary.put(entity, literal);
                                                       });
                                                   });
                    });
        logger.info("{} Processed {} axioms in order to build annotation based dictionaries",
                    projectId,
                    String.format("%,d", counter.getCounter()));
    }
}
