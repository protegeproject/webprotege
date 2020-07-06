package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.*;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
public class PropertyAssertionAxiomsBySubjectIndexImpl implements PropertyAssertionAxiomsBySubjectIndex, DependentIndex {

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject;

    @Nonnull
    private final ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubject;

    @Nonnull
    private final DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubject;

    @Inject
    public PropertyAssertionAxiomsBySubjectIndexImpl(@Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubject,
                                                     @Nonnull ObjectPropertyAssertionAxiomsBySubjectIndex objectPropertyAssertionAxiomsBySubject,
                                                     @Nonnull DataPropertyAssertionAxiomsBySubjectIndex dataPropertyAssertionAxiomsBySubject) {
        this.annotationAssertionAxiomsBySubject = checkNotNull(annotationAssertionAxiomsBySubject);
        this.objectPropertyAssertionAxiomsBySubject = checkNotNull(objectPropertyAssertionAxiomsBySubject);
        this.dataPropertyAssertionAxiomsBySubject = checkNotNull(dataPropertyAssertionAxiomsBySubject);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(annotationAssertionAxiomsBySubject,
                       objectPropertyAssertionAxiomsBySubject,
                       dataPropertyAssertionAxiomsBySubject);
    }

    @Nonnull
    @Override
    public Stream<OWLAxiom> getPropertyAssertions(@Nonnull OWLIndividual subject,
                                                  @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subject);
        checkNotNull(ontologyId);
        var annotationAssertions = getAnnotationAssertionAxioms(subject, ontologyId);
        var objectPropertyAssertions = objectPropertyAssertionAxiomsBySubject.getObjectPropertyAssertions(subject, ontologyId);
        var dataPropertyAssertions = dataPropertyAssertionAxiomsBySubject.getDataPropertyAssertions(subject, ontologyId);
        return Stream
                .of(annotationAssertions,
                    dataPropertyAssertions,
                    objectPropertyAssertions)
                .flatMap(ax -> ax);
    }

    private Stream<OWLAnnotationAssertionAxiom> getAnnotationAssertionAxioms(@Nonnull OWLIndividual subject,
                                                                             @Nonnull OWLOntologyID ontologyId) {
        OWLAnnotationSubject annotationSubject;
        if(subject instanceof OWLNamedIndividual) {
            annotationSubject = ((OWLNamedIndividual) subject).getIRI();
        }
        else {
            annotationSubject = (OWLAnonymousIndividual) subject;
        }
        return annotationAssertionAxiomsBySubject.getAxiomsForSubject(annotationSubject, ontologyId);
    }
}
