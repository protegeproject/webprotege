package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceFieldType;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface ChoiceFieldDescriptorView extends IsWidget {

    @Nonnull
    ChoiceFieldType getWidgetType();

    void setWidgetType(@Nonnull ChoiceFieldType widgetType);

    void setChoiceDescriptors(@Nonnull List<ChoiceDescriptor> choiceDescriptors);

    @Nonnull
    List<ChoiceDescriptor> getChoiceDescriptors();
}
