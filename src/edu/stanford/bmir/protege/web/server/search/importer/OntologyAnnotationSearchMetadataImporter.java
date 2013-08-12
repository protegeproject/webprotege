package edu.stanford.bmir.protege.web.server.search.importer;

import edu.stanford.bmir.protege.web.server.search.OntologyBasedSearchMDImporter;
import edu.stanford.bmir.protege.web.server.search.SearchMetadataDB;
import edu.stanford.bmir.protege.web.server.search.SearchMetadataImportContext;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import edu.stanford.bmir.protege.web.server.search.SearchMetadata;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class OntologyAnnotationSearchMetadataImporter extends OntologyBasedSearchMDImporter {

    @Override
    public boolean isImporterFor(Set<SearchType> types) {
        return types.contains(SearchType.ANNOTATION_VALUE);
    }

    @Override
    public void generateSearchMetadata(OWLOntology ontology, SearchMetadataImportContext context, SearchMetadataDB db) {
        for (OWLAnnotation annotation : ontology.getAnnotations()) {
            generateSearchMetadataForAnnotation(annotation, ontology, context, db);
        }
    }

    private void generateSearchMetadataForAnnotation(final OWLAnnotation annotation, OWLOntology ontology, final SearchMetadataImportContext context, SearchMetadataDB db) {
        String groupDescription = context.getRendering(annotation.getProperty());
        String rendering = context.getStyledStringRendering(annotation);
//        SearchMetadata md = new SearchMetadata(SearchType.ANNOTATION_VALUE, groupDescription, ontology, context.getRendering(ontology), rendering);
//        db.addResult(md);
        for (OWLAnnotation anno : annotation.getAnnotations()) {
            generateSearchMetadataForAnnotation(anno, ontology, context, db);
        }
    }
}
