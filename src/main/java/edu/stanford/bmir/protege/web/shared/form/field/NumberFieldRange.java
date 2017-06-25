package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class NumberFieldRange implements IsSerializable {

    private Double lowerBound;

    private BoundType lowerBoundType;

    private Double upperBound;

    private BoundType upperBoundType;

    private NumberFieldRange(double lowerBound,
                             BoundType lowerBoundType,
                             double upperBound,
                             BoundType upperBoundType) {
        this.lowerBound = lowerBound;
        this.lowerBoundType = lowerBoundType;
        this.upperBound = upperBound;
        this.upperBoundType = upperBoundType;
    }

    @GwtSerializationConstructor
    private NumberFieldRange() {
    }

    public static NumberFieldRange range(double lowerBound,
                                         BoundType lowerBoundType,
                                         double upperBound,
                                         BoundType upperBoundType) {
        return new NumberFieldRange(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public BoundType getLowerBoundType() {
        return lowerBoundType;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public BoundType getUpperBoundType() {
        return upperBoundType;
    }

    public enum BoundType {
        INCLUSIVE,
        EXCLUSIVE
    }
}
