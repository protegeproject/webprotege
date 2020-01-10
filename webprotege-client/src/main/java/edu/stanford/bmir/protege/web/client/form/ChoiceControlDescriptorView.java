package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlType;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface ChoiceControlDescriptorView extends IsWidget {

    @Nonnull
    SingleChoiceControlType getWidgetType();

    void setWidgetType(@Nonnull SingleChoiceControlType widgetType);

    void setChoiceDescriptors(@Nonnull List<ChoiceDescriptor> choiceDescriptors);

    @Nonnull
    ImmutableList<ChoiceDescriptor> getChoiceDescriptors();
}
