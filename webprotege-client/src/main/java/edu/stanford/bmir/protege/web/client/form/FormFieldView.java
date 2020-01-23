package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormFieldView extends IsWidget {

    void collapse();

    void expand();

    interface HeaderClickedHandler {
        void handleHeaderClicked();
    }

    void setHeaderClickedHandler(@Nonnull HeaderClickedHandler headerClickedHandler);

    void setHelpText(@Nonnull String helpText);

    void clearHelpText();

    void addStylePropertyValue(String cssProperty, String cssValue);

    void setId(FormFieldId elementId);

    Optional<FormFieldId> getId();

    void setFormLabel(String formLabel);

    void setEditor(FormFieldControl editor);

    FormFieldControl getEditor();

    void clearLimitedValuesDisplayed();

    void setLimitedValuesDisplayed(int size, int formControlDataCount);

    void setRequiredValueNotPresentVisible(boolean visible);

    void setRequired(Optionality required);

    Optionality getRequired();
}
