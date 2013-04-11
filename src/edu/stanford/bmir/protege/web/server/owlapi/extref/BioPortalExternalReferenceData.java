package edu.stanford.bmir.protege.web.server.owlapi.extref;

import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalReferenceData;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/10/2012
 */
public class BioPortalExternalReferenceData extends ExternalReferenceData {


    private static final String BIOPORTAL_NAMESPACE = "http://bioportal.bioontology.org#";

    public static final IRI DEFAULT_CONCEPT_SHORT_ID_PROPERTY_IRI = IRI.create(BIOPORTAL_NAMESPACE + "shortTermId");

    public static final IRI DEFAULT_BIO_PORTAL_TERM_URL_PROPERTY_IRI = IRI.create(BIOPORTAL_NAMESPACE + "url");

    public static final IRI DEFAULT_ONTOLOGY_ID_PROPERTY_IRI = IRI.create(BIOPORTAL_NAMESPACE + "ontologyId");

    /**
     * The property that points to the ontology "name".
     */
    public static final IRI DEFAULT_ONTOLOGY_NAME_PROPERTY_IRI = IRI.create(BIOPORTAL_NAMESPACE + "ontologyLabel");


    private enum AnnotationValueType {
        LITERAL,
        IRI
    }

    private OWLAPIProject project;

    private BioPortalReferenceData referenceData;


    public BioPortalExternalReferenceData(OWLAPIProject project, BioPortalReferenceData referenceData) {
        super(IRI.create(referenceData.getConceptId()), referenceData.getPreferredName());
        this.project = project;
        this.referenceData = referenceData;
    }

    @Override
    public Set<OWLAnnotation> getSourceSpecificAnnotations(OWLDataFactory df) {
        Set<OWLAnnotation> result = new HashSet<OWLAnnotation>();

        //  String conceptIdShort = referenceData.getConceptIdShort();
        String conceptIdShort = referenceData.getConceptIdShort();
        result.addAll(getAnnotations(getReferenceConceptIdShortProperty(), conceptIdShort, AnnotationValueType.IRI));

        // Ontology name - I think this is the label (not clear :( )
        String ontologyName = referenceData.getOntologyName();
        result.addAll(getAnnotations(getReferenceOntologyNameProperty(), ontologyName, AnnotationValueType.LITERAL));

        // Ontology ID - BioPortal version id, which is an integer - or it appears to be
        String ontologyVersionId = referenceData.getOntologyVersionId();
        result.addAll(getAnnotations(getReferenceOntologyVersionIdProperty(), ontologyVersionId, AnnotationValueType.LITERAL));

        // URL (Points to the BioPortal Rest URL)
        String bpUrl = referenceData.getBpUrl();
        result.addAll(getAnnotations(getReferenceBioPortalTermURLProperty(), bpUrl, AnnotationValueType.IRI));

        return result;
    }

    private Set<OWLAnnotation> getAnnotations(OWLAnnotationProperty property, String value, AnnotationValueType valueType) {
        if(value == null) {
            return Collections.emptySet();
        }
        if(property == null) {
            throw new NullPointerException("property must not be null");
        }
        if(valueType == null) {
            throw new NullPointerException("valueType must not be null");
        }

        OWLDataFactory df = project.getDataFactory();
        OWLAnnotationValue annotationValue;
        if(valueType == AnnotationValueType.LITERAL) {
            annotationValue = df.getOWLLiteral(value);
        }
        else {
            annotationValue = IRI.create(value);
        }
        OWLAnnotation result = df.getOWLAnnotation(property, annotationValue);
        return Collections.singleton(result);
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
