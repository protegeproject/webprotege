package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public abstract class OntologyBasedSearchMDImporter extends SearchMDImporter {

    public abstract boolean isImporterFor(Set<SearchType> types);

    public abstract void generateSearchMetadata(OWLOntology ontology, SearchMetadataImportContext context, SearchMetadataDB db);
}
