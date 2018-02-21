package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermXRefsResult implements Result {

    private ImmutableList<OBOXRef> xRefs;

    public GetOboTermXRefsResult(@Nonnull List<OBOXRef> xRefs) {
        this.xRefs = ImmutableList.copyOf(xRefs);
    }

    @GwtSerializationConstructor
    private GetOboTermXRefsResult() {
    }

    @Nonnull
    public List<OBOXRef> getxRefs() {
        return xRefs;
    }

    @Override
    public int hashCode() {
        return xRefs.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermXRefsResult)) {
            return false;
        }
        GetOboTermXRefsResult other = (GetOboTermXRefsResult) obj;
        return this.xRefs.equals(other.xRefs);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermXRefsResult")
                .addValue(xRefs)
                .toString();
    }
}
