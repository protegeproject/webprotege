package edu.stanford.bmir.protege.web.server.owlapi.extref;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/10/2012
 * <p>
 *     An object that describes a term reference.  As a minimum, a term reference consists of an IRI representing
 *     the IRI of the term, and a string representing the preferred label of the term.  Subclasses of this class
 *     defined source specific annotations for the term.
 * </p>
 */
public abstract class ExternalReferenceData {

    private IRI termIRI;

    private String preferredLabel;

    public ExternalReferenceData(IRI termIRI, String termPreferredName) {
        this.termIRI = termIRI;
        this.preferredLabel = termPreferredName;
    }

    /**
     * Gets an IRI which represents the IRI of the term that is referenced.
     * @return The term IRI.  Not <code>null</code>.
     */
    public IRI getTermIRI() {
        return termIRI;
    }

    /**
     * Gets a string that represents the preferred label for the term.
     * @return The term preferred label.  Not <code>null</code>.  The empty string may be returned.
     */
    public String getPreferredLabel() {
        return preferredLabel;
    }


    /**
     * Gets the annotation which would specify the preferred label for the term IRI.
     * @param df A data factory which may be used to create the {@link OWLAnnotation} object.
     * @return The preferred label annotation object.  Not <code>null</code>.
     */
    public OWLAnnotation getPreferredLabelAnnotation(OWLDataFactory df) {
        OWLAnnotationProperty preferredLabelProp = df.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        OWLLiteral preferredLabelValue = df.getOWLLiteral(preferredLabel);
        return df.getOWLAnnotation(preferredLabelProp, preferredLabelValue);
    }

    /**
     * Gets the source (from where the external reference is drawn) specific annotations for the term IRI.  These
     * annotations typically specify provenance information for the term.
     * @param df A data factory which may be used to create the {@link OWLAnnotation} objects.
     * @return A set of source specific annotation.  Not <code>null</code>.  May be empty.
     */
    public abstract Set<OWLAnnotation> getSourceSpecificAnnotations(OWLDataFactory df);

}
