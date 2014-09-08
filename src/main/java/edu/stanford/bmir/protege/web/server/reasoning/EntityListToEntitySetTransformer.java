package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.reasoning.Consistency;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryEntitySetResult;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryResultSection;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public class EntityListToEntitySetTransformer {

    private OWLAPIProject project;

    public EntityListToEntitySetTransformer(OWLAPIProject project) {
        this.project = project;
    }


    public DLQueryEntitySetResult getEntitySetResult(RevisionNumber revisionNumber, DLQueryResultSection section, ImmutableList<? extends OWLEntity> nodeSet) {
        ImmutableList.Builder<OWLEntityData> builder = ImmutableList.builder();
        for(OWLEntity ent : nodeSet) {
            OWLEntityData entData = project.getRenderingManager().getRendering(ent);
            builder.add(entData);
        }
        ImmutableCollection<OWLEntityData> entityData = builder.build();
        return new DLQueryEntitySetResult(Optional.<RevisionNumber>of(revisionNumber), Optional.of(Consistency.CONSISTENT), section, Optional.of(entityData));
    }
}
