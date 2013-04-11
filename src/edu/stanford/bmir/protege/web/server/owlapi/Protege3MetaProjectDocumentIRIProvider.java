package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.server.MetaProjectManager;
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
        MetaProjectManager mpm = MetaProjectManager.getManager();
        MetaProject metaProject = mpm.getMetaProject();
        ProjectInstance pi = metaProject.getProject(projectId.getId());
        String location = pi.getLocation();
        File fileSystemLocation = new File(location.substring(0, location.length() - ".pprj".length()));
        return IRI.create(fileSystemLocation);
    }
}
