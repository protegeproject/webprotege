package edu.stanford.bmir.protege.web.server.owlapi.extref;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/10/2012
 */
public class ExternalReferenceSubClassStrategy extends ExternalReferenceStrategy<OWLClass> {

    public static final String DESCRIPTION = "SubClass";

    @Override
    public String getAddTermAsDescription() {
        return DESCRIPTION;
    }

    @Override
    public EntityType<OWLClass> getSubjectType() {
        return EntityType.CLASS;
    }

    @Override
    protected List<OWLOntologyChange> generateStrategySpecificOntologyChanges(OWLClass referenceSubject, ExternalReferenceData externalReferenceData, OWLAPIProject project) {
        List<OWLOntologyChange> result = new ArrayList<OWLOntologyChange>();
        OWLDataFactory df = project.getDataFactory();
        IRI clsIRI = externalReferenceData.getTermIRI();
        OWLClass cls = df.getOWLClass(clsIRI);
        OWLOntology rootOntology = project.getRootOntology();
        OWLDeclarationAxiom declarationAxiom = df.getOWLDeclarationAxiom(cls);
        result.add(new AddAxiom(rootOntology, declarationAxiom));
        OWLSubClassOfAxiom subClassAxiom = df.getOWLSubClassOfAxiom(cls, referenceSubject);
        result.add(new AddAxiom(rootOntology, subClassAxiom));
        return result;
    }
}
