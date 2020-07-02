package edu.stanford.bmir.protege.web.shared.form.data;


import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

public abstract class FormSubjectDto implements IsSerializable {

    @Nonnull
    public static FormIriSubjectDto get(@Nonnull IRIData iri) {
        return new AutoValue_FormIriSubjectDto(iri);
    }

    @Nonnull
    public static FormEntitySubjectDto get(@Nonnull OWLEntityData entity) {
        return new AutoValue_FormEntitySubjectDto(entity);
    }

    @Nonnull
    public abstract IRI getIri();

    @Nonnull
    public abstract FormSubject toFormSubject();

    public static FormSubjectDto getFormSubject(OWLPrimitiveData root) {
        if (root instanceof IRIData) {
            return FormSubjectDto.get((IRIData) root);
        } else if (root instanceof OWLEntityData) {
            return FormSubjectDto.get((OWLEntityData) root);
        } else {
            throw new RuntimeException("Cannot process form subjects that are not IRIs or Entities");
        }
    }
}
