package edu.stanford.bmir.protege.web.shared.itemlist;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetPersonIdCompletionsAction extends GetPossibleItemCompletionsAction<PersonId> {

    /**
     * For serialization only
     */
    private GetPersonIdCompletionsAction() {
    }

    public GetPersonIdCompletionsAction(String completionText) {
        super(completionText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCompletionText());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPersonIdCompletionsAction)) {
            return false;
        }
        GetPersonIdCompletionsAction other = (GetPersonIdCompletionsAction) obj;
        return this.getCompletionText().equals(other.getCompletionText());
    }


    @Override
    public String toString() {
        return toStringHelper("GetPersonIdCompletionsAction")
                .addValue(getCompletionText())
                .toString();
    }
}
