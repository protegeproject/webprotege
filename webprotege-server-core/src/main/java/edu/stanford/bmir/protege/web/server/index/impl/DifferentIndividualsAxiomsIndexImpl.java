package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.DifferentIndividualsAxiomsIndex;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-24
 */
public class DifferentIndividualsAxiomsIndexImpl implements DifferentIndividualsAxiomsIndex, RequiresOntologyChangeNotification {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLDifferentIndividualsAxiom> index;

    @Inject
    public DifferentIndividualsAxiomsIndexImpl() {
        this.index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(
                OWLDifferentIndividualsAxiom.class,
                OWLDifferentIndividualsAxiom::getIndividuals,
                MultimapBuilder.hashKeys().arrayListValues().build()
        );
    }

    @Nonnull
    @Override
    public Stream<OWLDifferentIndividualsAxiom> getDifferentIndividualsAxioms(@Nonnull OWLIndividual individual,
                                                                              @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(individual);
        return index.getAxioms(individual, ontologyId);
    }

    @Override
    public void applyChanges(@Nonnull List<OntologyChange> changes) {
        index.handleOntologyChanges(changes);
    }
}
