package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-15
 */
public interface RepeatabilityView extends IsWidget {

    void setRepeatability(@Nonnull Repeatability repeatability);

    @Nonnull
    Repeatability getRepeatability();
}
