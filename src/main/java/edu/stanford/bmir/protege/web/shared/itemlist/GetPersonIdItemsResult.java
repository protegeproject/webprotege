package edu.stanford.bmir.protege.web.shared.itemlist;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/05/15
 */
public class GetPersonIdItemsResult extends GetItemsResult<PersonId> {

    /**
     * For serialization only
     */
    protected GetPersonIdItemsResult() {
    }

    public GetPersonIdItemsResult(List<PersonId> items) {
        super(items);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getItems());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetPersonIdItemsResult)) {
            return false;
        }
        GetPersonIdItemsResult other = (GetPersonIdItemsResult) obj;
        return this.getItems().equals(other.getItems());
    }


    @Override
    public String toString() {
        return toStringHelper("GetPersonIdItemsResult")
                .addValue(getItems())
                .toString();
    }
}
