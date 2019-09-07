package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.SubClassOfAxiomsBySubClassIndex;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
public class SubClassOfAxiomsBySubClassIndexImpl implements SubClassOfAxiomsBySubClassIndex, RequiresOntologyChangeNotification {

    @Nonnull
    private final AxiomMultimapIndex<OWLClass, OWLSubClassOfAxiom> index;

    @Inject
    public SubClassOfAxiomsBySubClassIndexImpl() {
        index = new AxiomMultimapIndex<>(MultimapBuilder.hashKeys()
                                                        .arrayListValues(1)
                                                        .build(),
                                         this::extractSubClassFromAxiom,
                                         OWLSubClassOfAxiom.class);

    }

    @Nullable
    private OWLClass extractSubClassFromAxiom(@Nonnull OWLSubClassOfAxiom ax) {
        var subClass = ax.getSubClass();
        if(subClass.isNamed()) {
            return subClass.asOWLClass();
        }
        else {
            return null;
        }
    }

    @Override
    public Stream<OWLSubClassOfAxiom> getSubClassOfAxiomsForSubClass(@Nonnull OWLClass subClass,
                                                                     @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(subClass);
        checkNotNull(ontologyId);
        return index.getAxioms(subClass, ontologyId);
    }

    @Override
    public void handleOntologyChanges(@Nonnull List<OntologyChange> changes) {
        index.handleOntologyChanges(changes);
    }
}
