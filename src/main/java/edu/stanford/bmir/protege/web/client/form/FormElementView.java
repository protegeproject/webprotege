package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormElementView extends IsWidget {

    void setFormLabel(String formLabel);

    void setEditor(FormElementEditor editor);

    FormElementEditor getEditor();

}
