package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.project.Project;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateAnnotationPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLAnnotationProperty, OWLAnnotationProperty> {

    public CreateAnnotationPropertiesChangeGenerator(Set<String> browserTexts, Optional<OWLAnnotationProperty> parent) {
        super(EntityType.ANNOTATION_PROPERTY, browserTexts, parent);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLAnnotationProperty freshEntity, Project project, ChangeGenerationContext context, Optional<OWLAnnotationProperty> parent) {
        if(parent.isPresent()) {
            OWLAxiom ax = project.getDataFactory().getOWLSubAnnotationPropertyOfAxiom(freshEntity, parent.get());
            return Collections.singleton(ax);
        }
        else {
            return Collections.emptySet();
        }
    }
}
