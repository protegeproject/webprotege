package edu.stanford.bmir.protege.web.shared.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-22
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormPageRequest {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public enum SourceType {
        CONTROL_STACK,
        GRID_CONTROL
    }

    @Nonnull
    public static FormPageRequest get(@Nonnull FormId formId,
                                      @Nonnull FormSubject subject,
                                      @Nonnull FormRegionId formFieldId,
                                      @Nonnull SourceType sourceType,
                                      @Nonnull PageRequest pageRequest) {
        return new AutoValue_FormPageRequest(formId, subject, formFieldId, sourceType, pageRequest);
    }

    @Nonnull
    public abstract FormId getFormId();

    @Nonnull
    public abstract FormSubject getSubject();

    @Nonnull
    public abstract FormRegionId getFieldId();

    @Nonnull
    public abstract SourceType getSourceType();

    @Nonnull
    public abstract PageRequest getPageRequest();
}
