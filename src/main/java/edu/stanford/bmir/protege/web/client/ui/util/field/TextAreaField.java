package edu.stanford.bmir.protege.web.client.ui.util.field;

import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.form.TextArea;

public class TextAreaField extends AbstractField {

    @Override
    protected Component createFieldComponent() {
        return new TextArea();
    }

    public String getValueAsString() {
        return ((TextArea)getFieldComponent()).getValueAsString();
    }

}
