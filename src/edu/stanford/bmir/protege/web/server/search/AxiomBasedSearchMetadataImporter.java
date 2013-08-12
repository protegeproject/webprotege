package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.shared.search.SearchType;
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
public abstract class AxiomBasedSearchMetadataImporter {

    public abstract boolean isImporterFor(AxiomType<?> axiomType, Set<SearchType> types);

    public abstract void generateSearchMetadataFor(OWLAxiom axiom, OWLEntity axiomSubject, String axiomSubjectRendering, SearchMetadataImportContext context, SearchMetadataDB db);
}
