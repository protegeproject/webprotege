package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.MultiChoiceControlDescriptorDto;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-01
 */
public interface MultiValueChoiceControl extends ChoiceControl {

    void setDescriptor(@Nonnull MultiChoiceControlDescriptorDto descriptor);
}
