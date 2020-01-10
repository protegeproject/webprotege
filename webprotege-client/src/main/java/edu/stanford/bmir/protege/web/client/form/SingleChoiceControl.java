package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptor;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-01
 */
public interface SingleChoiceControl extends ChoiceControl {

    void setDescriptor(@Nonnull SingleChoiceControlDescriptor descriptor);
}
