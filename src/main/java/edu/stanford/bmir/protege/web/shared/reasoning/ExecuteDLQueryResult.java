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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class ExecuteDLQueryResult implements Result {

    private Optional<DLQueryResult> result;

    /**
     * For serialization purposes only
     */
    private ExecuteDLQueryResult() {
    }

    public ExecuteDLQueryResult(Optional<DLQueryResult> result) {
        this.result = checkNotNull(result);
    }

    public Optional<DLQueryResult> getResult() {
        return result;
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("ExecuteDLQueryResult")
                      .addValue(result).toString();
    }
}
