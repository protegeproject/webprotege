package edu.stanford.bmir.protege.web.shared.shortform;

import com.google.common.collect.ImmutableMap;
import com.mongodb.BasicDBObject;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.Namespaces;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Aug 2018
 */
public enum WellKnownLabellingIris {

    RDFS_LABEL(OWLRDFVocabulary.RDFS_LABEL.getIRI(), OWLRDFVocabulary.RDFS_LABEL.getPrefixedName()),

    SKOS_PREF_LABEL(SKOSVocabulary.PREFLABEL.getIRI(), SKOSVocabulary.PREFLABEL.getPrefixedName()),

    DC_TERMS_TITLE(IRI.create("http://purl.org/dc/terms/title"), "dcterms:title"),

    DC_ELEMENTS_TITLE(DublinCoreVocabulary.TITLE.getIRI(), DublinCoreVocabulary.TITLE.getPrefixedName()),

    SKOS_ALT_LABEL(SKOSVocabulary.ALTLABEL.getIRI(), SKOSVocabulary.ALTLABEL.getPrefixedName()),

    SKOS_HIDDEN_LABEL(SKOSVocabulary.HIDDENLABEL.getIRI(), SKOSVocabulary.HIDDENLABEL.getPrefixedName()),

    SCHEMA_NAME(IRI.create(Namespaces.SCHEMA.getPrefixIRI() + "name"), Namespaces.SCHEMA.getPrefixName() + ":name"),

    SCHEMA_IDENTIFIER(IRI.create(Namespaces.SCHEMA.getPrefixIRI() + "identifier"), Namespaces.SCHEMA.getPrefixName() + ":identifier"),

    SCHEMA_ALTERNATE_NAME(IRI.create(Namespaces.SCHEMA.getPrefixIRI() + "alternateName"), Namespaces.SCHEMA.getPrefixName() + ":alternateName");

    private final IRI iri;

    private final String prefixedName;


    static final ImmutableMap<IRI, WellKnownLabellingIris> BY_IRI;

    static {
        ImmutableMap.Builder<IRI, WellKnownLabellingIris> builder = ImmutableMap.builder();
        for(WellKnownLabellingIris l : values()) {
            builder.put(l.iri, l);
        }
        BY_IRI = builder.build();
    }

    WellKnownLabellingIris(IRI iri, String prefixedName) {
        this.iri = iri;
        this.prefixedName = prefixedName;
    }

    public IRI getIri() {
        return iri;
    }

    @Nonnull
    public String getPrefixedName() {
        return prefixedName;
    }

    @Nonnull
    public static Optional<WellKnownLabellingIris> get(@Nonnull IRI iri) {
        return Optional.ofNullable(BY_IRI.get(iri));
    }

    public static boolean isWellKnownLabellingIri(@Nonnull IRI iri) {
        return BY_IRI.containsKey(iri);
    }
}
