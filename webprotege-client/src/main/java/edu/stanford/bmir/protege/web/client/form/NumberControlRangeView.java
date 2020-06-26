package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlRange;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface NumberControlRangeView extends IsWidget {

    void clear();

    void setAnyNumber(boolean b);

    boolean isAnyNumber();

    void setLowerBound(double min);

    double getLowerBound();

    void setLowerBoundType(NumberControlRange.BoundType boundType);

    NumberControlRange.BoundType getLowerBoundType();

    void setUpperBound(double max);

    double getUpperBound();

    void setUpperBoundType(NumberControlRange.BoundType boundType);

    NumberControlRange.BoundType getUpperBoundType();


}
