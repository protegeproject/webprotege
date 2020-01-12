package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public interface FixedChoiceListSourceDescriptorView extends IsWidget {

    void setChoiceDescriptors(@Nonnull List<ChoiceDescriptor> choiceDescriptors);

    @Nonnull
    ImmutableList<ChoiceDescriptor> getChoiceDescriptors();
}
