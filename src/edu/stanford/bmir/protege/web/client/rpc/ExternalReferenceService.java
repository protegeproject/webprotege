package edu.stanford.bmir.protege.web.client.rpc;

import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalReferenceData;
import edu.stanford.bmir.protege.web.client.rpc.data.extref.ExternalReferenceType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/10/2012
 */
//@RemoteServiceRelativePath("externalreference")
public interface ExternalReferenceService { // extends RemoteService {

    /**
     * Gets the available external reference types for the specified subject.  Each type specifies a way in which the
     * reference may be created.
     * @param referenceSubject The reference subject.
     * @return A possibly empty list of {@link ExternalReferenceType} objects.
     */
    List<ExternalReferenceType> getExternalReferenceTypesForSubject(OWLEntity referenceSubject);

    /**
     * Creates an external reference
     * @param referenceData
     * @param referenceType
     */
    void createExternalReference(BioPortalReferenceData referenceData, ExternalReferenceType referenceType);
}
