package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isDictionaryAnnotationAssertion;
import static org.semanticweb.owlapi.model.AxiomType.ANNOTATION_ASSERTION;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryCacheBuilder {

    @Nonnull
    private final OWLOntology rootOntology;

    public DictionaryCacheBuilder(@Nonnull OWLOntology rootOntology) {
        this.rootOntology = checkNotNull(rootOntology);
    }

    /**
     * Build a dictionary cache for the specified annotation property IRI and the specified lang.
     * @param annotationPropertyIri The annotation property IRI
     * @param lang The lang
     * @return A {@link DictionaryCache} that maps IRIs to short forms based on annotations that
     *         annotate IRIs with the specified annotation IRI and lang.
     */
    @Nonnull
    public DictionaryCache build(@Nonnull IRI annotationPropertyIri,
                                 @Nonnull String lang) {
        DictionaryCache dictionaryCache = DictionaryCache.create(annotationPropertyIri, lang);
        rootOntology.getImportsClosure().stream()
                    .flatMap(ont -> ont.getAxioms(ANNOTATION_ASSERTION).stream())
                    .filter(ax -> isDictionaryAnnotationAssertion(dictionaryCache, ax))
                    .forEach(ax -> {
                        IRI iri = (IRI) ax.getSubject();
                        String literal = ((OWLLiteral) ax.getValue()).getLiteral();
                        dictionaryCache.put(iri, literal);
                    });
        return dictionaryCache;
    }
}
