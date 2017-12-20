package edu.stanford.bmir.protege.web.server.crud;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 9 May 2017
 */
@AutoFactory
public class DeleteEntitiesChangeListGenerator implements ChangeListGenerator<Set<OWLEntity>> {

    private final Set<OWLEntity> entities;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final MessageFormatter msgFormatter;

    private String message = "Deleted entities";

    public DeleteEntitiesChangeListGenerator(@Provided @Nonnull OWLOntology rootOntology,
                                             @Provided @Nonnull MessageFormatter msgFormatter,
                                             @Nonnull Set<OWLEntity> entities) {
        this.entities = ImmutableSet.copyOf(entities);
        this.msgFormatter = msgFormatter;
        this.rootOntology = rootOntology;
    }

    @Override
    public OntologyChangeList<Set<OWLEntity>> generateChanges(ChangeGenerationContext context) {
        generateMessage();
        OWLEntityRemover entityRemover = new OWLEntityRemover(rootOntology.getImportsClosure());
        entities.forEach(entity -> entity.accept(entityRemover));
        return OntologyChangeList.<Set<OWLEntity>>builder().addAll(entityRemover.getChanges()).build(entities);
    }

    private void generateMessage() {
        if (entities.size() == 1) {
            message = msgFormatter.format("Deleted {0}: {1}",
                                          entities.iterator().next().getEntityType().getPrintName().toLowerCase(),
                                          entities);
        }
        else {
            Collection<EntityType<?>> deletedTypes = entities.stream()
                                                             .map(OWLEntity::getEntityType)
                                                             .collect(Collectors.toSet());
            if (deletedTypes.size() == 1) {
                message = msgFormatter.format("Deleted {0} {1}: {2}",
                                              entities.size(),
                                              deletedTypes.iterator().next().getPluralPrintName().toLowerCase(),
                                              entities);
            }
            else {
                message = msgFormatter.format("Deleted: {1}",
                                              entities);
            }
        }
    }

    @Override
    public Set<OWLEntity> getRenamedResult(Set<OWLEntity> result, RenameMap renameMap) {
        return renameMap.getRenamedEntities(result);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Set<OWLEntity>> result) {
        return message;
    }
}
