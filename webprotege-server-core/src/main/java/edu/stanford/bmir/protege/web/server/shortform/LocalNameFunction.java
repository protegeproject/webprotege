package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Function;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class LocalNameFunction implements Function<IRI, String> {

    @Nonnull
    private final LocalNameExtractor extractor;

    @Inject
    public LocalNameFunction(@Nonnull LocalNameExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public String apply(IRI iri) {
        return extractor.getLocalName(iri);
    }
}
