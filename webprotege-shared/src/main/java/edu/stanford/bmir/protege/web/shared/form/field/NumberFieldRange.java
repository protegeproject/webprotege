package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.base.Objects;
import com.google.common.collect.Range;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
public class NumberFieldRange implements IsSerializable {

    private static final NumberFieldRange ANY_NUMBER = new NumberFieldRange(Double.MIN_VALUE,
                                                                            BoundType.INCLUSIVE,
                                                                            Double.MAX_VALUE,
                                                                            BoundType.INCLUSIVE);

    private Double lowerBound = Double.MIN_VALUE;

    private BoundType lowerBoundType = BoundType.INCLUSIVE;

    private Double upperBound = Double.MAX_VALUE;

    private BoundType upperBoundType = BoundType.INCLUSIVE;

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

    public boolean isAnyNumber() {
        return this.equals(ANY_NUMBER);
    }

    public static NumberFieldRange range(double lowerBound,
                                         BoundType lowerBoundType,
                                         double upperBound,
                                         BoundType upperBoundType) {
        return new NumberFieldRange(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }

    public static NumberFieldRange all() {
        return ANY_NUMBER;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NumberFieldRange)) {
            return false;
        }
        NumberFieldRange other = (NumberFieldRange) obj;
        return this.lowerBound.equals(other.lowerBound)
                && this.lowerBoundType.equals(other.lowerBoundType)
                && this.upperBound.equals(other.upperBound)
                && this.upperBoundType.equals(other.upperBoundType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lowerBound, lowerBoundType, upperBound, upperBoundType);
    }

    public Range<Double> toRange() {
        return Range.range(
                this.getLowerBound(),
                this.getLowerBoundType() == NumberFieldRange.BoundType.INCLUSIVE ? com.google.common.collect.BoundType.CLOSED : com.google.common.collect.BoundType.OPEN,
                this.getUpperBound(),
                this.getUpperBoundType() == NumberFieldRange.BoundType.INCLUSIVE ? com.google.common.collect.BoundType.CLOSED : com.google.common.collect.BoundType.OPEN
        );
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

    @Override
    public String toString() {
        return toStringHelper("NumberFieldRange")
                .add("lowerBound", lowerBound)
                .add("lowerBoundType", lowerBoundType)
                .add("upperBound", upperBound)
                .add("upperBoundType", upperBoundType)
                .toString();
    }

    public enum BoundType {
        INCLUSIVE(">=", "<="),
        EXCLUSIVE(">", "<");

        private String lowerSymbol;

        private String upperSymbol;

        BoundType(String lowerSymbol, String upperSymbol) {
            this.lowerSymbol = lowerSymbol;
            this.upperSymbol = upperSymbol;
        }

        public String getLowerSymbol() {
            return lowerSymbol;
        }

        public String getUpperSymbol() {
            return upperSymbol;
        }
    }
}
