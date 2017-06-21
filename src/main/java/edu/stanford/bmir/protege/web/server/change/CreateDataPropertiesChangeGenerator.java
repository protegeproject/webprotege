package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.semanticweb.owlapi.model.EntityType.DATA_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateDataPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLDataProperty, OWLDataProperty> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateDataPropertiesChangeGenerator(@Nonnull Set<String> browserTexts,
                                               @Nonnull Optional<OWLDataProperty> parent,
                                               @Nonnull OWLOntology rootOntology,
                                               @Nonnull OWLDataFactory dataFactory) {
        super(DATA_PROPERTY, browserTexts, parent, rootOntology, dataFactory);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLDataProperty freshEntity,
                                                        ChangeGenerationContext context,
                                                        Optional<OWLDataProperty> parent) {
        if (parent.isPresent()) {
            OWLAxiom ax = dataFactory.getOWLSubDataPropertyOfAxiom(freshEntity, parent.get());
            return singleton(ax);
        }
        else {
            return emptySet();
        }
    }
}
