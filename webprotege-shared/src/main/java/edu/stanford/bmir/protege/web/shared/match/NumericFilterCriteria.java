package edu.stanford.bmir.protege.web.shared.match;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
public class NumericFilterCriteria implements AnnotationValueFilter {

    private NumericFilterType filterType;

    private double value;

    public NumericFilterCriteria(NumericFilterType filterType, double value) {
        this.filterType = filterType;
        this.value = value;
    }

    public NumericFilterType getFilterType() {
        return filterType;
    }

    public double getValue() {
        return value;
    }
}
