package edu.stanford.bmir.protege.web.server.mansyntax;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/03/2014
 */
public class WebProtegeOWLOntologyChecker implements OWLOntologyChecker {

    private OWLOntology rootOntology;

    private OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    @Inject
    public WebProtegeOWLOntologyChecker(@RootOntology OWLOntology rootOntology, OntologyIRIShortFormProvider ontologyIRIShortFormProvider) {
        this.rootOntology = rootOntology;
        this.ontologyIRIShortFormProvider = ontologyIRIShortFormProvider;
    }

    @Override
    public OWLOntology getOntology(String s) {
        if(s == null) {
            return rootOntology;
        }
        for (OWLOntology ont : rootOntology.getImportsClosure()) {
            if (s.equals(ontologyIRIShortFormProvider.getShortForm(ont))) {
                return ont;
            }
        }
        return null;
    }
}
