package edu.stanford.bmir.protege.web.server.individuals;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.change.AbstractCreateEntitiesChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
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
@AutoFactory
public class CreateIndividualsChangeListGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLNamedIndividual, OWLClass> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateIndividualsChangeListGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                                @Provided @Nonnull MessageFormatter msg,
                                                @Provided @Nonnull OWLOntology rootOntology,
                                                @Nonnull Optional<OWLClass> parent,
                                                @Nonnull String sourceText) {
        super(NAMED_INDIVIDUAL, sourceText, parent, rootOntology, dataFactory, msg);
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
