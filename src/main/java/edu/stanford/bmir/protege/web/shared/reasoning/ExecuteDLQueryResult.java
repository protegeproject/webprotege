package edu.stanford.bmir.protege.web.shared.reasoning;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import java.util.Collection;
import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class ExecuteDLQueryResult implements Result {

    private Optional<RevisionNumber> revisionNumber;

    private ImmutableList<OWLClassData> subClasses;

    private ImmutableList<OWLClassData> superClasses;

    /**
     * For serialization purposes only
     */
    private ExecuteDLQueryResult() {
    }

    public ExecuteDLQueryResult(Optional<RevisionNumber> revisionNumber, ImmutableList<OWLClassData> subClasses, ImmutableList<OWLClassData> superClasses) {
        this.subClasses = subClasses;
        this.superClasses = superClasses;
        this.revisionNumber = revisionNumber;
    }

    public Optional<RevisionNumber> getRevisionNumber() {
        return revisionNumber;
    }

    public ImmutableList<OWLClassData> getSubClasses() {
        return subClasses;
    }

    public ImmutableList<OWLClassData> getSuperClasses() {
        return superClasses;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("ExecuteDLQueryResult")
                      .addValue(subClasses).toString();
    }
}
