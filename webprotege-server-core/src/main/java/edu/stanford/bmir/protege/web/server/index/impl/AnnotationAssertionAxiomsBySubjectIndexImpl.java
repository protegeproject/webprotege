package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
public class AnnotationAssertionAxiomsBySubjectIndexImpl implements AnnotationAssertionAxiomsBySubjectIndex, RequiresOntologyChangeNotification {

    @Nonnull
    private final AxiomMultimapIndex<OWLAnnotationSubject, OWLAnnotationAssertionAxiom> index;

    @Inject
    public AnnotationAssertionAxiomsBySubjectIndexImpl() {
        index = AxiomMultimapIndex.create(OWLAnnotationAssertionAxiom.class,
                                          OWLAnnotationAssertionAxiom::getSubject,
                                          MultimapBuilder.hashKeys()
                                                         .arrayListValues()
                                                         .build());
    }

    @Override
    public Stream<OWLAnnotationAssertionAxiom> getAxiomsForSubject(@Nonnull OWLAnnotationSubject subject,
                                                                   @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subject);
        checkNotNull(ontologyId);
        return index.getAxioms(subject, ontologyId);
    }

    @Override
    public void applyChanges(@Nonnull List<OntologyChange> changes) {
        index.applyChanges(changes);
    }
}
