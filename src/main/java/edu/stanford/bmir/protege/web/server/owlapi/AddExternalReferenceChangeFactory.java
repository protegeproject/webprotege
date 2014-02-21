package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalReferenceData;
import edu.stanford.bmir.protege.web.server.owlapi.extref.BioPortalExternalReferenceData;
import edu.stanford.bmir.protege.web.server.owlapi.extref.ExternalReferenceStrategy;
import edu.stanford.bmir.protege.web.server.owlapi.extref.ExternalReferenceSubClassStrategy;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/06/2012
 */
public class AddExternalReferenceChangeFactory extends OWLOntologyChangeFactory {


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

        ExternalReferenceStrategy<OWLClass> strategy = new ExternalReferenceSubClassStrategy();
        IRI subjectIRI = IRI.create(entityName);
        OWLClass subjectCls = df.getOWLClass(subjectIRI);
        List<OWLOntologyChange> changes = strategy.generateOntologyChanges(subjectCls, new BioPortalExternalReferenceData(project, referenceData), project);
        changeListToFill.addAll(changes);


    }
}
