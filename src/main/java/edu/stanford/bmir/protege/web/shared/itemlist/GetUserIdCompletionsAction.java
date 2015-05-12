package edu.stanford.bmir.protege.web.shared.itemlist;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GetUserIdCompletionsAction extends GetPossibleItemCompletionsAction<UserId> {

    /**
     * For serialization only
     */
    private GetUserIdCompletionsAction() {
    }

    public GetUserIdCompletionsAction(String completionText) {
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
        if (!(obj instanceof GetUserIdCompletionsAction)) {
            return false;
        }
        GetUserIdCompletionsAction other = (GetUserIdCompletionsAction) obj;
        return this.getCompletionText().equals(other.getCompletionText());
    }


    @Override
    public String toString() {
        return toStringHelper("GetUserIdCompletionsAction")
                .addValue(getCompletionText())
                .toString();
    }
}
