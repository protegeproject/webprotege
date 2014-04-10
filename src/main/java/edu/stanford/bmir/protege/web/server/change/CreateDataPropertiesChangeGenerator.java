package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateDataPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLDataProperty, OWLDataProperty> {

    public CreateDataPropertiesChangeGenerator(Set<String> browserTexts, Optional<OWLDataProperty> parent) {
        super(EntityType.DATA_PROPERTY, browserTexts, parent);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLDataProperty freshEntity, OWLAPIProject project, ChangeGenerationContext context, Optional<OWLDataProperty> parent) {
        if (parent.isPresent()) {
            OWLAxiom ax = project.getDataFactory().getOWLSubDataPropertyOfAxiom(freshEntity, parent.get());
            return Collections.singleton(ax);
        }
        else {
            return Collections.emptySet();
        }
    }
}
