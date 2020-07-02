package edu.stanford.bmir.protege.web.server.form.processor;

import edu.stanford.bmir.protege.web.server.form.FormFrame;
import edu.stanford.bmir.protege.web.server.form.FormSubjectResolver;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-13
 *
 * Converts raw {@link FormData} into a {@link FormFrame}, which is a step closer to an {@link EntityFrame}.
 */
public class FormDataConverter {

    @Nonnull
    private final FormSubjectResolver formSubjectResolver;

    @Nonnull
    private final FormDataProcessor formDataProcessor;

    @Inject
    public FormDataConverter(@Nonnull FormSubjectResolver formSubjectResolver,
                             @Nonnull FormDataProcessor formDataProcessor) {
        this.formSubjectResolver = checkNotNull(formSubjectResolver);
        this.formDataProcessor = checkNotNull(formDataProcessor);
    }

    /**
     * Converts the specified {@link FormData} into its equivalent {@link FormFrame}.
     */
    public FormFrame convert(@Nonnull FormData formData) {
        var formFrameBuilder = formDataProcessor.processFormData(formData, false);
        return formFrameBuilder.build(formSubjectResolver);
    }
}
