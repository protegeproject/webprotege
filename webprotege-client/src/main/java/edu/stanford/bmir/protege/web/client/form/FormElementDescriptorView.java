package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.ElementRun;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormElementDescriptorView extends IsWidget {

    interface FieldTypeChangedHandler {
        void handleFieldTypeChanged();
    }

    void clearOwlProperty();

    @Nonnull
    AcceptsOneWidget getFieldEditorContainer();

    void setFormElementId(@Nonnull String id);

    @Nonnull
    String getFormElementId();

    void setHelp(@Nonnull LanguageMap help);

    @Nonnull
    LanguageMap getHelp();

    void setLabel(@Nonnull LanguageMap label);

    @Nonnull
    LanguageMap getLabel();

    void setElementRun(ElementRun elementRun);

    ElementRun getElementRun();

    void setOptionality(@Nonnull Optionality optionality);

    @Nonnull
    Optionality getOptionality();

    void setOwlProperty(@Nonnull OWLPropertyData property);

    @Nonnull
    Optional<OWLPropertyData> getOwlProperty();

    void setRepeatability(@Nonnull Repeatability repeatability);

    @Nonnull
    Repeatability getRepeatability();

    @Nonnull
    String getFieldType();

    void setFieldTypeChangedHandler(FieldTypeChangedHandler handler);

    void setFieldType(@Nonnull String fieldType);

    void addAvailableFieldType(@Nonnull String value, @Nonnull String label);
}
