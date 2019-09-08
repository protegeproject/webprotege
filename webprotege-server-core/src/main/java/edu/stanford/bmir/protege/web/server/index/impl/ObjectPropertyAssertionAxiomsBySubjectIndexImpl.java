package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.ObjectPropertyAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-12
 */
public class ObjectPropertyAssertionAxiomsBySubjectIndexImpl implements ObjectPropertyAssertionAxiomsBySubjectIndex, RequiresOntologyChangeNotification {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLObjectPropertyAssertionAxiom> index;

    @Inject
    public ObjectPropertyAssertionAxiomsBySubjectIndexImpl() {
        this.index = AxiomMultimapIndex.create(OWLObjectPropertyAssertionAxiom.class,
                                               OWLPropertyAssertionAxiom::getSubject,
                                               MultimapBuilder.hashKeys()
                                                              .arrayListValues()
                                                              .build()
        );
    }

    private OWLIndividual extractSubject(@Nonnull OWLObjectPropertyAssertionAxiom axiom) {
        return axiom.getObject();
    }

    @Override
    public void handleOntologyChanges(@Nonnull List<OntologyChange> changes) {
        index.handleOntologyChanges(changes);
    }

    @Nonnull
    @Override
    public Stream<OWLObjectPropertyAssertionAxiom> getObjectPropertyAssertions(@Nonnull OWLIndividual subject,
                                                                               @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subject);
        checkNotNull(ontologyId);
        return index.getAxioms(subject, ontologyId);
    }
}
