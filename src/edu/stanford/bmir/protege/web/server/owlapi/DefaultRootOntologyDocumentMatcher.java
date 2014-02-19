package edu.stanford.bmir.protege.web.server.owlapi;

import java.io.File;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class DefaultRootOntologyDocumentMatcher implements RootOntologyDocumentFileMatcher {

    public static final String ROOT_ONTOLOGY_DOCUMENT_FILE_NAME = "root-ontology.owl";

    @Override
    public boolean isRootOntologyDocument(File file) {
        return file.getName().equals(ROOT_ONTOLOGY_DOCUMENT_FILE_NAME);
    }

    @Override
    public String getErrorMessage() {
        return "The zip file should contain one ontology document named root-ontology.owl";
    }
}
