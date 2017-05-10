package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.Project;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2017
 */
public class DeleteEntitiesChangeListGenerator implements ChangeListGenerator<Set<OWLEntity>> {

    private final Set<OWLEntity> entities;

    public DeleteEntitiesChangeListGenerator(@Nonnull Set<OWLEntity> entities) {
        this.entities = ImmutableSet.copyOf(entities);
    }

    @Override
    public OntologyChangeList<Set<OWLEntity>> generateChanges(Project project, ChangeGenerationContext context) {

        OWLEntityRemover entityRemover = new OWLEntityRemover(project.getRootOntology().getImportsClosure());
        entities.forEach(entity -> entity.accept(entityRemover));
        return OntologyChangeList.<Set<OWLEntity>>builder().addAll(entityRemover.getChanges()).build(entities);
    }

    @Override
    public Set<OWLEntity> getRenamedResult(Set<OWLEntity> result, RenameMap renameMap) {
        return renameMap.getRenamedEntities(result);
    }
}
