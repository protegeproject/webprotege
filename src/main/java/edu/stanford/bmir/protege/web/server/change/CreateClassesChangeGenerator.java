package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.EntityType.CLASS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLClass, OWLClass> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateClassesChangeGenerator(@Nonnull Set<String> browserTexts,
                                        @Nonnull Optional<OWLClass> parent,
                                        @Nonnull OWLOntology rootOntology,
                                        @Nonnull OWLDataFactory dataFactory) {
        super(CLASS, browserTexts, parent, rootOntology, dataFactory);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLClass freshEntity,
                                                        ChangeGenerationContext context,
                                                        Optional<OWLClass> parent) {
        if (parent.isPresent()) {
            OWLAxiom ax = dataFactory.getOWLSubClassOfAxiom(freshEntity, parent.get());
            return Collections.singleton(ax);
        }
        else {
            return Collections.emptySet();
        }
    }
}
