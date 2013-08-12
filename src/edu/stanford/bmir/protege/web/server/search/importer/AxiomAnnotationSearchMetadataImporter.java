package edu.stanford.bmir.protege.web.server.search.importer;

import edu.stanford.bmir.protege.web.server.search.AxiomBasedSearchMetadataImporter;
import edu.stanford.bmir.protege.web.server.search.SearchMetadataDB;
import edu.stanford.bmir.protege.web.server.search.SearchMetadataImportContext;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import edu.stanford.bmir.protege.web.server.search.SearchMetadata;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class AxiomAnnotationSearchMetadataImporter extends AxiomBasedSearchMetadataImporter {

    @Override
    public boolean isImporterFor(AxiomType<?> axiomType, Set<SearchType> types) {
        return types.contains(SearchType.ANNOTATION_VALUE);
    }

    @Override
    public void generateSearchMetadataFor(OWLAxiom axiom, OWLEntity axiomSubject, String axiomSubjectRendering, SearchMetadataImportContext context, SearchMetadataDB db) {
        for (OWLAnnotation annotation : axiom.getAnnotations()) {
            generateSearchMetadataForAnnotation(annotation, axiomSubject, axiomSubjectRendering, context, db);
        }
    }

    private void generateSearchMetadataForAnnotation(final OWLAnnotation annotation, OWLEntity axiomSubject, String axiomSubjectRendering, final SearchMetadataImportContext context, SearchMetadataDB db) {
        String group = context.getRendering(annotation.getProperty());
        String ren = context.getRendering(annotation);
//        SearchMetadata md = new SearchMetadata(SearchType.ANNOTATION_VALUE, group, axiomSubject, axiomSubjectRendering, ren);
//        db.addResult(md);
        for (OWLAnnotation anno : annotation.getAnnotations()) {
            generateSearchMetadataForAnnotation(anno, axiomSubject, axiomSubjectRendering, context, db);
        }
    }
}
