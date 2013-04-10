package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionSummary;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.text.DateFormat;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/10/2012
 */
public class RevisionMetadataAnnotater {

    private ProjectId projectId;
    
    private RevisionNumber revisionNumber;
    
    private OWLOntology ontologyToAnnotate;

    public RevisionMetadataAnnotater(ProjectId projectId, RevisionNumber revisionNumber, OWLOntology ontologyToAnnotate) {
        this.projectId = projectId;
        this.revisionNumber = revisionNumber;
        this.ontologyToAnnotate = ontologyToAnnotate;
    }
    
    public List<OWLOntologyChange> getChanges() {
        List<OWLOntologyChange> result =  new ArrayList<OWLOntologyChange>();
        OWLAnnotation revisionAnnotation = getRevisionAnnotation();
        result.add(new AddOntologyAnnotation(ontologyToAnnotate, revisionAnnotation));
        result.add(new AddOntologyAnnotation(ontologyToAnnotate, getDescriptionAnnotation()));
        Set<OWLAnnotation> creatorAnnotations = getCreatorAnnotations();
        for(OWLAnnotation annotation : creatorAnnotations) {
            result.add(new AddOntologyAnnotation(ontologyToAnnotate, annotation));
        }
        result.add(new AddOntologyAnnotation(ontologyToAnnotate, getRevisionDateAnnotation()));
        result.add(new AddOntologyAnnotation(ontologyToAnnotate, getCommentAnnotation()));
        return result;
    }
    
    
    private OWLAnnotation getCommentAnnotation() {
        OWLAPIProject project = getProject();
        OWLDataFactory df = project.getDataFactory();
        return df.getOWLAnnotation(df.getRDFSComment(), df.getOWLLiteral("This ontology was generated from an ontology revision in webprotege. See http://webprotege-beta.stanford.edu"));
    }


 
    private Set<OWLAnnotation> getCreatorAnnotations() {

        OWLAPIProject project = getProject();
        Set<String> authorNames = getRevisionAuthorNames();
        Set<OWLAnnotation> result = new HashSet<OWLAnnotation>();
        for(String authorName : authorNames) {
            OWLDataFactory df = project.getDataFactory();
            OWLAnnotation creatorAnnotation = df.getOWLAnnotation(creatorProperty(df), df.getOWLLiteral(authorName));
            result.add(creatorAnnotation);
        }
        return result;
    }

    private Set<String> getRevisionAuthorNames() {
        OWLAPIProject project = getProject();
        OWLAPIChangeManager changeManager = project.getChangeManager();
        List<RevisionSummary> revisionSummaries = changeManager.getRevisionSummaries();
        Set<String> authorNames = new HashSet<String>();
        for(RevisionSummary revisionSummary : revisionSummaries) {
            if(revisionSummary.getRevisionNumber().compareTo(revisionNumber) > 0) {
                break;
            }
            authorNames.add(revisionSummary.getUserId().getUserName());
        }
        return authorNames;
    }

    private OWLAnnotation getRevisionDateAnnotation() {
        OWLAPIProject project = getProject();
        OWLAPIChangeManager changeManager = project.getChangeManager();
        RevisionSummary summary = changeManager.getRevisionSummary(revisionNumber);
        long timestamp = summary.getTimestamp();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String date = dateFormat.format(new Date(timestamp));
        OWLDataFactory df = project.getDataFactory();
        return df.getOWLAnnotation(df.getOWLAnnotationProperty(DublinCoreVocabulary.DATE.getIRI()), df.getOWLLiteral(date, OWL2Datatype.XSD_DATE_TIME));
    }


    private OWLAnnotationProperty creatorProperty(OWLDataFactory df) {
        return df.getOWLAnnotationProperty(DublinCoreVocabulary.CREATOR.getIRI());
    }

    private OWLAnnotation getRevisionAnnotation() {
        OWLAPIProject project = getProject();
        OWLDataFactory df = project.getDataFactory();
        OWLAnnotationProperty revisionAnnotationProperty = df.getOWLAnnotationProperty(IRI.create("http://protege.stanford.edu/webprotege/revision"));
        return df.getOWLAnnotation(revisionAnnotationProperty, df.getOWLLiteral(revisionNumber.getValueAsInt()));
    }

    private OWLAPIProject getProject() {
        return OWLAPIProjectManager.getProjectManager().getProject(projectId);
    }

    private OWLAnnotation getDescriptionAnnotation() {
        OWLAPIProject project = getProject();
        OWLAPIProjectMetadataManager metadataManager = OWLAPIProjectMetadataManager.getManager();
        String description = metadataManager.getDescription(projectId);
        OWLDataFactory df = project.getDataFactory();
        return df.getOWLAnnotation(df.getRDFSComment(), df.getOWLLiteral(description));
    }

}
