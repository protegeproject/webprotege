package edu.stanford.bmir.protege.web.shared.reasoning;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import java.io.Serializable;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public class DLQueryEntitySetResult implements Serializable {

    private Optional<RevisionNumber> revisionNumber;

    private Optional<Consistency> consistency;

    private DLQueryResultSection section;

    private Optional<ImmutableCollection<OWLEntityData>> entityData;

    /**
     * For serialization purposes only
     */
    private DLQueryEntitySetResult() {
    }

    public DLQueryEntitySetResult(DLQueryResultSection section) {
        this.section = section;
        this.revisionNumber = Optional.absent();
        this.consistency = Optional.absent();
        this.entityData = Optional.absent();
    }

    public DLQueryEntitySetResult(
            Optional<RevisionNumber> revisionNumber,
            Optional<Consistency> consistency,
            DLQueryResultSection section,
            Optional<ImmutableCollection<OWLEntityData>> entityData) {
        this.revisionNumber = revisionNumber;
        this.consistency = consistency;
        this.section = section;
        this.entityData = entityData;
    }

    public Optional<RevisionNumber> getRevisionNumber() {
        return revisionNumber;
    }

    public Optional<Consistency> getConsistency() {
        return consistency;
    }

    public DLQueryResultSection getSection() {
        return section;
    }



    public Optional<ImmutableCollection<OWLEntityData>> getEntityData() {
        return entityData;
    }
}
