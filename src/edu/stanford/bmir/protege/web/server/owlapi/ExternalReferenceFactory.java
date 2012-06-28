package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalReferenceData;
import edu.stanford.bmir.protegex.bp.ref.OntologyEntityConstants;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/06/2012
 * <p>
 *     Creates changes which create an "external reference".  This is basically an IRI representing a term in BioPortal
 *     with some annotations on it. These annotations are as follows:
 *     <ul>
 *
 *     </ul>
 * </p>
 */
public class ExternalReferenceFactory {

    /**
     * An external reference is basically a term that points to
     */
    public static final IRI DEFAULT_REFERENCE_PROPERTY_IRI = OWLRDFVocabulary.RDFS_SEE_ALSO.getIRI();


    // NOTE: The class some of the constants defined in OntologyEntityConstants are ABSOLUTE IRIs and some are RELATIVE
    // IRIs. Check the OntologyEntityConstants before changing anything to do with these constants!

    public static final IRI DEFAULT_PREFERRED_LABEL_PROPERTY_IRI = IRI.create(OntologyEntityConstants.BP_NAMESPACE + OntologyEntityConstants.PROPERTY_PREFERRED_TERM);

    public static final IRI DEFAULT_CONCEPT_SHORT_ID_PROPERTY_IRI = IRI.create(OntologyEntityConstants.BP_NAMESPACE + OntologyEntityConstants.PROPERTY_CONCEPT_ID_SHORT);

    public static final IRI DEFAULT_BIO_PORTAL_TERM_URL_PROPERTY_IRI = IRI.create(OntologyEntityConstants.BP_NAMESPACE + OntologyEntityConstants.PROPERTY_URL);

    /**
     * The
     */
    public static final IRI DEFAULT_ONTOLOGY_ID_PROPERTY_IRI = IRI.create(OntologyEntityConstants.BP_NAMESPACE + OntologyEntityConstants.PROPERTY_ONTOLOGY_ID);

    /**
     * The property that points to the ontology "name".
     */
    public static final IRI DEFAULT_ONTOLOGY_NAME_PROPERTY_IRI = IRI.create(OntologyEntityConstants.BP_NAMESPACE + OntologyEntityConstants.PROPERTY_ONTOLOGY_NAME);


    private enum AnnotationValueType {
        LITERAL,
        IRI
    }

    private IRI referenceSubject;
    
    private OWLAPIProject project;
    
    private BioPortalReferenceData referenceData;

    public ExternalReferenceFactory(OWLAPIProject project, IRI referenceSubject, BioPortalReferenceData referenceData) {
        this.project = project;
        this.referenceSubject = referenceSubject;
        this.referenceData = referenceData;
    }


    public List<OWLOntologyChange> createAddReferenceChanges() {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        OWLDataFactory df = project.getDataFactory();

        // IRI is conceptId
        IRI extRef = IRI.create(referenceData.getConceptId());
        OWLAnnotationProperty referenceProperty = getReferenceProperty();
        OWLAnnotationAssertionAxiom seeAlsoAssertion = df.getOWLAnnotationAssertionAxiom(referenceProperty, referenceSubject, extRef);
        changes.add(new AddAxiom(project.getRootOntology(), seeAlsoAssertion));

        // conceptIdShort - BioPortal term Id I think
        String conceptIdShort = referenceData.getConceptIdShort();
        addAddAxiomChange(getReferenceConceptIdShortProperty(), extRef, conceptIdShort, AnnotationValueType.IRI, changes);

        // Preferred Name
        String preferredName = referenceData.getPreferredName();
        addAddAxiomChange(getReferencePreferredNameProperty(), extRef, preferredName, AnnotationValueType.LITERAL, changes);

        // Ontology name - I think this is the label (not clear :( )
        String ontologyLabel = referenceData.getOntologyName();
        addAddAxiomChange(getReferenceOntologyNameProperty(), extRef, ontologyLabel, AnnotationValueType.LITERAL, changes);

        // Ontology ID - BioPortal version id, which is an integer - or it appears to be
        String ontologyId = referenceData.getOntologyVersionId();
        addAddAxiomChange(getReferenceOntologyVersionIdProperty(), extRef, ontologyId, AnnotationValueType.LITERAL, changes);

        // URL (Points to the BioPortal Rest URL)
        String bioportalURL = referenceData.getBpUrl();
        addAddAxiomChange(getReferenceBioPortalTermURLProperty(), extRef, bioportalURL, AnnotationValueType.IRI, changes);

        return changes;
    }
    
    
    private void addAddAxiomChange(OWLAnnotationProperty property, IRI subject, String value, AnnotationValueType valueType, List<OWLOntologyChange> changes) {
        if(value == null) {
            return;
        }
        if(property == null) {
            throw new NullPointerException("property must not be null");
        }
        if(valueType == null) {
            throw new NullPointerException("valueType must not be null");
        }
        if(changes == null) {
            throw new NullPointerException("changes must not be null");
        }
        
        OWLDataFactory df = project.getDataFactory();
        OWLAnnotationValue annotationValue;
        if(valueType == AnnotationValueType.LITERAL) {
            annotationValue = df.getOWLLiteral(value);
        }
        else {
            annotationValue = IRI.create(value);
        }
        OWLAnnotationAssertionAxiom ax = df.getOWLAnnotationAssertionAxiom(property, subject, annotationValue);
        changes.add(new AddAxiom(project.getRootOntology(), ax));
    }

    /**
     * Gets the annotation property for "pointing to" external references. The default is defined by the
     * {@link #DEFAULT_REFERENCE_PROPERTY_IRI} constant.
     * @return The external references annotation property.  Not null.
     */
    public OWLAnnotationProperty getReferenceProperty() {
        return getReferenceAnnotationProperty(referenceData.getReferencePropertyName(), DEFAULT_REFERENCE_PROPERTY_IRI);
    }


    /**
     * Gets the property which labels the external reference.  The default is defined by the
     * {@link #DEFAULT_PREFERRED_LABEL_PROPERTY_IRI} constant.
     * @return The external reference labelling property.  Not null.
     */
    public OWLAnnotationProperty getReferencePreferredNameProperty() {
        return getReferenceAnnotationProperty(referenceData.getPreferredLabelPropertyName(), DEFAULT_PREFERRED_LABEL_PROPERTY_IRI);
    }

    /**
     * Gets the property that should be used to provide a short id for the external reference. The default is defined by
     * the {@link #DEFAULT_CONCEPT_SHORT_ID_PROPERTY_IRI} constant.
     * @return The annotation property corresponding to the label property for an external reference. Not null.
     */
    public OWLAnnotationProperty getReferenceConceptIdShortProperty() {
        return project.getDataFactory().getOWLAnnotationProperty(DEFAULT_CONCEPT_SHORT_ID_PROPERTY_IRI);
    }

    /**
     * Gets the annotation property that should be used to point to the URL that displays the external reference
     * in the bioportal site.
     * @return The annotation property that should be used to point to term in BioPortal.  By default this is
     * rdfs:seeAlso.  Not null.
     */
    private OWLAnnotationProperty getReferenceBioPortalTermURLProperty() {
        return getReferenceAnnotationProperty(referenceData.getUrlPropertyName(), DEFAULT_BIO_PORTAL_TERM_URL_PROPERTY_IRI);
    }


    /**
     * Gets the reference ontology version id.  The default is defined by the {@link #DEFAULT_ONTOLOGY_ID_PROPERTY_IRI}
     * constant.
     * @return The annotation property that should be used to point to the ontology from where the term is drawn. Not null.
     */
    private OWLAnnotationProperty getReferenceOntologyVersionIdProperty() {
        return getReferenceAnnotationProperty(referenceData.getOntologyIdPropertyName(), DEFAULT_ONTOLOGY_ID_PROPERTY_IRI);
    }

    /**
     * Gets the property that points to the name of the ontology (the bioportal name of the ontology).
     * @return The property. Not null.  By default this is defined by the {@link #DEFAULT_ONTOLOGY_NAME_PROPERTY_IRI}
     */
    private OWLAnnotationProperty getReferenceOntologyNameProperty() {
        return getReferenceAnnotationProperty(referenceData.getOntologyNamePropertyName(), DEFAULT_ONTOLOGY_NAME_PROPERTY_IRI);
    }





    /**
     * Gets an annotation property to be used to annotated an external reference.
     * @param propertyIRI A string that specifies the name, or rather IRI, of the property.  This may be null.
     * @param defaultPropertyIRI The default property IRI to use if the propertyIRI parameter is null.
     * @return An annotation property that has an IRI that is equal to the IRI created from the propertyIRI parameter
     * if this parameter is not null, or an annotation property that is equal to defaultProperty.
     * @throws NullPointerException if defaultProperty is null.
     */
    private OWLAnnotationProperty getReferenceAnnotationProperty(String propertyIRI, IRI defaultPropertyIRI) {
        if (defaultPropertyIRI == null) {
            throw new NullPointerException("defaultPropertyIRI must not be null");
        }
        OWLDataFactory df = project.getDataFactory();
        if(propertyIRI == null) {
            return df.getOWLAnnotationProperty(defaultPropertyIRI);
        }
        IRI iri = IRI.create(propertyIRI);
        return df.getOWLAnnotationProperty(iri);
    }
}
