package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.util.ClassExpression;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.CLASS;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
@AutoFactory
public class CreateClassesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLClass, OWLClass> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateClassesChangeGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                        @Provided @Nonnull MessageFormatter msg,
                                        @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                                        @Nonnull String sourceText,
                                        @Nonnull String langTag,
                                        @Nonnull ImmutableSet<OWLClass> parent) {
        super(CLASS, sourceText, langTag, parent, dataFactory, msg, defaultOntologyIdManager);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    protected Set<? extends OWLAxiom> createParentPlacementAxioms(OWLClass freshEntity,
                                                                  ChangeGenerationContext context,
                                                                  ImmutableSet<OWLClass> parents) {
        return parents.stream()
                .filter(ClassExpression::isNotOwlThing)
                .map(parent -> dataFactory.getOWLSubClassOfAxiom(freshEntity, parent))
                .collect(toSet());
    }
}
