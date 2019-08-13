package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.util.EntityDeleter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class DeleteEntityChangeListGenerator implements ChangeListGenerator<OWLEntity> {

    @Nonnull
    private final OWLEntity entity;

    @Nonnull
    private final EntityDeleter entityDeleter;

    @Inject
    public DeleteEntityChangeListGenerator(@Nonnull OWLEntity entity,
                                           @Nonnull EntityDeleter entityDeleter) {
        this.entity = checkNotNull(entity);
        this.entityDeleter = checkNotNull(entityDeleter);
    }

    @Override
    public OntologyChangeList<OWLEntity> generateChanges(ChangeGenerationContext context) {
        var deletionChanges = entityDeleter.getChangesToDeleteEntities(Collections.singleton(entity));
        var changeListBuilder = new OntologyChangeList.Builder<OWLEntity>();
        changeListBuilder.addAll(deletionChanges);
        return changeListBuilder.build(entity);
    }

    @Override
    public OWLEntity getRenamedResult(OWLEntity result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<OWLEntity> result) {
        return "Deleted " + entity.getEntityType().getPrintName().toLowerCase();
    }
}
