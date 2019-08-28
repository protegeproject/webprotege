package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
* @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/03/2014
*/
@SuppressWarnings("Guava")
public class WebProtegeOntologyIRIShortFormProvider extends OntologyIRIShortFormProvider {

    public static final String ROOT_ONTOLOGY_SHORT_FORM = "root-ontology";

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public WebProtegeOntologyIRIShortFormProvider(@Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    @Override
    public String getShortForm(OWLOntology ont) {
        var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
        if(ont.getOntologyID().equals(defaultOntologyId)) {
            return ROOT_ONTOLOGY_SHORT_FORM;
        }
        return super.getShortForm(ont);
    }

    @Nonnull
    public String getShortForm(OWLOntologyID ontologyId) {
        var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
        if(ontologyId.equals(defaultOntologyId)) {
            return ROOT_ONTOLOGY_SHORT_FORM;
        }
        var versionIri = ontologyId.getVersionIRI();
        if(versionIri.isPresent()) {
            return getShortForm(versionIri.get());
        }
        var ontologyIri = ontologyId.getOntologyIRI();
        if(ontologyIri.isPresent()) {
            return getShortForm(ontologyIri.get());
        }
        else {
            return "Anonymous Ontology";
        }
    }

    @Nonnull
    @Override
    public String getShortForm(IRI iri) {
        var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
        if (defaultOntologyId.getOntologyIRI().equals(Optional.of(iri))) {
            return ROOT_ONTOLOGY_SHORT_FORM;
        }
        return super.getShortForm(iri);
    }
}
