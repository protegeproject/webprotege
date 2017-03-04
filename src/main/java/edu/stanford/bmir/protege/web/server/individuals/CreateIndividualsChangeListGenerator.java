package edu.stanford.bmir.protege.web.server.individuals;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.change.AbstractCreateEntitiesChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateIndividualsChangeListGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLNamedIndividual, OWLClass> {

    public CreateIndividualsChangeListGenerator(Set<String> browserTexts, Optional<OWLClass> parent) {
        super(EntityType.NAMED_INDIVIDUAL, browserTexts, parent);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLNamedIndividual freshEntity, OWLAPIProject project, ChangeGenerationContext context, Optional<OWLClass> parent) {
        if(parent.isPresent() && !parent.get().isOWLThing()) {
            return Collections.<OWLAxiom>singleton(project.getDataFactory().getOWLClassAssertionAxiom(parent.get(), freshEntity));
        }
        else {
            return Collections.emptySet();
        }
    }
}
