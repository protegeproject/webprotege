package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface OBOTextEditorServiceAsync {


    void getSubsets(AsyncCallback<Set<OBOTermSubset>> async);

    void addSubset(OBOTermSubset subset, AsyncCallback<Void> async);

    void removeSubset(OBOTermSubset subset, AsyncCallback<Void> async);

    void getRelationships(ProjectId projectId, OWLClass term, AsyncCallback<OBOTermRelationships> async);

    void getCrossProduct(ProjectId projectId, OWLClass term, AsyncCallback<OBOTermCrossProduct> async);

    void getDefinition(ProjectId projectId, OWLEntity term, AsyncCallback<OBOTermDefinition> async);

    void getTermId(ProjectId projectId, OWLEntity entity, AsyncCallback<OBOTermId> async);

    void getNamespaces(ProjectId projectId, AsyncCallback<Set<OBONamespace>> async);

    void getSynonyms(ProjectId projectId, OWLEntity term, AsyncCallback<Collection<OBOTermSynonym>> async);


    void setRelationships(ProjectId projectId, OWLClass lastEntity, OBOTermRelationships relationships, AsyncCallback<Void> async);

    void setTermId(ProjectId projectId, OWLEntity lastEntity, OBOTermId termId, AsyncCallback<Void> async);

    void setDefinition(ProjectId projectId, OWLEntity term, OBOTermDefinition definition, AsyncCallback<Void> async);

    void setSynonyms(ProjectId projectId, OWLEntity term, Collection<OBOTermSynonym> synonyms, AsyncCallback<Void> async);

    void setCrossProduct(ProjectId projectId, OWLClass term, OBOTermCrossProduct crossProduct, AsyncCallback<Void> async);

    void getXRefs(ProjectId projectId, OWLEntity term, AsyncCallback<List<OBOXRef>> async);

    void setXRefs(ProjectId projectId, OWLEntity term, List<OBOXRef> xrefs, AsyncCallback<Void> async);
}
