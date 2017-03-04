package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.shared.user.NotSignedInException;
import edu.stanford.bmir.protege.web.shared.obo.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

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
@Deprecated
@RemoteServiceRelativePath("obotexteditorservice")
public interface OBOTextEditorService extends RemoteService {
    
    Set<OBONamespace> getNamespaces(ProjectId projectId);
    

    OBOTermId getTermId(ProjectId projectId, OWLEntity entity);

    void setTermId(ProjectId projectId, OWLEntity lastEntity, OBOTermId termId) throws NotSignedInException;

    

    Set<OBOTermSubset> getSubsets();

    void addSubset(OBOTermSubset subset);
    
    void removeSubset(OBOTermSubset subset);
    
    
    List<OBOXRef> getXRefs(ProjectId projectId, OWLEntity term);
    
    void setXRefs(ProjectId projectId, OWLEntity term, List<OBOXRef> xrefs) throws NotSignedInException;


    OBOTermDefinition getDefinition(ProjectId projectId, OWLEntity term);

    void setDefinition(ProjectId projectId, OWLEntity term, OBOTermDefinition definition) throws NotSignedInException;

    Collection<OBOTermSynonym> getSynonyms(ProjectId projectId, OWLEntity term);

    void setSynonyms(ProjectId projectId, OWLEntity term, Collection<OBOTermSynonym> synonyms) throws NotSignedInException;



    OBOTermRelationships getRelationships(ProjectId projectId, OWLClass term);

    void setRelationships(ProjectId projectId, OWLClass lastEntity, OBOTermRelationships relationships) throws NotSignedInException;


    
    OBOTermCrossProduct getCrossProduct(ProjectId projectId, OWLClass term);

    void setCrossProduct(ProjectId projectId, OWLClass term, OBOTermCrossProduct crossProduct) throws NotSignedInException;

}
