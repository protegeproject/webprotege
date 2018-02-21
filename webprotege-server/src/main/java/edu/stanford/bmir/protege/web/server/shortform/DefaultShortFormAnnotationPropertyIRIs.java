package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/01/15
 */
public enum DefaultShortFormAnnotationPropertyIRIs implements HasIRI {

    SKOS_PREF_LABEL(SKOSVocabulary.PREFLABEL.getIRI()),

    RDFS_LABEL(OWLRDFVocabulary.RDFS_LABEL.getIRI()),

    DC_TITLE(DublinCoreVocabulary.TITLE.getIRI()),

    DC_TERMS_TITLE(IRI.create("http://purl.org/dc/terms/title"));

    private final IRI iri;

    DefaultShortFormAnnotationPropertyIRIs(IRI iri) {
        this.iri = iri;
    }

    @Override
    public IRI getIRI() {
        return iri;
    }

    public static ImmutableList<IRI> asImmutableList() {
        ImmutableList.Builder<IRI> resultBuilder = ImmutableList.builder();
        for(DefaultShortFormAnnotationPropertyIRIs labellingIRI : values()) {
            resultBuilder.add(labellingIRI.getIRI());
        }
        return resultBuilder.build();
    }
}
