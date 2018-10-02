package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class SetAnnotationValueActionChangeListGenerator implements ChangeListGenerator<Set<OWLEntity>> {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final ImmutableSet<OWLEntity> entities;

    @Nonnull
    private final OWLAnnotationProperty property;

    @Nonnull
    private final OWLAnnotationValue value;

    @Nonnull
    private final MessageFormatter messageFormatter;

    @Nonnull
    private final String commitMessage;

    private OWLLiteral litTrue;

    @AutoFactory
    public SetAnnotationValueActionChangeListGenerator(@Provided @Nonnull OWLDataFactory dataFactory,
                                                       @Provided @Nonnull OWLOntology rootOntology,
                                                       @Provided @Nonnull MessageFormatter messageFormatter,
                                                       @Nonnull ImmutableSet<OWLEntity> entities,
                                                       @Nonnull OWLAnnotationProperty property,
                                                       @Nonnull OWLAnnotationValue value,
                                                       @Nonnull String commitMessage) {
        this.dataFactory = checkNotNull(dataFactory);
        this.litTrue = dataFactory.getOWLLiteral(true);
        this.rootOntology = checkNotNull(rootOntology);
        this.entities = checkNotNull(entities);
        this.property = checkNotNull(property);
        this.value = checkNotNull(value);
        this.messageFormatter = checkNotNull(messageFormatter);
        this.commitMessage = checkNotNull(commitMessage);
    }

    @Override
    public OntologyChangeList<Set<OWLEntity>> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Set<OWLEntity>> builder = OntologyChangeList.builder();
        // TODO: Axiom Annotations
        for(OWLOntology ontology : rootOntology.getImportsClosure()) {
            for (OWLEntity entity : entities) {
                if (ontology.containsEntityInSignature(entity, Imports.EXCLUDED)) {
                    for(OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(entity.getIRI())) {
                        if (ax.getProperty().equals(property)) {
                            builder.removeAxiom(ontology, ax);
                        }
                    }
                }
            }
        }
        for(OWLEntity entity : entities) {
            OWLAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(property, entity.getIRI(), value);
            builder.addAxiom(rootOntology, ax);
        }
        return builder.build(entities);
    }

    @Override
    public Set<OWLEntity> getRenamedResult(Set<OWLEntity> result, RenameMap renameMap) {
        return result;
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Set<OWLEntity>> result) {
        return commitMessage;
    }
}
