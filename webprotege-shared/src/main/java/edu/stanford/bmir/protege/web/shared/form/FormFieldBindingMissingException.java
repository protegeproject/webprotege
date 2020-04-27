package edu.stanford.bmir.protege.web.shared.form;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class FormFieldBindingMissingException extends RuntimeException implements IsSerializable {

    private FormFieldId formFieldId;

    public FormFieldBindingMissingException(@Nonnull FormFieldId formFieldId) {
        super("Form field binding is missing for " + formFieldId.getId() + ".  Improperly configured form.");
        this.formFieldId = checkNotNull(formFieldId);
    }

    @GwtSerializationConstructor
    private FormFieldBindingMissingException() {
    }

    public FormFieldId getFormFieldId() {
        return formFieldId;
    }
}
