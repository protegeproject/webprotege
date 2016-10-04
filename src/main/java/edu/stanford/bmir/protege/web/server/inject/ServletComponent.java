package edu.stanford.bmir.protege.web.server.inject;

import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.server.BioPortalAPIServiceImpl;
import edu.stanford.bmir.protege.web.server.OBOTextEditorServiceImpl;
import edu.stanford.bmir.protege.web.server.dispatch.impl.DispatchServiceImpl;
import edu.stanford.bmir.protege.web.server.filedownload.FileDownloadServlet;
import edu.stanford.bmir.protege.web.server.filesubmission.FileUploadServlet;
import edu.stanford.bmir.protege.web.server.owlapi.OntologyServiceOWLAPIImpl;

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

    BioPortalAPIServiceImpl getBioPortalAPIService();

    FileDownloadServlet getFileDownloadServlet();

    FileUploadServlet getFileUploadServlet();

}
