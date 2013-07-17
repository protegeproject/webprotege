package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
public class CreateInstanceValueChangeFactory extends OWLOntologyChangeFactory {

    private String instName;
    
    private String typeName;
    
    private String subjectEntity;
    
    String propertyEntity;

    public CreateInstanceValueChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String instName, String typeName, String subjectEntity, String propertyEntity) {
        super(project, userId, changeDescription);
        this.instName = instName;
        this.typeName = typeName;
        this.subjectEntity = subjectEntity;
        this.propertyEntity = propertyEntity;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        RenderingManager rm = getRenderingManager();
        OWLNamedIndividual namedIndividual = rm.getEntity(instName, EntityType.NAMED_INDIVIDUAL);
        OWLNamedIndividual subject = rm.getEntity(subjectEntity, EntityType.NAMED_INDIVIDUAL);
        IRI propertyIRI = IRI.create(propertyEntity);
        OWLDataFactory df = getDataFactory();
        if(isAnnotationPropertyIRI(propertyIRI)) {
            OWLAnnotationProperty property = rm.getEntity(propertyEntity, EntityType.ANNOTATION_PROPERTY);
            OWLAnnotationAssertionAxiom annotationAssertionAxiom = df.getOWLAnnotationAssertionAxiom(property, subject.getIRI(), namedIndividual.getIRI());
            changeListToFill.add(new AddAxiom(getRootOntology(), annotationAssertionAxiom));
        }
        else {
            OWLObjectProperty property = rm.getEntity(propertyEntity, EntityType.OBJECT_PROPERTY);
            OWLObjectPropertyAssertionAxiom propertyAssertionAxiom = df.getOWLObjectPropertyAssertionAxiom(property, subject, namedIndividual);
            changeListToFill.add(new AddAxiom(getRootOntology(), propertyAssertionAxiom));

        }
        OWLClass type;
        if (typeName != null) {
            type = rm.getEntity(typeName, EntityType.CLASS);

        }
        else {
            type = getDataFactory().getOWLThing();
        }
        OWLClassAssertionAxiom classAssertionAxiom = df.getOWLClassAssertionAxiom(type, namedIndividual);
        changeListToFill.add(new AddAxiom(getRootOntology(), classAssertionAxiom));


    }

    //// This is too messy

    private boolean isAnnotationPropertyIRI(IRI propertyIRI) {
        return getRootOntology().containsAnnotationPropertyInSignature(propertyIRI) || isBuiltInAnnotationPropertyIRI(propertyIRI) || isSKOSAnnotationPropertyIRI(propertyIRI) || isDublinCoreAnnotationIRI(propertyIRI);
    }

    private boolean isDublinCoreAnnotationIRI(IRI propertyIRI) {
        return DublinCoreVocabulary.ALL_URIS.contains(propertyIRI);
    }

    private boolean isBuiltInAnnotationPropertyIRI(IRI propertyIRI) {
        return OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS.contains(propertyIRI);
    }

    private boolean isSKOSAnnotationPropertyIRI(IRI propertyIRI) {
        return SKOSVocabulary.getAnnotationProperties(getDataFactory()).contains(getDataFactory().getOWLAnnotationProperty(propertyIRI));
    }

}
