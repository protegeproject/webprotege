package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
public class FormsManagerObjectListDefaultObjectProvider implements Provider<FormDescriptor> {

    @Nonnull
    private final UuidV4Provider uuidV4Provider;

    @Inject
    public FormsManagerObjectListDefaultObjectProvider(@Nonnull UuidV4Provider uuidV4Provider) {
        this.uuidV4Provider = checkNotNull(uuidV4Provider);
    }

    @Override
    public FormDescriptor get() {
        FormId formId = FormId.get(uuidV4Provider.get());
        return FormDescriptor.builder(formId)
                .build();
    }
}
