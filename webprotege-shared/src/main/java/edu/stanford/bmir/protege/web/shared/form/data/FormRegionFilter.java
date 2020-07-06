package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormRegionFilter implements IsSerializable {

    @Nonnull
    public static FormRegionFilter get(@Nonnull FormRegionId formRegionId,
                                       @Nonnull PrimitiveFormControlDataMatchCriteria matchCriteria) {
        return new AutoValue_FormRegionFilter(formRegionId, matchCriteria);
    }

    @Nonnull
    public abstract FormRegionId getFormRegionId();

    @Nonnull
    public abstract PrimitiveFormControlDataMatchCriteria getMatchCriteria();

}
