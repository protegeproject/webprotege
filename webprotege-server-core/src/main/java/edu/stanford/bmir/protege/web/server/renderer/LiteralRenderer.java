package edu.stanford.bmir.protege.web.server.renderer;

import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-02
 */
public interface LiteralRenderer {

    @Nonnull
    String getLiteralRendering(@Nonnull
                  OWLLiteral literal);
}
