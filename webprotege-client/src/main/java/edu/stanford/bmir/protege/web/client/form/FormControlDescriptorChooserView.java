package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public interface FormControlDescriptorChooserView extends IsWidget {

    void addAvailableFieldType(String descriptorType, String descriptorLabel);

    @Nonnull
    AcceptsOneWidget getFieldEditorContainer();

    @Nonnull
    String getFieldType();

    void setFieldType(@Nonnull String fieldType);

    void setFieldTypeChangedHandler(Runnable handler);
}
