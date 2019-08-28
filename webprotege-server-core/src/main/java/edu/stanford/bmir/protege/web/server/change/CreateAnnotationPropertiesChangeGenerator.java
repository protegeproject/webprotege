package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.ANNOTATION_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
@AutoFactory
public class CreateAnnotationPropertiesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLAnnotationProperty, OWLAnnotationProperty> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateAnnotationPropertiesChangeGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                                     @Provided @Nonnull MessageFormatter msg,
                                                     @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                                     @Nonnull String sourceText,
                                                     @Nonnull String langTag,
                                                     @Nonnull ImmutableSet<OWLAnnotationProperty> parents) {
        super(ANNOTATION_PROPERTY, sourceText, langTag, parents, dataFactory, msg, defaultOntologyIdManager);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<? extends OWLAxiom> createParentPlacementAxioms(OWLAnnotationProperty freshEntity,
                                                                  ChangeGenerationContext context,
                                                                  ImmutableSet<OWLAnnotationProperty> parents) {
        return parents.stream()
                .map(parent -> dataFactory.getOWLSubAnnotationPropertyOfAxiom(freshEntity, parent))
                .collect(toSet());
    }
}
