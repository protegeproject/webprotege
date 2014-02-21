package edu.stanford.bmir.protege.web.server.owlapi;

import java.io.File;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public interface RootOntologyDocumentFileMatcher {

    boolean isRootOntologyDocument(File file);

    String getErrorMessage();
}
