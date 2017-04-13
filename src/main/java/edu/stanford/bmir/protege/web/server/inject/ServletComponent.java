package edu.stanford.bmir.protege.web.server.inject;

import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.server.obo.OBOTextEditorServiceImpl;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceImpl;
import edu.stanford.bmir.protege.web.server.download.FileDownloadServlet;
import edu.stanford.bmir.protege.web.server.upload.FileUploadServlet;
import edu.stanford.bmir.protege.web.server.legacy.OntologyServiceOWLAPIImpl;

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

    OBOTextEditorServiceImpl getOBOTextEditorService();

    FileDownloadServlet getFileDownloadServlet();

    FileUploadServlet getFileUploadServlet();

}
