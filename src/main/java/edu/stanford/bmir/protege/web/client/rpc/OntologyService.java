package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.*;

import java.util.List;


/**
 * A service for accessing ontology data.
 * <p />
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
@RemoteServiceRelativePath("ontology")
public interface OntologyService extends RemoteService {

    /*
     * Project management methods
     */

    public ImportsData getImportedOntologies(String projectName);

    public EntityData getRootEntity(String projectName);

    public EntityData getEntity(String projectName, String entityName);

    public List<SubclassEntityData> getSubclasses(String projectName, String className);

    public List<EntityData> moveCls(String projectName, String clsName, String oldParentName, String newParentName, boolean checkForCycles,
            String user, String operationDescription);

    public List<EntityData> getParents(String projectName, String className, boolean direct);

    /*
     * Properties methods
     */

    public List<EntityData> getSubproperties(String projectName, String propertyName);

    public PaginationData<EntityData> search(String projectName, String searchString, ValueType valueType, int start, int limit, String sort, String dir);

    public List<EntityData> search(String projectName, String searchString);

    public List<EntityData> search(String projectName, String searchString, ValueType valueType);

    public List<EntityData> getPathToRoot(String projectName, String entityName);

}
