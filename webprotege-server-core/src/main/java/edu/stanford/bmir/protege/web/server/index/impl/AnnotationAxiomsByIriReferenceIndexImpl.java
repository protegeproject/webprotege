package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAxiomsByIriReferenceIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorExAdapter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@ProjectSingleton
public class AnnotationAxiomsByIriReferenceIndexImpl implements AnnotationAxiomsByIriReferenceIndex, RequiresOntologyChangeNotification {

    @Nonnull
    private final AxiomMultimapIndex<IRI, OWLAnnotationAxiom> index;

    private final OWLAxiomVisitorExAdapter<Iterable<IRI>> keyExtractor = new OWLAxiomVisitorExAdapter<>(ImmutableList.of()) {

        // Not thread safe
        private final List<IRI> result = new ArrayList<>(2);

        @Nonnull
        @Override
        public Iterable<IRI> visit(OWLAnnotationAssertionAxiom axiom) {
            result.clear();
            if(axiom.getSubject() instanceof IRI) {
                result.add((IRI) axiom.getSubject());
            }
            if(axiom.getValue() instanceof IRI) {
                result.add((IRI) axiom.getValue());
            }
            return result;
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
    };

    @Inject
    public AnnotationAxiomsByIriReferenceIndexImpl() {
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLAnnotationAxiom.class,
                                                                   this::extractIris,
                                                                   MultimapBuilder.hashKeys().hashSetValues().build());
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

    public void applyChanges(@Nonnull List<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
