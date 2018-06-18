package edu.stanford.bmir.protege.web.server.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public abstract class IriMixin {

    @JsonValue
    public abstract String toString();

    @JsonCreator
    public static IRI create(@Nonnull String s) {
        return null;
    }
}
