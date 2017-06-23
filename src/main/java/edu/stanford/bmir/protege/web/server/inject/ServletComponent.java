package edu.stanford.bmir.protege.web.server.inject;

import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceImpl;
import edu.stanford.bmir.protege.web.server.download.ProjectDownloadServlet;
import edu.stanford.bmir.protege.web.server.legacy.OntologyServiceOWLAPIImpl;
import edu.stanford.bmir.protege.web.server.upload.FileUploadServlet;

import javax.inject.Singleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
@Subcomponent
@Singleton
public interface ServletComponent {

    DispatchServiceImpl getDispatchService();

    OntologyServiceOWLAPIImpl getOntologyService();

    ProjectDownloadServlet getFileDownloadServlet();

    FileUploadServlet getFileUploadServlet();

}
