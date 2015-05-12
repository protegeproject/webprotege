package edu.stanford.bmir.protege.web.shared.itemlist;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

import java.util.List;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsResult extends GetPossibleItemCompletionsResult<PersonId> {

    /**
     * For serialization only
     */
    private GetPersonIdCompletionsResult() {
    }

    public GetPersonIdCompletionsResult(List<PersonId> possibleItemCompletions) {
        super(possibleItemCompletions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPossibleItemCompletions());
    }


    @Override
    public String toString() {
        return toStringHelper("GetPersonIdCompletionsResult")
                .addValue(getPossibleItemCompletions())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPersonIdCompletionsResult)) {
            return false;
        }
        GetPersonIdCompletionsResult other = (GetPersonIdCompletionsResult) obj;
        return this.getPossibleItemCompletions().equals(other.getPossibleItemCompletions());
    }
}
