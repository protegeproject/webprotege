package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAxiomsByIriReferenceIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@ProjectSingleton
public class AnnotationAxiomsByIriReferenceIndexImpl implements AnnotationAxiomsByIriReferenceIndex, UpdatableIndex {

    @Nonnull
    private final AxiomMultimapIndex<IRI, OWLAnnotationAxiom> index;

    private final KeyExtractorVisitor keyExtractor = new KeyExtractorVisitor();

    @Inject
    public AnnotationAxiomsByIriReferenceIndexImpl() {
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLAnnotationAxiom.class,
                                                                   this::extractIris);
        index.setLazy(true);
    }

    @Override
    public Stream<OWLAnnotationAxiom> getReferencingAxioms(@Nonnull IRI iri, @Nonnull OWLOntologyID ontologyID) {
        return index.getAxioms(iri, ontologyID);
    }

    private Iterable<IRI> extractIris(OWLAnnotationAxiom axiom) {
        var irisInAxiomAnnotations = new ArrayList<IRI>(4);
        getAnnotationValues(axiom, irisInAxiomAnnotations);
        var axiomIris = axiom.accept(keyExtractor);
        if(irisInAxiomAnnotations.isEmpty()) {
            return axiomIris;
        }
        else {
            return Iterables.concat(axiomIris, irisInAxiomAnnotations);
        }
    }

    private void getAnnotationValues(@Nonnull HasAnnotations hasAnnotations,
                                    @Nonnull Collection<IRI> result) {
        var annotations = getAnnotationsFromHasAnnotations(hasAnnotations);
        if(annotations.isEmpty()) {
            return;
        }
        for(var annotation : annotations) {
            if(annotation.getValue() instanceof IRI) {
                result.add((IRI) annotation.getValue());
            }
            getAnnotationValues(annotation, result);
        }
    }

    private Collection<OWLAnnotation> getAnnotationsFromHasAnnotations(@Nonnull HasAnnotations hasAnnotations) {
        return hasAnnotations.getAnnotations();
    }

    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        index.applyChanges(changes);
    }

    private static class KeyExtractorVisitor extends OWLAxiomVisitorExAdapter<Iterable<IRI>> {

        private final AssertionIrisIterable assertionIrisIterable = new AssertionIrisIterable();

        public KeyExtractorVisitor() {
            super(Collections.emptyList());
        }

        @Nonnull
        @Override
        public Iterable<IRI> visit(OWLAnnotationAssertionAxiom axiom) {
            assertionIrisIterable.reset();
            if(axiom.getSubject() instanceof IRI) {
                assertionIrisIterable.put((IRI) axiom.getSubject());
            }
            if(axiom.getValue() instanceof IRI) {
                assertionIrisIterable.put((IRI) axiom.getValue());
            }
            return assertionIrisIterable;
        }

        @Nonnull
        @Override
        public Iterable<IRI> visit(OWLAnnotationPropertyDomainAxiom axiom) {
            return ImmutableList.of(axiom.getDomain());
        }

        @Nonnull
        @Override
        public Iterable<IRI> visit(OWLAnnotationPropertyRangeAxiom axiom) {
            return ImmutableList.of(axiom.getRange());
        }

    }


    private static class AssertionIrisIterable implements Iterable<IRI>, Iterator<IRI> {

        // Not thread safe
        private final IRI [] subjectValueIris = new IRI [2];

        private int index;

        private int size = 0;

        public void reset() {
            size  = 0;
            index = 0;
        }

        public void put(@Nonnull IRI iri) {
            subjectValueIris[size] = iri;
            size++;
        }

        @Nonnull
        @Override
        public Iterator<IRI> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public IRI next() {
            var iri = subjectValueIris[index];
            index++;
            return iri;
        }
    }
}
