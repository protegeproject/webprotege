package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.OntologyService;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.PaginationServerUtil;
import edu.stanford.bmir.protege.web.server.URLUtil;
import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.hierarchy.AssertedClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLDataPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.smi.protege.util.Log;
import org.ncbo.stanford.bean.concept.ClassBean;
import org.ncbo.stanford.util.BioPortalServerConstants;
import org.ncbo.stanford.util.BioPortalUtil;
import org.ncbo.stanford.util.BioportalConcept;
import org.ncbo.stanford.util.HTMLUtil;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Level;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2012
 */
public class OntologyServiceOWLAPIImpl extends WebProtegeRemoteServiceServlet implements OntologyService {

    /**
     * The default property hierarchy in WP has a node called "RootPropertyNode".  However, there doesn't seem to be
     * a constant for this, hence we've got one here.
     */
    public static final String PROPERTY_HIERARCHY_ROOT_NODE_NAME = "RootPropertyNode";

    /**
     * A constant for the node for annotation properties
     */
    public static final String ANNOTATION_PROPERTIES_ROOT_NAME = "Annotation properties";

    /**
     * The root node for annotation properties
     */
    public static final PropertyEntityData ANNOTATION_PROPERTIES_ROOT = new PropertyEntityData(ANNOTATION_PROPERTIES_ROOT_NAME);



    static {
        ANNOTATION_PROPERTIES_ROOT.setValueType(ValueType.Property);
        ANNOTATION_PROPERTIES_ROOT.setPropertyType(PropertyType.ANNOTATION);
        ANNOTATION_PROPERTIES_ROOT.setBrowserText(ANNOTATION_PROPERTIES_ROOT_NAME);
    }

    private OWLAPIProjectManager projectManager;

    public OntologyServiceOWLAPIImpl() {
        projectManager = WebProtegeInjector.get().getInstance(OWLAPIProjectManager.class);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////
    ///////
    ///////  Convenience Methods
    ///////
    ///////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the OWLAPIProject for a given project name.  If a project with the specified name exists then that project
     * will be returned, otherwise, a fresh project will be created and that fresh project returned.
     * @param projectName The name of the project.
     * @return The OWL API project. Not <code>null</code>.
     */
    private OWLAPIProject getProject(String projectName) {
        if (projectName == null) {
            throw new NullPointerException("projectName must not be null");
        }
        ProjectId projectId = ProjectId.get(projectName);
        // TODO: Log
        return getProject(projectId);
    }

    /**
     * Gets the OWLAPIProject for a given {@link ProjectId}.If a project with the specified id exists then that project
     * will be returned, otherwise, a fresh project will be created and that fresh project returned.
     * @param projectId The id of the project.
     * @return The OWL API project. Not <code>null</code>.
     */
    private OWLAPIProject getProject(ProjectId projectId) {
        return projectManager.getProject(projectId);
    }

    private RenderingManager getRenderingManager(String projectName) {
        OWLAPIProject project = getProject(projectName);
        return project.getRenderingManager();
    }

    private RenderingManager getRenderingManager(ProjectId projectId) {
        OWLAPIProject project = getProject(projectId);
        return project.getRenderingManager();
    }


    /**
     * Gets the root ontology for a given project name.
     * @param projectName The name of the project.
     * @return The root ontology. Not <code>null</code>.
     */
    private OWLOntology getOntology(String projectName) {
        ProjectId projectId = ProjectId.get(projectName);
        return projectManager.getProject(projectId).getRootOntology();
    }

    private String toName(OWLOntologyID id) {
        if (id.isAnonymous()) {
            return id.toString();
        }
        else {
            return id.getOntologyIRI().toString();
        }
    }


    private UserId getUserId(String user) {
        return UserId.getUserId(user);

    }

    private UserId getUserId() {
        return getUserInSession();
    }


    private void applyChanges(OWLOntologyChangeFactory... changeFactories) {

        for (OWLOntologyChangeFactory cf : changeFactories) {
            List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
            cf.createChanges(changes);
            if (!changes.isEmpty()) {
                OWLAPIProject project = cf.getProject();
                project.applyChanges(cf.getUserId(), changes, cf.getChangeDescription());
            }
        }


    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////
    ///////
    ///////  Implementation of OntologyService
    ///////
    ///////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Boolean hasWritePermission(String projectName, String userName) {
        return true;
    }

    /**
     * Gets the imports of the root ontology for the specified project.
     * @param projectName The name of the project for which the imports will be retrieved.
     * @return ImportsData for the specified project.
     */
    public ImportsData getImportedOntologies(String projectName) {
        // Is this the imports closure? Direct imports???
        OWLOntology rootOntology = getOntology(projectName);
        ImportsData importsData = new ImportsData(toName(rootOntology.getOntologyID()));
        for (OWLOntology importedOntology : rootOntology.getDirectImports()) {
            ImportsData importedOntologyImportsData = new ImportsData(toName(importedOntology.getOntologyID()));
            importsData.addImport(importedOntologyImportsData);
        }
        return importsData;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////
    ///////
    ///////  Entity Triples.  What is the distinction between entity triples and property values?
    ///////
    ///////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the triples for the specified entity.  This basically returns annotations for the specified entity.  At one
     * point I had this returning property assertion data and property/filler pairs for existential restrictions, but
     * it's really unclear what values are acceptable to UI components.  The main grid that uses these triples either
     * edits strings or allows instances to be specified (as far as I can tell).
     * @param projectName The project which specifies the triples for the specified entity.
     * @param entityName The name of the entity.  This should be an IRI but browser text should also work.  Some UI/client
     * components send browser text. Pretty messy really - needs sorting!
     * @return The list of triples for the specified entity.
     */
    public List<Triple> getEntityTriples(final String projectName, String entityName) {
        final Set<Triple> result = new LinkedHashSet<Triple>();
        TripleMapperSelector selector = new TripleMapperSelector(getProject(projectName), AnnotationsTreatment.INCLUDE_ANNOTATIONS, NonAnnotationTreatment.EXCLUDE_NON_ANNOTATIONS);
        OWLAPIProject project = getProject(projectName);
        Set<OWLEntity> entities = project.getRenderingManager().getEntities(entityName);
        for (OWLEntity entity : entities) {
            TripleMapper<?> mapper = selector.getMapper(entity);
            if (mapper != null) {
                result.addAll(mapper.getTriples());
            }
        }
        return new ArrayList<Triple>(result);
    }

    public List<Triple> getEntityTriples(String projectName, List<String> entities, List<String> properties) {
        List<Triple> result = new ArrayList<Triple>();
        for (String entityName : entities) {
            List<Triple> entityTriples = getEntityTriples(projectName, entityName);
            for (Triple entityTriple : entityTriples) {
                PropertyEntityData propertyEntityData = entityTriple.getProperty();
                // Check name AND check the browser text - some consistency would be really nice!
                if (properties.contains(propertyEntityData.getName()) || properties.contains(propertyEntityData.getBrowserText())) {
                    result.add(entityTriple);
                }
            }
        }
        return result;
    }

    public List<Triple> getEntityTriples(String projectName, List<String> entities, List<String> properties, List<String> reifiedProps) {
        // First get the direct values
        List<Triple> directValues = getEntityTriples(projectName, entities, properties);
        // Add in the reified stuff
        List<Triple> reifiedValues = new ArrayList<Triple>();
        for(Triple triple : directValues) {
            EntityData entityData = triple.getValue();
            if(entityData.getValueType() == ValueType.Instance || entityData.getValueType() == ValueType.Cls) {
                reifiedValues.addAll(getEntityTriples(projectName, Arrays.asList(entityData.getName()), reifiedProps));
            }
        }
        return reifiedValues;
    }

    public List<EntityPropertyValues> getEntityPropertyValues(String projectName, List<String> entities, List<String> properties, List<String> reifiedProps) {
        // First get the direct values
        List<Triple> directValues = getEntityTriples(projectName, entities, properties);
        // Add in the reified stuff
        List<EntityPropertyValues> result = new ArrayList<EntityPropertyValues>();
        for(Triple triple : directValues) {
            EntityData entityData = triple.getValue();
            if(entityData.getValueType() == ValueType.Instance || entityData.getValueType() == ValueType.Cls) {
                final List<Triple> reifiedTriples = getEntityTriples(projectName, Arrays.asList(entityData.getName()), reifiedProps);
                if (!reifiedTriples.isEmpty()) {
                    EntityPropertyValues reifiedSet = new EntityPropertyValues(entityData);
                    for(Triple reifiedTriple : reifiedTriples) {
                        reifiedSet.addPropertyValue(reifiedTriple.getProperty(), reifiedTriple.getValue());
                    }
                    result.add(reifiedSet);
                }
            }

        }
        return result;


    }


    /**
     * Implemented as returning the entity data for owl:Thing
     * @param projectName The name of the project. Doesn't really matter - it's always owl:Thing
     * @return The entity data for owl:Thing
     */
    public EntityData getRootEntity(String projectName) {
        OWLAPIProject project = getProject(projectName);
        RenderingManager renderingManager = project.getRenderingManager();
        OWLClass owlThing = project.getRootOntology().getOWLOntologyManager().getOWLDataFactory().getOWLThing();
        return renderingManager.getEntityData(owlThing);
    }

    /**
     * Implemented as getting the entity data for the entity corresponding to the specified name.
     * @param projectName The name of the project
     * @param entityName The name of the entity.  This should be an IRI, but this implementation also assumes it could
     * be browser text.
     * @return The entity data for the specified entity.  Web-Protege seems to assume that different types of entities
     *         will not share the same name, and hence methods like this only return one value.  Since different types of
     *         entities CAN share the same name in OWL (due to punning), this method is implemented as getting the first entity
     *         in the set as returned by the {@link RenderingManager#selectEntity(java.util.Set)} method.  If there are no entities with the
     *         specified name, then, as implemented here, this method returns <code>null</code> (I don't know whether this is
     *         allowed or not).
     */
    public EntityData getEntity(String projectName, String entityName) {
        RenderingManager rm = getRenderingManager(projectName);
        Set<OWLEntity> entities = rm.getEntities(entityName);
        if (entities.isEmpty()) {
            // Everything else in this code base returns null, so I'm not going to bother about it here.
            return null;
        }
        OWLEntity selectedEntity = RenderingManager.selectEntity(entities);
        return rm.getEntityData(selectedEntity);
    }

    /**
     * Gets the subclasses of a given entity.  This implementation uses the {@link edu.stanford.bmir.protege.web.server.hierarchy.AssertedClassHierarchyProvider} that
     * is used in Protege 4 to answer the request.
     * @param projectName The name of the relevant project.
     * @param className The class name which corresponds to an entity for which subclasses will be retrieved.  This
     * should be an IRI, but this implementation will tolerate browser text.  If null, then this implementation returns
     * an empty list.
     * @return The list of subclasses.
     */
    public List<SubclassEntityData> getSubclasses(String projectName, String className) {
        if (projectName == null) {
            throw new NullPointerException("projectName must not be null");
        }
        if (className == null) {
            return Collections.emptyList();
        }
        List<SubclassEntityData> result = new ArrayList<SubclassEntityData>();
        OWLAPIProject project = getProject(projectName);
        RenderingManager rm = project.getRenderingManager();
        AssertedClassHierarchyProvider hierarchyProvider = project.getClassHierarchyProvider();
        OWLClass cls = rm.getEntity(className, EntityType.CLASS);

        boolean checkForDeprecated = project.getRootOntology().containsAnnotationPropertyInSignature(OWLRDFVocabulary.OWL_DEPRECATED.getIRI());
        for (OWLClass subclass : new ArrayList<OWLClass>(hierarchyProvider.getChildren(cls))) {
            boolean deprecated = false;
            if(checkForDeprecated) {
                deprecated = project.isDeprecated(subclass);
            }
//            if (!deprecated) {
                Set<OWLClass> children = hierarchyProvider.getChildren(subclass);
                int subClassSubClassesCount = children.size();
                String browserText = rm.getBrowserText(subclass);
                String name = subclass.getIRI().toString();
                SubclassEntityData data = new SubclassEntityData(name, browserText, new HashSet<EntityData>(0), subClassSubClassesCount);
                data.setDeprecated(deprecated);
                int directNotesCount = project.getNotesManager().getIndirectNotesCount(subclass);
//                int indirectNotesCount = project.getNotesManager().getIndirectNotesCount(cls);
                data.setLocalAnnotationsCount(directNotesCount);
            Set<Watch<?>> directWatches = project.getWatchManager().getDirectWatches(subclass, getUserId());
            if(!directWatches.isEmpty()) {
                data.setWatches(directWatches);
            }
                data.setValueType(ValueType.Cls);
                result.add(data);
//            }
        }
        Collections.sort(result, new Comparator<SubclassEntityData>() {
            public int compare(SubclassEntityData o1, SubclassEntityData o2) {
                if(o1.isDeprecated()) {
                    if(!o2.isDeprecated()) {
                        return 1;
                    }
                }
                else if(o2.isDeprecated()) {
                    return -1;
                }
                String browserText1 = o1.getBrowserText();
                String browserText2 = o2.getBrowserText();
                if(browserText1.startsWith("'")) {
                    browserText1 = browserText1.substring(1);
                }
                if(browserText2.startsWith("'")) {
                    browserText2 = browserText2.substring(1);
                }
                return browserText1.compareToIgnoreCase(browserText2);
            }
        });
        return result;
    }

    public List<EntityData> moveCls(String projectName, String clsName, String oldParentName, String newParentName, boolean checkForCycles, String user, String operationDescription) {
        // Why check for cycles here and nowhere else?
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        MoveClassChangeFactory cf = new MoveClassChangeFactory(project, userId, operationDescription, clsName, oldParentName, newParentName, checkForCycles);
        applyChanges(cf);
        // Not sure what this is meant to return - the other impl returns null - return null.  Confirmation from Csongor null idicates no cycles.
        return null;
    }

    /**
     * Gets a paginated list of individuals.
     * and then uses {@link PaginationServerUtil} to compute the pagination.
     * @param projectName The name of the relevant project
     * @param className The name of the class who's individuals are to be retrieved.  This should be an IRI, but this
     * implementation will also tolerate browser text.
     * @param start The start index of the pagination
     * @param limit The end index of the pagination
     * @param sort Whether to sort the pagination or not.
     * @param dir The direction of the sort. (Oh dear - a string).  I've no idea where this magic string is defined.
     * @return A {@link PaginationData} object conforming to the specified criteria.
     */
    public PaginationData<EntityData> getIndividuals(String projectName, String className, int start, int limit, String sort, String dir) {
        GetIndividualsStrategy strategy = new GetIndividualsStrategy(getProject(projectName), getUserId(), className);
        List<EntityData> result = strategy.execute();
        return PaginationServerUtil.pagedRecords(result, start, limit, sort, dir);
    }

    /**
     * Gets the superclasses of a particular class.  This implementation uses the {@link AssertedClassHierarchyProvider}
     * to obtain this information.
     * @param projectName The relevant project which makes the assertions to be taken into consideration.
     * @param className The name of the class.  This should be an IRI, but this implementation will also tolerate
     * browser text.
     * @param direct Whether the direct or indirect superclases of A will be returned.  Specifying <code>true</code> causes
     * {@link AssertedClassHierarchyProvider#getParents(org.semanticweb.owlapi.model.OWLObject)} to be called in order
     * to compute the result.  Specifying <code>false</code> causes {@link AssertedClassHierarchyProvider#getAncestors(org.semanticweb.owlapi.model.OWLObject)}
     * to be used in the calculation of the result.
     * @return A list of entity data
     */
    public List<EntityData> getParents(String projectName, String className, final boolean direct) {
        OWLAPIProject project = getProject(projectName);
        GetParentsStrategy strategy = new GetParentsStrategy(project, getUserId(), className, direct);
        return strategy.execute();
    }

    public List<EntityData> getSubproperties(String projectName, String propertyName) {
        // NOTE:  ALTHOUGH THIS CAN RETURN A LIST OF ENTITY DATA, VARIOUS PLACES IN THE UI CODE PERFORM AN UNCHECKED CAST
        // TO PROPERTY ENTITY DATA!!!!!

        OWLAPIProject project = getProject(projectName);
        RenderingManager rm = project.getRenderingManager();

        // propertyName can be null!  This means the top property.
        // I'm not sure if RootPropertyNode is a system property or what but the UI asks for it.  Messy - needs tidying.
        // The properties tree asks for sub properties
        // of this property
        if (propertyName == null || PROPERTY_HIERARCHY_ROOT_NODE_NAME.equals(propertyName)) {
            List<EntityData> roots = new ArrayList<EntityData>();
            OWLDataFactory df = project.getDataFactory();
            EntityData topObjectProperty = rm.getPropertyEntityData(df.getOWLTopObjectProperty());
            roots.add(topObjectProperty);
            EntityData topDataProperty = rm.getPropertyEntityData(df.getOWLTopDataProperty());
            roots.add(topDataProperty);
            roots.add(ANNOTATION_PROPERTIES_ROOT);
            return roots;
        }

        // Special handling for fake annotation property root
        if(propertyName.equals(ANNOTATION_PROPERTIES_ROOT_NAME)) {
            Set<EntityData> annotationPropertyRoots = new LinkedHashSet<EntityData>();
            Set<IRI> addedProperties = new HashSet<IRI>();
            for (OWLAnnotationProperty annotationProperty : project.getAnnotationPropertyHierarchyProvider().getRoots()) {
                annotationPropertyRoots.add(rm.getEntityData(annotationProperty));
                addedProperties.add(annotationProperty.getIRI());
            }
            for (IRI iri : OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) {
                if (!addedProperties.contains(iri)) {
                    annotationPropertyRoots.add(rm.getEntityData(iri));
                }
            }
            final ArrayList<EntityData> result = new ArrayList<EntityData>(annotationPropertyRoots);
            sortListOfEntityData(result);
            return result;
        }


        List<EntityData> result = new ArrayList<EntityData>();
        Set<OWLEntity> matchingEntities = rm.getEntities(propertyName);
        // Which entity does it refer to?  All messed up.
        for (OWLEntity entity : matchingEntities) {
            if (entity.isOWLObjectProperty()) {
                OWLObjectPropertyHierarchyProvider hierarchyProvider = project.getObjectPropertyHierarchyProvider();
                Set<OWLObjectProperty> subProperties = hierarchyProvider.getChildren(entity.asOWLObjectProperty());
                for (OWLObjectProperty subProperty : subProperties) {
                    final EntityData entityData = rm.getEntityData(subProperty);
                    int notesCount = project.getNotesManager().getDirectNotesCount(subProperty);
                    entityData.setLocalAnnotationsCount(notesCount);
                    result.add(entityData);
                }
            }
            else if (entity.isOWLDataProperty()) {
                OWLDataPropertyHierarchyProvider hierarchyProvider = project.getDataPropertyHierarchyProvider();
                Set<OWLDataProperty> subProperties = hierarchyProvider.getChildren(entity.asOWLDataProperty());
                for (OWLDataProperty subProperty : subProperties) {
                    final EntityData entityData = rm.getEntityData(subProperty);
                    int notesCount = project.getNotesManager().getDirectNotesCount(subProperty);
                    entityData.setLocalAnnotationsCount(notesCount);
                    result.add(entityData);
                }
            }
            else if (entity.isOWLAnnotationProperty()) {
                OWLAnnotationPropertyHierarchyProvider hierarchyProvider = project.getAnnotationPropertyHierarchyProvider();
                Set<OWLAnnotationProperty> subProperties = hierarchyProvider.getChildren(entity.asOWLAnnotationProperty());
                for (OWLAnnotationProperty subProperty : subProperties) {
                    final EntityData entityData = rm.getEntityData(subProperty);
                    int notesCount = project.getNotesManager().getDirectNotesCount(subProperty);
                    entityData.setLocalAnnotationsCount(notesCount);
                    result.add(entityData);
                }
            }
        }
        sortListOfEntityData(result);
        return result;
    }

    private void sortListOfEntityData(List<EntityData> result) {
        Collections.sort(result, new Comparator<EntityData>() {
            @Override
            public int compare(EntityData o1, EntityData o2) {
                String browserText1 = o1.getBrowserText();
                String browserText2 = o2.getBrowserText();
                if (browserText1 != null && browserText2 != null) {
                    return browserText1.compareToIgnoreCase(browserText2);
                }
                else {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////
    ///////
    ///////  Property Values
    ///////
    ///////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addPropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity, EntityData value, String user, String operationDescription) {
        AddPropertyValueChangeFactory cf = new AddPropertyValueChangeFactory(getProject(projectName), getUserId(), operationDescription, entityName, propertyEntity, value);
        applyChanges(cf);
    }

    public void removePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity, EntityData value, String user, String operationDescription) {
        RemovePropertyValueChangeFactory cf = new RemovePropertyValueChangeFactory(getProject(projectName), getUserId(), operationDescription, entityName, propertyEntity, value);
        applyChanges(cf);
    }

    public void replacePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity, EntityData oldValue, EntityData newValue, String user, String operationDescription) {
        ReplacePropertyValueChangeFactory cf = new ReplacePropertyValueChangeFactory(getProject(projectName), getUserId(), operationDescription, entityName, propertyEntity, oldValue, newValue);
        applyChanges(cf);

    }

    public void setPropertyValues(String projectName, String entityName, PropertyEntityData propertyEntity, List<EntityData> values, String user, String operationDescription) {
        throw new RuntimeException("Not implemented");
    }

    public EntityData createInstanceValue(String projectName, String instName, String typeName, String subjectEntity, String propertyEntity, String user, String operationDescription) {
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        if(instName == null) {
            // Not surprisingly it can be
            instName = "http://protege.stanford.edu/named-individuals/Individual-" + UUID.randomUUID().toString();
        }
        applyChanges(new CreateInstanceValueChangeFactory(project, userId, operationDescription, instName, typeName, subjectEntity, propertyEntity));
        return getRenderingManager(projectName).getEntityData(instName, EntityType.NAMED_INDIVIDUAL);
    }

    public EntityData createInstanceValueWithPropertyValue(String projectName, String instName, String typeName, String subjectEntity, String propertyEntity, PropertyEntityData instancePropertyEntity, EntityData valueEntityData, String user, String operationDescription) {
        throw new RuntimeException("Not implemented");
    }

    public List<EntityData> getDirectTypes(String projectName, String instanceName) {
        GetDirectTypesStrategy strategy = new GetDirectTypesStrategy(getProject(projectName), getUserId(), instanceName);
        return strategy.execute();
    }

    public PaginationData<EntityData> search(String projectName, String searchString, ValueType valueType, int start, int limit, String sort, String dir) {
        List<EntityData> search = search(projectName, searchString);
        return PaginationServerUtil.pagedRecords(search, start, limit, sort, dir);
    }


    public List<EntityData> search(String projectName, String searchString) {
        OWLAPIProject project = getProject(projectName);
        OWLAPISearchManager searchManager = project.getSearchManager();
        return searchManager.search(searchString);
    }

    public List<EntityData> search(String projectName, String searchString, ValueType valueType) {
        OWLAPIProject project = getProject(projectName);
        OWLAPISearchManager searchManager = project.getSearchManager();
        return searchManager.search(searchString);
    }

    public List<EntityData> getPathToRoot(String projectName, String entityName) {
        OWLAPIProject project = getProject(projectName);
        RenderingManager rm = project.getRenderingManager();
        Set<OWLEntity> entities = rm.getEntities(entityName);
        List<EntityData> result = new ArrayList<EntityData>();

        if (!entities.isEmpty()) {
            // Here we go again!  Which one?!?!
            OWLEntity entity = RenderingManager.selectEntity(entities);
            if (entity.isOWLClass()) {
                Set<List<OWLClass>> paths = project.getClassHierarchyProvider().getPathsToRoot(entity.asOWLClass());
                if (!paths.isEmpty()) {
                    for (OWLClass cls : paths.iterator().next()) {
                        result.add(rm.getEntityData(cls));
                    }
                }
            }
            else if (entity.isOWLObjectProperty()) {
                Set<List<OWLObjectProperty>> paths = project.getObjectPropertyHierarchyProvider().getPathsToRoot(entity.asOWLObjectProperty());
                if (!paths.isEmpty()) {
                    for (OWLObjectProperty prop : paths.iterator().next()) {
                        result.add(rm.getEntityData(prop));
                    }
                }
            }
            else if (entity.isOWLDataProperty()) {
                Set<List<OWLDataProperty>> paths = project.getDataPropertyHierarchyProvider().getPathsToRoot(entity.asOWLDataProperty());
                if (!paths.isEmpty()) {
                    for (OWLDataProperty prop : paths.iterator().next()) {
                        result.add(rm.getEntityData(prop));
                    }
                }
            }
            else if (entity.isOWLAnnotationProperty()) {
                Set<List<OWLAnnotationProperty>> paths = project.getAnnotationPropertyHierarchyProvider().getPathsToRoot(entity.asOWLAnnotationProperty());
                if (!paths.isEmpty()) {
                    for (OWLAnnotationProperty prop : paths.iterator().next()) {
                        result.add(rm.getEntityData(prop));
                    }
                }
            }
        }


        return result;
    }



    public EntityData createExternalReference(String projectName, String entityName, BioPortalReferenceData bpRefData, String user, String operationDescription) {
        OWLAPIProject project = getProject(projectName);
        RenderingManager rm = project.getRenderingManager();
        applyChanges(new AddExternalReferenceChangeFactory(project, entityName, bpRefData, UserId.getUserId(user), operationDescription));
        return rm.getEntityData(bpRefData.getConceptId(), EntityType.CLASS);
    }

    public EntityData replaceExternalReference(String projectName, String entityName, BioPortalReferenceData bpRefData, EntityData oldValueEntityData, String user, String operationDescription) {
        return null;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // TODO: Copied from the old ontology service - needs tidying up!!!

    public String getBioPortalSearchContent(String projectName, String entityName, BioPortalSearchData bpSearchData) {
            return URLUtil.getURLContent(getBioPortalSearchUrl(entityName, bpSearchData));
    }

    public String getBioPortalSearchContentDetails(String projectName, BioPortalSearchData bpSearchData,
                                                   BioPortalReferenceData bpRefData) {
        BioportalConcept bpc = new BioportalConcept();
        String encodedConceptId = bpRefData.getConceptId();
        try {
            encodedConceptId = URLEncoder.encode(bpRefData.getConceptId(), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            Log.getLogger().log(Level.WARNING, "Error at encoding BP search url", e1);
        }
        String urlString = bpSearchData.getBpRestBaseUrl() + BioPortalServerConstants.CONCEPTS_REST + "/"
                + bpRefData.getOntologyVersionId() + "/?conceptid=" + encodedConceptId;
        urlString = BioPortalUtil.addRestCallSuffixToUrl(urlString, bpSearchData.getBpRestCallSuffix());
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.getLogger().log(Level.WARNING, "Invalid BP search URL: " + urlString, e);
        }
        if (url == null) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>");
        buffer.append("<table width=\"100%\" class=\"servicesT\" style=\"border-collapse:collapse;border-width:0px;padding:5px\"><tr>");

        buffer.append("<td class=\"servHd\" style=\"background-color:#8E798D;color:#FFFFFF;\">Property</td>");
        buffer.append("<td class=\"servHd\" style=\"background-color:#8E798D;color:#FFFFFF;\">Value</td>");

        String oddColor = "#F4F2F3";
        String evenColor = "#E6E6E5";

        ClassBean cb = bpc.getConceptProperties(url);
        if (cb == null) {
            return "<html><body><i>Details could not be retrieved.</i></body></html>";
        }

        Map<Object, Object> relationsMap = cb.getRelations();
        int i = 0;
        for (Object obj : relationsMap.keySet()) {
            Object value = relationsMap.get(obj);
            if (value != null) {
                String text = HTMLUtil.replaceEOF(HTMLUtil.makeHTMLLinks(value.toString()));
                if (text.startsWith("[")) {
                    text = text.substring(1, text.length() - 1);
                }
                if (text.length() > 0) {
                    String color = i % 2 == 0 ? evenColor : oddColor;
                    buffer.append("<tr>");
                    buffer.append("<td class=\"servBodL\" style=\"background-color:" + color + ";padding:7px;font-weight: bold;\" >");
                    buffer.append(HTMLUtil.makeHTMLLinks(obj.toString()));
                    buffer.append("</td>");
                    buffer.append("<td class=\"servBodL\" style=\"background-color:" + color + ";padding:7px;\" >");
                    buffer.append(text);
                    buffer.append("</td>");
                    buffer.append("</tr>");
                    i++;
                }
            }
        }
        buffer.append("</table>");

        String directLink = bpRefData.getBpUrl();
        if (directLink != null && directLink.length() > 0) {
            buffer.append("<div style=\"padding:5px;\"><br><b>Direct link in BioPortal:</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            buffer.append("<a href=\"");
            buffer.append(directLink);
            buffer.append("\" target=\"_blank\">");
            buffer.append(directLink);
            buffer.append("</a></div>");
        }
        buffer.append("</body></html>");

        return buffer.toString();
    }

    private static String getBioPortalSearchUrl(String text, BioPortalSearchData bpSearchData) {
        text = text.replaceAll(" ", "%20");
        String urlString = bpSearchData.getBpRestBaseUrl() + BioPortalServerConstants.SEARCH_REST + "/" +
                text + createSearchUrlQueryString(bpSearchData);
        urlString = BioPortalUtil.addRestCallSuffixToUrl(urlString, bpSearchData.getBpRestCallSuffix());
        return urlString;
    }

    private static String createSearchUrlQueryString(BioPortalSearchData bpSearchData) {
        String res = "";
        String ontIds = bpSearchData.getSearchOntologyIds();
        String srchOpts = bpSearchData.getSearchOptions();
        String pgOpt = bpSearchData.getSearchPageOption();
        boolean firstSep = true;
        if (ontIds != null) {
            res += (firstSep ? "?" : "&") + "ontologyids=" + ontIds;
            firstSep = false;
        }
        if (srchOpts != null) {
            res += (firstSep ? "?" : "&") + srchOpts;
            firstSep = false;
        }
        if (pgOpt != null) {
            res += (firstSep ? "?" : "&") + pgOpt;
            firstSep = false;
        }
        return res;
    }
}
