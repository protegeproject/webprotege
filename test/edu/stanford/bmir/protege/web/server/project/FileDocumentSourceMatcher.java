package edu.stanford.bmir.protege.web.server.project;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.IRI;

import java.io.File;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class FileDocumentSourceMatcher extends TypeSafeMatcher<OWLOntologyDocumentSource> {


    private File ontologyDocumentSource;

    public FileDocumentSourceMatcher(File ontologyDocumentSource) {
        this.ontologyDocumentSource = ontologyDocumentSource;
    }

    @Override
    protected boolean matchesSafely(OWLOntologyDocumentSource source) {
        IRI expectedIRI = IRI.create(ontologyDocumentSource);
        return source instanceof FileDocumentSource && source.getDocumentIRI().equals(expectedIRI);
    }

    @Override
    public void describeTo(Description description) {
    }

    public static FileDocumentSourceMatcher isFileDocumentSourceForFile(File file) {
        return new FileDocumentSourceMatcher(file);
    }
}
