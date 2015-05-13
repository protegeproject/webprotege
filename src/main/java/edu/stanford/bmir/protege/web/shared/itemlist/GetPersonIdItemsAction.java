package edu.stanford.bmir.protege.web.shared.itemlist;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

import java.util.List;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/05/15
 */
public class GetPersonIdItemsAction extends GetItemsAction<PersonId, GetPersonIdItemsResult> {

    /**
     * For serialization only
     */
    private GetPersonIdItemsAction() {
    }

    public GetPersonIdItemsAction(List<String> itemNames) {
        super(itemNames);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getItemNames());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPersonIdItemsAction)) {
            return false;
        }
        GetPersonIdItemsAction other = (GetPersonIdItemsAction) obj;
        return this.getItemNames().equals(other.getItemNames());
    }


    @Override
    public String toString() {
        return toStringHelper("GetPersonIdItemsAction")
                .addValue(getItemNames())
                .toString();
    }
}
