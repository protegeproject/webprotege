package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalReferenceData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/06/2012
 */
public class AddExternalReferenceChangeFactory extends OWLOntologyChangeFactory {

    private static final String BIOPORTAL_METADATA_BASE = "http://bioportal.bioontology.org/metadata/def/";
    
    private String entityName;
    
    private BioPortalReferenceData referenceData;
    
    public AddExternalReferenceChangeFactory(OWLAPIProject project, String entityName, BioPortalReferenceData referenceData, UserId userId, String highLevelDescription) {
        super(project, userId, highLevelDescription);
        this.entityName = entityName;
        this.referenceData = referenceData;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        OWLAPIProject project = getProject();
        OWLDataFactory df = project.getDataFactory();

        IRI subject = IRI.create(entityName);

        ExternalReferenceFactory refFac = new ExternalReferenceFactory(getProject(), subject, referenceData);
        changeListToFill.addAll(refFac.createAddReferenceChanges());


    }
}
