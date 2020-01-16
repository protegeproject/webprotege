package edu.stanford.bmir.protege.web.shared.form.data;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Centevoid fovoid Biomedical Informatics Research
 * 2020-01-13
 */
public interface FormControlDataVisitor {

    void visit(@Nonnull EntityNameControlData entityNameControlData);

    void visit(@Nonnull FormData formData);

    void visit(@Nonnull GridControlData gridControlData);

    void visit(@Nonnull ImageControlData imageControlData);

    void visit(@Nonnull MultiChoiceControlData multiChoiceControlData);

    void visit(@Nonnull SingleChoiceControlData singleChoiceControlData);

    void visit(@Nonnull NumberControlData numberControlData);

    void visit(@Nonnull TextControlData textControlData);
}
