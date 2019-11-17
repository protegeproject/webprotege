package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormElementDescriptorEditorView extends IsWidget {

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

    void setOptionality(@Nonnull Optionality optionality);

    @Nonnull
    Optionality getOptionality();

    void setOwlProperty(@Nonnull OWLProperty property);

    @Nonnull
    Optional<OWLProperty> getOwlProperty();

    void setRepeatability(@Nonnull Repeatability repeatability);

    @Nonnull
    Repeatability getRepeatability();

    @Nonnull
    String getFieldType();

    void setFieldType(@Nonnull String fieldType);

    void setAvailableFieldTypes(@Nonnull List<String> fieldTypes);
}
