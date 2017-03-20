package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;

import java.util.List;


/**
 * A service for accessing ontology data.
 * <p />
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
@Deprecated
@RemoteServiceRelativePath("ontology")
public interface OntologyService extends RemoteService {

    List<SubclassEntityData> getSubclasses(String projectName, String className);

    List<EntityData> moveCls(String projectName,
                             String clsName,
                             String oldParentName,
                             String newParentName,
                             boolean checkForCycles,
                             String user,
                             String operationDescription);

    List<EntityData> getSubproperties(String projectName, String propertyName);

    List<EntityData> getPathToRoot(String projectName, String entityName);

}
