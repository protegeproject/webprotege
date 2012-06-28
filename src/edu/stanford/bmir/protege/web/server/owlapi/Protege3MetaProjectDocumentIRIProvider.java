package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.server.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.Protege3ProjectManager;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import org.semanticweb.owlapi.model.IRI;

import java.io.File;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class Protege3MetaProjectDocumentIRIProvider implements DocumentIRIProvider {

    public IRI getDocumentIRI(ProjectId projectId) {
        Protege3ProjectManager pm = Protege3ProjectManager.getProjectManager();
        MetaProjectManager mpm = pm.getMetaProjectManager();
        MetaProject metaProject = mpm.getMetaProject();
        ProjectInstance pi = metaProject.getProject(projectId.getProjectName());
        String location = pi.getLocation();
        File fileSystemLocation = new File(location.substring(0, location.length() - ".pprj".length()));
        return IRI.create(fileSystemLocation);
    }
}
