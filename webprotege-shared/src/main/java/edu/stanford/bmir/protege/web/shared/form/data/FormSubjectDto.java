package edu.stanford.bmir.protege.web.shared.form.data;


import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

public abstract class FormSubjectDto {

    public static FormIriSubjectDto get(@Nonnull IRIData iri) {
        return new AutoValue_FormIriSubjectDto(iri);
    }

    @Nonnull
    public abstract IRI getIri();
}
