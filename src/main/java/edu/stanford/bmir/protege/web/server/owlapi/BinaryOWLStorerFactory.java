package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLDocumentFormatFactory;
import org.semanticweb.owlapi.model.OWLStorer;
import org.semanticweb.owlapi.model.OWLStorerFactory;
import org.semanticweb.owlapi.util.OWLDocumentFormatFactoryImpl;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jul 16
 */
public class BinaryOWLStorerFactory implements OWLStorerFactory {

    /**
     * Create new storer.
     *
     * @return new storer
     */
    @Nonnull
    @Override
    public OWLStorer createStorer() {
        return new BinaryOWLOntologyDocumentStorer();
    }

    /**
     * @return format factory for the format parsed by this storer
     */
    @Nonnull
    @Override
    public OWLDocumentFormatFactory getFormatFactory() {
        return new OWLDocumentFormatFactoryImpl() {
            @Override
            public OWLDocumentFormat createFormat() {
                return new BinaryOWLOntologyDocumentFormat();
            }
        };
    }

    /**
     * Provides a fully-constructed and injected instance of {@code T}.
     *
     * @throws RuntimeException if the injector encounters an error while
     *                          providing an instance. For example, if an injectable member on
     *                          {@code T} throws an exception, the injector may wrap the exception
     *                          and throw it to the caller of {@code get()}. Callers should not try
     *                          to handle such exceptions as the behavior may vary across injector
     *                          implementations and even different configurations of the same injector.
     */
    @Override
    public OWLStorer get() {
        return createStorer();
    }
}
