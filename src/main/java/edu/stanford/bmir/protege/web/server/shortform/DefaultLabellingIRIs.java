package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/01/15
 */
public enum DefaultLabellingIRIs implements HasIRI {

    SKOS_PREF_LABEL(SKOSVocabulary.PREFLABEL.getIRI()),

    RDFS_PREF_LABEL(OWLRDFVocabulary.RDFS_LABEL.getIRI());

    private final IRI iri;

    DefaultLabellingIRIs(IRI iri) {
        this.iri = iri;
    }

    @Override
    public IRI getIRI() {
        return iri;
    }

    public static ImmutableList<IRI> asImmutableList() {
        ImmutableList.Builder<IRI> resultBuilder = ImmutableList.builder();
        for(DefaultLabellingIRIs labellingIRI : values()) {
            resultBuilder.add(labellingIRI.getIRI());
        }
        return resultBuilder.build();
    }
}
