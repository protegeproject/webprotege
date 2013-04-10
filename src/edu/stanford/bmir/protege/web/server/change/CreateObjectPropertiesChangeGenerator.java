package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLObjectProperty> {

    public CreateObjectPropertiesChangeGenerator(Set<String> browserTexts, Optional<OWLObjectProperty> parent) {
        super(EntityType.OBJECT_PROPERTY, browserTexts, parent);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLObjectProperty freshEntity, OWLAPIProject project, ChangeGenerationContext context, Optional<OWLObjectProperty> parent) {
        if (parent.isPresent()) {
            OWLSubObjectPropertyOfAxiom ax = project.getDataFactory().getOWLSubObjectPropertyOfAxiom(freshEntity, parent.get());
            return Collections.<OWLAxiom>singleton(ax);
        }
        else {
            return Collections.emptySet();
        }
    }
}
