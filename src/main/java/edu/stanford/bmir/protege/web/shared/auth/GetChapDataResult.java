package edu.stanford.bmir.protege.web.shared.auth;


import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class GetChapDataResult implements Result {

    private ChapData chapData;

    private GetChapDataResult() {
    }

    public GetChapDataResult(ChapData chapData) {
        this.chapData = checkNotNull(chapData);
    }

    public ChapData getChapData() {
        return chapData;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(chapData);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetChapDataResult)) {
            return false;
        }
        GetChapDataResult other = (GetChapDataResult) obj;
        return this.chapData.equals(other.chapData);
    }


    @Override
    public String toString() {
        return toStringHelper("GetChapDataResult")
                .addValue(chapData)
                .toString();
    }
}
