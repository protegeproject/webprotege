package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isAxiomForDictionary;
import static java.util.Collections.singletonList;
import static org.semanticweb.owlapi.model.AxiomType.ANNOTATION_ASSERTION;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryBuilder {

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public DictionaryBuilder(@Nonnull OWLOntology rootOntology) {
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
        for(Dictionary dictionary : dictionaries) {
            if(dictionary.getLanguage().isAnnotationBased()) {
                annotationBasedDictionaries.add(dictionary);
            }
            else {
                localNameDictionary = dictionary;
            }
        }
        buildAnnotationBasedDictionaries(annotationBasedDictionaries);
        if(localNameDictionary != null) {
            buildLocalNameDictionary(localNameDictionary);
        }
    }

    private void buildLocalNameDictionary(Dictionary localNameDictionary) {
        LocalNameExtractor extractor = new LocalNameExtractor();
        rootOntology.getSignature(Imports.INCLUDED).stream()
                    .map(OWLNamedObject::getIRI)
                    .forEach(iri -> {
                        String shortForm = extractor.getLocalName(iri);
                        if(!shortForm.isEmpty()) {
                            localNameDictionary.put(iri, shortForm);
                        }
                    });
    }

    private void buildAnnotationBasedDictionaries(List<Dictionary> annotationBasedDictionaries) {
        if(annotationBasedDictionaries.isEmpty()) {
            return;
        }
        rootOntology.getImportsClosure().stream()
                    .flatMap(ont -> ont.getAxioms(ANNOTATION_ASSERTION).stream())
                    .forEach(ax -> {
                        annotationBasedDictionaries.stream()
                                    .filter(dictionary -> isAxiomForDictionary(ax, dictionary))
                                    .forEach(dictionary -> {
                                        IRI iri = (IRI) ax.getSubject();
                                        String literal = ((OWLLiteral) ax.getValue()).getLiteral();
                                        dictionary.put(iri, literal);
                                    });
                    });
    }
}
