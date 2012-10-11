package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.*;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.NamedClass;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 * <p>
 *     The seemingly strange name for this service comes from the "text editor" view in OBOEdit.  It provides information
 *     for OBO ontologies so that definitions, synonyms and DBXrefs associated with this things can be viewed and edited.
 *     For more information on this attributes, take a look at <a href="http://oboedit.org/docs/html/Text_Editing.htm">Text Editing in OBO Edit</a>
 *     which provides a very nice summary of the functionality.
 * </p>
 */
@RemoteServiceRelativePath("obotexteditorservice")
public interface OBOTextEditorService extends RemoteService {
    
    Set<OBONamespace> getNamespaces(ProjectId projectId);
    

    OBOTermId getTermId(ProjectId projectId, Entity entity);

    void setTermId(ProjectId projectId, Entity lastEntity, OBOTermId termId) throws NotSignedInException;

    

    Set<OBOTermSubset> getSubsets();

    void addSubset(OBOTermSubset subset);
    
    void removeSubset(OBOTermSubset subset);
    
    
    List<OBOXRef> getXRefs(ProjectId projectId, Entity term);
    
    void setXRefs(ProjectId projectId, Entity term, List<OBOXRef> xrefs) throws NotSignedInException;
    
//    Set<OBOTermSynonymScope> getSynonymScopes();
//
//    void addSynonymScope(OBOTermSynonymScope synonymScope);
//
//    void removeSynonymScope(OBOTermSynonymScope synonymScope);



    OBOTermDefinition getDefinition(ProjectId projectId, Entity term);

    void setDefinition(ProjectId projectId, Entity term, OBOTermDefinition definition) throws NotSignedInException;

    Collection<OBOTermSynonym> getSynonyms(ProjectId projectId, Entity term);

    void setSynonyms(ProjectId projectId, Entity term, Collection<OBOTermSynonym> synonyms) throws NotSignedInException;



    OBOTermRelationships getRelationships(ProjectId projectId, NamedClass term);

    void setRelationships(ProjectId projectId, NamedClass lastEntity, OBOTermRelationships relationships) throws NotSignedInException;


    
    OBOTermCrossProduct getCrossProduct(ProjectId projectId, NamedClass term);

    void setCrossProduct(ProjectId projectId, NamedClass term, OBOTermCrossProduct crossProduct) throws NotSignedInException;

}
