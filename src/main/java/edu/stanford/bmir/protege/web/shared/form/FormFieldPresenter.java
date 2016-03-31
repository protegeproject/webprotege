package edu.stanford.bmir.protege.web.shared.form;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormFieldPresenter extends HasValueChangeHandlers<PropertyValue> {

    boolean canEditValues(List<OWLPrimitiveData> values);

    void setValues(List<OWLPropertyData> value);

    List<OWLPrimitiveData> getValues();

    FormFieldView getView();

    void clear();

}
