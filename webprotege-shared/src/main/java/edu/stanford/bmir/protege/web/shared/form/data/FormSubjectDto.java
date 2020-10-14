package edu.stanford.bmir.protege.web.shared.form.data;


import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

public abstract class FormSubjectDto implements IsSerializable {

    @Nonnull
    public static FormEntitySubjectDto get(@Nonnull OWLEntityData entity) {
        return new AutoValue_FormEntitySubjectDto(entity);
    }

    @Nonnull
    public abstract IRI getIri();

    @Nonnull
    public abstract FormEntitySubject toFormSubject();

    public static FormSubjectDto getFormSubject(OWLEntityData root) {
        return FormSubjectDto.get(root);
    }
}
