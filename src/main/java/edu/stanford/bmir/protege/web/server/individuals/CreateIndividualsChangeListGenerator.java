package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.server.change.AbstractCreateEntitiesChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.semanticweb.owlapi.model.EntityType.NAMED_INDIVIDUAL;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateIndividualsChangeListGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLNamedIndividual, OWLClass> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateIndividualsChangeListGenerator(@Nonnull Set<String> browserTexts,
                                                @Nonnull Optional<OWLClass> parent,
                                                @Nonnull OWLOntology rootOntology,
                                                @Nonnull OWLDataFactory dataFactory) {
        super(NAMED_INDIVIDUAL, browserTexts, parent, rootOntology, dataFactory);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLNamedIndividual freshEntity,
                                                        ChangeGenerationContext context,
                                                        Optional<OWLClass> parent) {
        if(parent.isPresent() && !parent.get().isOWLThing()) {
            return singleton(dataFactory.getOWLClassAssertionAxiom(parent.get(), freshEntity));
        }
        else {
            return emptySet();
        }
    }
}
