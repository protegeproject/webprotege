package edu.stanford.bmir.protege.web.server.search.importer;

import edu.stanford.bmir.protege.web.server.search.EntityBasedSearchMDImporter;
import edu.stanford.bmir.protege.web.server.search.SearchMetadata;
import edu.stanford.bmir.protege.web.server.search.SearchMetadataDB;
import edu.stanford.bmir.protege.web.server.search.SearchMetadataImportContext;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class EntityAnnotationValueSearchMetadataImporter extends EntityBasedSearchMDImporter {

    @Override
    public boolean isImporterFor(Set<SearchType> types) {
        return types.contains(SearchType.ANNOTATION_VALUE);
    }

    @Override
    public void generateSearchMetadataFor(OWLEntity entity, String entityRendering, final SearchMetadataImportContext context, SearchMetadataDB db) {
        for (OWLOntology ontology : context.getOntologies()) {
            for (final OWLAnnotation annotation : entity.getAnnotations(ontology)) {
                String groupDescription = context.getRendering(annotation.getProperty());
                String ren = context.getRendering(annotation);
//                SearchMetadata md = new SearchMetadata(SearchType.ANNOTATION_VALUE, groupDescription, entity, entityRendering, ren);
//                db.addResult(md);
            }
        }
    }
}
