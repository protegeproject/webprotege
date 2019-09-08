package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByClassIndex;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
public class ClassAssertionAxiomsByClassIndexImpl implements ClassAssertionAxiomsByClassIndex, RequiresOntologyChangeNotification {

    @Nonnull
    private final AxiomMultimapIndex<OWLClassExpression, OWLClassAssertionAxiom> index;

    @Inject
    public ClassAssertionAxiomsByClassIndexImpl() {
        index = AxiomMultimapIndex.create(OWLClassAssertionAxiom.class,
                                          OWLClassAssertionAxiom::getClassExpression,
                                          MultimapBuilder.hashKeys()
                                                         .arrayListValues()
                                                         .build());
    }

    @Nonnull
    @Override
    public Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLClass cls,
                                                                  @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(cls);
        return index.getAxioms(cls, ontologyId);
    }

    @Override
    public void handleOntologyChanges(@Nonnull List<OntologyChange> changes) {
        index.handleOntologyChanges(changes);
    }
}
