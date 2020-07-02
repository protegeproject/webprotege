package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormIriSubjectDto extends FormSubjectDto {

    @Nonnull
    public static FormIriSubjectDto get(@Nonnull IRIData data) {
        return new AutoValue_FormIriSubjectDto(data);
    }

    @Nonnull
    public abstract IRIData getIriData();

    @Nonnull
    @Override
    public IRI getIri() {
        return getIriData().getObject();
    }

    @Memoized
    @Nonnull
    @Override
    public FormSubject toFormSubject() {
        return FormIriSubject.get(getIri());
    }
}
