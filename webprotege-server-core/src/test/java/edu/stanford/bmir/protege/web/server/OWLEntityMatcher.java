package edu.stanford.bmir.protege.web.server;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.any;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/04/2014
 */
public class OWLEntityMatcher extends TypeSafeMatcher<OWLEntity> {

    private Matcher<? extends EntityType> entityType;

    private Matcher<IRI> iriMatcher;


    public OWLEntityMatcher(Matcher<? extends EntityType> entityType, Matcher<IRI> iriMatcher) {
        this.entityType = entityType;
        this.iriMatcher = iriMatcher;
    }

    @Override
    protected boolean matchesSafely(OWLEntity item) {
        return entityType.matches(item.getEntityType()) && iriMatcher.matches(item.getIRI());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matched entity type and IRI");
    }

    private static OWLEntityMatcher create(EntityType entityType, OWLRDFVocabulary vocabulary) {
        return new OWLEntityMatcher(is(entityType), is(vocabulary.getIRI()));
    }

    public static OWLEntityMatcher owlThing() {
        return create(EntityType.CLASS, OWLRDFVocabulary.OWL_THING);
    }

    public static OWLEntityMatcher owlNothing() {
        return create(EntityType.CLASS, OWLRDFVocabulary.OWL_THING);
    }

    public static OWLEntityMatcher rdfsComment() {
        return create(EntityType.ANNOTATION_PROPERTY, OWLRDFVocabulary.RDFS_COMMENT);
    }


    private static OWLEntityMatcher create(EntityType entityType, SKOSVocabulary vocabulary) {
        return new OWLEntityMatcher(is(entityType), is(vocabulary.getIRI()));
    }

    public static OWLEntityMatcher skosPrefLabel() {
        return create(EntityType.ANNOTATION_PROPERTY, SKOSVocabulary.PREFLABEL);
    }

    public static OWLEntityMatcher skosAltLabel() {
        return create(EntityType.ANNOTATION_PROPERTY, SKOSVocabulary.ALTLABEL);
    }

    public static OWLEntityMatcher annotationProperty() {
        return new OWLEntityMatcher(is(EntityType.ANNOTATION_PROPERTY), any(IRI.class));
    }

    public static OWLEntityMatcher entityWithIRI(String iri) {
        return new OWLEntityMatcher(any(EntityType.class), is(IRI.create(iri)));
    }

    public static OWLEntityMatcher entityWithIRI(IRI iri) {
        return new OWLEntityMatcher(any(EntityType.class), is(iri));
    }

    public static OWLEntityMatcher hasIRI(IRI iri) {
        return new OWLEntityMatcher(any(EntityType.class), is(iri));
    }

    public static OWLEntityMatcher hasIRI(String iri) {
        return new OWLEntityMatcher(any(EntityType.class), is(IRI.create(iri)));
    }
}
