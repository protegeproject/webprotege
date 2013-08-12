package edu.stanford.bmir.protege.web.server.search.importer;

import edu.stanford.bmir.protege.web.server.search.AxiomBasedSearchMetadataImporter;
import edu.stanford.bmir.protege.web.server.search.SearchMetadataDB;
import edu.stanford.bmir.protege.web.server.search.SearchMetadataImportContext;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import edu.stanford.bmir.protege.web.server.search.SearchMetadata;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/06/2013
 */
public class LogicalAxiomRenderingSearchMetadataImporter extends AxiomBasedSearchMetadataImporter {

    @Override
    public boolean isImporterFor(AxiomType<?> axiomType, Set<SearchType> types) {
        return axiomType.isLogical() && types.contains(SearchType.LOGICAL_AXIOM);
    }

    @Override
    public void generateSearchMetadataFor(final OWLAxiom axiom, OWLEntity axiomSubject, String axiomSubjectRendering, final SearchMetadataImportContext context, SearchMetadataDB db) {
        String rendering = context.getRendering(axiom);
        String groupDescription = axiom.getAxiomType().getName();
//        SearchMetadata md = new SearchMetadata(SearchType.LOGICAL_AXIOM, groupDescription, axiomSubject, axiomSubjectRendering, rendering);
//        db.addResult(md);
    }
}
