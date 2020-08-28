package edu.stanford.bmir.protege.web.server.shortform;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
@AutoValue
public abstract class LuceneEntityDocument {

    @Nonnull
    @JsonCreator
    public static LuceneEntityDocument get(@Nonnull EntityType<?> entityType,
                                           @Nonnull IRI iri) {
        return new AutoValue_LuceneEntityDocument(entityType, iri);
    }

    @JsonProperty("entityType")
    @Nonnull
    public abstract EntityType<?> getEntityType();

    @JsonProperty("iri")
    @Nonnull
    public abstract IRI getIri();

}
