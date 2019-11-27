package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.ElementRun;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-16
 */
public interface FormElementDescriptorView extends IsWidget {

    void clearOwlProperty();

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

    void setElementIdChangedHandler(@Nonnull Consumer<FormElementId> runnable);

    @Nonnull
    AcceptsOneWidget getFieldDescriptorViewContainer();
}
