package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class RawProjectSourcesImporter {

    private OWLOntologyManager manager;

    private OWLOntologyLoaderConfiguration loaderConfig;

    public RawProjectSourcesImporter(OWLOntologyManager manager, OWLOntologyLoaderConfiguration loaderConfig) {
        this.manager = manager;
        this.loaderConfig = loaderConfig;
    }

    public void importRawProjectSources(RawProjectSources projectSources) throws OWLOntologyCreationException {
        try {
            manager.addIRIMapper(projectSources.getOntologyIRIMapper());
            for (OWLOntologyDocumentSource documentSource : projectSources.getDocumentSources()) {
                manager.loadOntologyFromOntologyDocument(documentSource, loaderConfig);
            }
        } finally {
            manager.removeIRIMapper(projectSources.getOntologyIRIMapper());
        }
    }

}
