package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.EntityType.OBJECT_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLObjectProperty, OWLObjectProperty> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    public CreateObjectPropertiesChangeGenerator(@Nonnull Set<String> browserTexts,
                                                 @Nonnull Optional<OWLObjectProperty> parent,
                                                 @Nonnull OWLOntology rootOntology,
                                                 @Nonnull OWLDataFactory dataFactory) {
        super(OBJECT_PROPERTY, browserTexts, parent, rootOntology, dataFactory);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLObjectProperty freshEntity,
                                                        ChangeGenerationContext context,
                                                        Optional<OWLObjectProperty> parent) {
        if (parent.isPresent()) {
            OWLSubObjectPropertyOfAxiom ax = dataFactory.getOWLSubObjectPropertyOfAxiom(freshEntity, parent.get());
            return Collections.singleton(ax);
        }
        else {
            return Collections.emptySet();
        }
    }
}
