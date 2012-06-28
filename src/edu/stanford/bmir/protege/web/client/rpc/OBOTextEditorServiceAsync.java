package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.*;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Cls;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.WebProtegeIRI;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface OBOTextEditorServiceAsync {


    void getSubsets(AsyncCallback<Set<OBOTermSubset>> async);

    void addSubset(OBOTermSubset subset, AsyncCallback<Void> async);

    void removeSubset(OBOTermSubset subset, AsyncCallback<Void> async);

    void getRelationships(ProjectId projectId, Cls term, AsyncCallback<OBOTermRelationships> async);

    void getCrossProduct(ProjectId projectId, Cls term, AsyncCallback<OBOTermCrossProduct> async);

    void getDefinition(ProjectId projectId, Entity term, AsyncCallback<OBOTermDefinition> async);

    void getTermId(ProjectId projectId, Entity entity, AsyncCallback<OBOTermId> async);

    void getNamespaces(ProjectId projectId, AsyncCallback<Set<OBONamespace>> async);

    void getSynonyms(ProjectId projectId, Entity term, AsyncCallback<Collection<OBOTermSynonym>> async);


    void setRelationships(ProjectId projectId, Cls lastEntity, OBOTermRelationships relationships, AsyncCallback<Void> async);

    void setTermId(ProjectId projectId, Entity lastEntity, OBOTermId termId, AsyncCallback<Void> async);

    void setDefinition(ProjectId projectId, Entity term, OBOTermDefinition definition, AsyncCallback<Void> async);

    void setSynonyms(ProjectId projectId, Entity term, Collection<OBOTermSynonym> synonyms, AsyncCallback<Void> async);

    void setCrossProduct(ProjectId projectId, Cls term, OBOTermCrossProduct crossProduct, AsyncCallback<Void> async);

    void getXRefs(ProjectId projectId, Entity term, AsyncCallback<List<OBOXRef>> async);

    void setXRefs(ProjectId projectId, Entity term, List<OBOXRef> xrefs, AsyncCallback<Void> async);
}
