package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-13
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM
)
@JsonSubTypes({
                    @JsonSubTypes.Type(FormEntitySubject.class),
              })
public interface FormSubject {


    static FormEntitySubject get(@Nonnull OWLEntity entity) {
        return FormEntitySubject.get(entity);
    }

    @Nonnull
    IRI getIri();

}
