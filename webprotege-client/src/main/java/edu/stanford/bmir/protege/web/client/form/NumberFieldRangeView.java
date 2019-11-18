package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface NumberFieldRangeView extends IsWidget {

    void clear();

    void setAnyNumber(boolean b);

    boolean isAnyNumber();

    void setLowerBound(double min);

    double getLowerBound();

    void setLowerBoundType(NumberFieldRange.BoundType boundType);

    NumberFieldRange.BoundType getLowerBoundType();

    void setUpperBound(double max);

    double getUpperBound();

    void setUpperBoundType(NumberFieldRange.BoundType boundType);

    NumberFieldRange.BoundType getUpperBoundType();


}
