package edu.stanford.bmir.protege.web.shared.merge_add;

import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

public class NewOntologyMergeAddAction extends AbstractHasProjectAction<NewOntologyMergeAddResult> {

    private DocumentId documentId;

    private String iri;

    private List<OWLOntologyID> ontologyList;

    private NewOntologyMergeAddAction(){
    }


    public NewOntologyMergeAddAction(ProjectId projectId, DocumentId documentId, String iri, List<OWLOntologyID> ontologyList) {
        super(projectId);
        this.documentId = documentId;
        this.iri = iri;
        this.ontologyList = ontologyList;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public String getIri() {
        return iri;
    }

    public List<OWLOntologyID> getOntologyList() {
        return ontologyList;
    }
}
