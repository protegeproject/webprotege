package edu.stanford.bmir.protege.web.shared.match.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Jun 2018
 */
public enum NumericPredicate implements IsSerializable {

    GREATER_THAN(">") {
        @Override
        public <A, R> R visit(@Nonnull NumericPredicateVisitor<A, R> visitor, A arg) {
            return visitor.isGreaterThan(arg);
        }

        @Override
        public boolean eval(double left, double right) {
            return left > right;
        }

        @Override
        public boolean eval(int left, int right) {
            return left > right;
        }
    },

    GREATER_THAN_OR_EQUAL_TO(">=") {
        @Override
        public <A, R> R visit(@Nonnull NumericPredicateVisitor<A, R> visitor, A arg) {
            return visitor.isGreaterThanOrEqualTo(arg);
        }

        @Override
        public boolean eval(double left, double right) {
            return left >= right;
        }

        @Override
        public boolean eval(int left, int right) {
            return left >= right;
        }
    },

    LESS_THAN("<") {
        @Override
        public <A, R> R visit(@Nonnull NumericPredicateVisitor<A, R> visitor, A arg) {
            return visitor.isLessThan(arg);
        }

        @Override
        public boolean eval(double left, double right) {
            return left < right;
        }

        @Override
        public boolean eval(int left, int right) {
            return left < right;
        }
    },

    LESS_THAN_OR_EQUAL_TO("<=") {
        @Override
        public <A, R> R visit(@Nonnull NumericPredicateVisitor<A, R> visitor, A arg) {
            return visitor.isLessThanOrEqualTo(arg);
        }

        @Override
        public boolean eval(double left, double right) {
            return left <= right;
        }

        @Override
        public boolean eval(int left, int right) {
            return left <= right;
        }
    },

    IS_EQUAL_TO("=") {
        @Override
        public <A, R> R visit(@Nonnull NumericPredicateVisitor<A, R> visitor, A arg) {
            return visitor.isEqualTo(arg);
        }

        @Override
        public boolean eval(double left, double right) {
            return left == right;
        }

        @Override
        public boolean eval(int left, int right) {
            return left == right;
        }
    };

    private String symbol;

    @JsonCreator
    NumericPredicate(String symbol) {
        this.symbol = symbol;
    }

    NumericPredicate() {
    }

    @JsonValue
    public String getSymbol() {
        return symbol;
    }

    public abstract <A, R> R visit(@Nonnull NumericPredicateVisitor<A, R> visitor, A arg);

    public abstract boolean eval(double left, double right);

    public abstract boolean eval(int left, int right);
}
