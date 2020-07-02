package edu.stanford.bmir.protege.web.shared.form.data;

import javax.annotation.Nonnull;

public interface FormControlDataDtoVisitorEx<R> {

    R visit(@Nonnull EntityNameControlDataDto entityNameControlData);

    R visit(@Nonnull FormDataDto formData);

    R visit(@Nonnull GridControlDataDto gridControlData);

    R visit(@Nonnull ImageControlDataDto imageControlData);

    R visit(@Nonnull MultiChoiceControlDataDto multiChoiceControlData);

    R visit(@Nonnull SingleChoiceControlDataDto singleChoiceControlData);

    R visit(@Nonnull NumberControlDataDto numberControlData);

    R visit(@Nonnull TextControlDataDto textControlData);
}
