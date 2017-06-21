package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.EntityType.ANNOTATION_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateAnnotationPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLAnnotationProperty, OWLAnnotationProperty> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateAnnotationPropertiesChangeGenerator(@Nonnull Set<String> browserTexts,
                                                     @Nonnull Optional<OWLAnnotationProperty> parent,
                                                     @Nonnull OWLOntology rootOntology,
                                                     @Nonnull OWLDataFactory dataFactory) {
        super(ANNOTATION_PROPERTY, browserTexts, parent, rootOntology, dataFactory);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLAnnotationProperty freshEntity,
                                                        ChangeGenerationContext context,
                                                        Optional<OWLAnnotationProperty> parent) {
        if(parent.isPresent()) {
            OWLAxiom ax = dataFactory.getOWLSubAnnotationPropertyOfAxiom(freshEntity, parent.get());
            return Collections.singleton(ax);
        }
        else {
            return Collections.emptySet();
        }
    }
}
