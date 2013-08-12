package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class SearchMetadataImportContext implements HasProjectId {

    private OWLAPIProject project;

    public SearchMetadataImportContext(OWLAPIProject project) {
        this.project = project;
    }

    @Override
    public ProjectId getProjectId() {
        return project.getProjectId();
    }

    public Set<OWLOntology> getOntologies() {
        return project.getRootOntology().getImportsClosure();
    }

    public String getRendering(OWLObject object) {
        return project.getRenderingManager().getBrowserText(object);
    }

    public String getStyledStringRendering(OWLObject object) {
        return project.getRenderingManager().getHTMLBrowserText(object);
    }
}
