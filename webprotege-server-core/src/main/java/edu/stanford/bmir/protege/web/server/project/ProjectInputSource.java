package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntologyDocument;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDocumentFormat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.*;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
public class ProjectInputSource implements OWLOntologyDocumentSource {

    private static final int BUFFER_SIZE = (8 * 1024 * 1024) + 100;

    @RootOntologyDocument
    private File rootOntologyDocument;


    @Inject
    public ProjectInputSource(@Nonnull File rootOntologyDocument) {
        this.rootOntologyDocument = checkNotNull(rootOntologyDocument);
    }

    @Override
    public boolean isReaderAvailable() {
        return false;
    }

    @Nonnull
    @Override
    public Reader getReader() {
        throw new RuntimeException("Reader Not Available");
    }

    @Override
    public boolean isInputStreamAvailable() {
        return true;
    }

    @Nonnull
    @Override
    public InputStream getInputStream() {
        try {
            return new BufferedInputStream(new FileInputStream(rootOntologyDocument),
                                           BUFFER_SIZE);
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Nonnull
    @Override
    public IRI getDocumentIRI() {
        return IRI.create(rootOntologyDocument.toURI());
    }

    @Nullable
    @Override
    public OWLDocumentFormat getFormat() {
        return new BinaryOWLOntologyDocumentFormat();
    }

    @Override
    public boolean isFormatKnown() {
        return true;
    }

    @Override
    public String getMIMEType() {
        throw new RuntimeException("MIME Type is Not Available");
    }

    @Override
    public boolean isMIMETypeKnown() {
        return false;
    }

    @Override
    public void setAcceptHeaders(String headers) {

    }

    @Override
    public Optional<String> getAcceptHeaders() {
        return Optional.empty();
    }
}
