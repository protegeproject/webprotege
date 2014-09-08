package edu.stanford.bmir.protege.web.shared.reasoning;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import java.io.Serializable;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public class DLQueryResult implements Serializable {

    private Optional<RevisionNumber> revisionNumber;

    private Optional<Consistency> consistency;

    private ImmutableList<DLQueryEntitySetResult> sections;

    private DLQueryResult() {
    }

    public DLQueryResult(
            Optional<RevisionNumber> revisionNumber,
            Optional<Consistency> consistency,
            ImmutableList<DLQueryEntitySetResult> sections) {
        this.revisionNumber = revisionNumber;
        this.consistency = consistency;
        this.sections = sections;
    }

    public Optional<RevisionNumber> getRevisionNumber() {
        return revisionNumber;
    }

    public Optional<Consistency> getConsistency() {
        return consistency;
    }

    public ImmutableList<DLQueryEntitySetResult> getSections() {
        return sections;
    }
}
