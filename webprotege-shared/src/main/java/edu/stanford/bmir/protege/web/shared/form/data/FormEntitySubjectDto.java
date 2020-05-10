package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormEntitySubjectDto extends FormSubjectDto {

    @Nonnull
    public static FormEntitySubjectDto get(@Nonnull OWLEntityData entityData) {
        return new AutoValue_FormEntitySubjectDto(entityData);
    }

    @Nonnull
    public static FormIriSubjectDto get(@Nonnull IRIData iriData) {
        return new AutoValue_FormIriSubjectDto(iriData);
    }


    @Nonnull
    public abstract OWLEntityData getEntityData();

    @Nonnull
    @Override
    public IRI getIri() {
        return getEntityData().getEntity().getIRI();
    }

    @Memoized
    @Nonnull
    @Override
    public FormSubject toFormSubject() {
        return FormEntitySubject.get(getEntityData().getEntity());
    }
}
