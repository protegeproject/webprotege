package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
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
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.DATA_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
@AutoFactory
public class CreateDataPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLDataProperty, OWLDataProperty> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateDataPropertiesChangeGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                               @Provided @Nonnull MessageFormatter msg,
                                               @Provided @Nonnull OWLOntology rootOntology,
                                               @Nonnull String sourceText,
                                               @Nonnull String langTag,
                                               @Nonnull ImmutableSet<OWLDataProperty> parents) {
        super(DATA_PROPERTY, sourceText, langTag, parents, rootOntology, dataFactory, msg);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<? extends OWLAxiom> createParentPlacementAxioms(OWLDataProperty freshEntity,
                                                                  ChangeGenerationContext context,
                                                                  ImmutableSet<OWLDataProperty> parents) {
        return parents.stream()
                .map(parent -> dataFactory.getOWLSubDataPropertyOfAxiom(freshEntity, parent))
                .collect(toSet());
    }
}
