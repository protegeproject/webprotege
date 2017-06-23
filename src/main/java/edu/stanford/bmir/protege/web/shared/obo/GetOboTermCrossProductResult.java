package edu.stanford.bmir.protege.web.shared.obo;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermCrossProductResult implements Result {

    private OBOTermCrossProduct crossProduct;

    public GetOboTermCrossProductResult(@Nonnull OBOTermCrossProduct crossProduct) {
        this.crossProduct = crossProduct;
    }

    @GwtSerializationConstructor
    private GetOboTermCrossProductResult() {
    }

    @Nonnull
    public OBOTermCrossProduct getCrossProduct() {
        return crossProduct;
    }

    @Override
    public int hashCode() {
        return crossProduct.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermCrossProductResult)) {
            return false;
        }
        GetOboTermCrossProductResult other = (GetOboTermCrossProductResult) obj;
        return this.crossProduct.equals(other.crossProduct);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermCrossProductResult")
                .addValue(crossProduct)
                .toString();
    }
}
