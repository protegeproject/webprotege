package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.OntologyService;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.PaginationServerUtil;
import edu.stanford.bmir.protege.web.server.URLUtil;
import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.owlapi.extref.ExternalReferenceStrategy;
import edu.stanford.bmir.protege.web.server.owlapi.extref.ExternalReferenceSubClassStrategy;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetric;
import edu.stanford.bmir.protege.web.server.owlapi.metrics.OWLAPIProjectMetricValue;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.smi.protege.util.Log;
import org.ncbo.stanford.bean.concept.ClassBean;
import org.ncbo.stanford.util.BioPortalServerConstants;
import org.ncbo.stanford.util.BioPortalUtil;
import org.ncbo.stanford.util.BioportalConcept;
import org.ncbo.stanford.util.HTMLUtil;
import org.protege.editor.owl.model.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLDataPropertyHierarchyProvider;
import org.protege.editor.owl.model.hierarchy.OWLObjectPropertyHierarchyProvider;
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


    public OntologyServiceOWLAPIImpl() {
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
        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        return pm.getProject(projectId);
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
        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        return pm.getProject(projectId).getRootOntology();
    }

    /**
     * Converts a web-protege name (IRI?) to an IRI.
     * @param name The name.
     * @return The IRI corresponding to the name.
     */
    private IRI toIRI(String name) {
        return IRI.create(name);
    }

    /**
     * Converts an IRI to a web-protege name.
     * @param iri The IRI to be converted to a name.
     * @return The name.
     */
    private String toName(IRI iri) {
        return iri.toString();
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
//        return getUserId(KBUtil.getUserInSession(getThreadLocalRequest()));
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

    public Integer loadProject(String projectName) {
        getOntology(projectName);
        OWLAPIProject project = getProject(projectName);
        return (int) project.getChangeManager().getCurrentRevision().getValueAsInt();
    }


//    public List<OntologyEvent> getEvents(String projectName, long fromVersion) {
//        // TODO: Log user
//        OWLAPIProject project = getProject(projectName);
//        OWLAPIChangeManager changeManager = project.getChangeManager();
//        RevisionNumber revisionNumber = RevisionNumber.getRevisionNumber(fromVersion);
//        return changeManager.getOntologyEventsSinceRevisionNumber(revisionNumber);
//    }

    public Boolean hasWritePermission(String projectName, String userName) {
        return true;
    }

    public String getOntologyURI(String projectName) {
        OWLOntology ontology = getOntology(projectName);
        OWLOntologyID id = ontology.getOntologyID();
        return toName(id);
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

    public List<MetricData> getMetrics(String projectName) {
        List<MetricData> result = new ArrayList<MetricData>();
        OWLAPIProject project = getProject(projectName);
        for(OWLAPIProjectMetric metric : project.getMetricsManager().getMetrics()) {
            OWLAPIProjectMetricValue metricValue = metric.getMetricValue();
            result.add(new MetricData(metricValue.getMetricName(), metricValue.getBrowserText()));
        }
        return result;
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
                reifiedValues.addAll(getEntityTriples(projectName, Arrays.<String>asList(entityData.getName()), reifiedProps));
            }
        }
        return reifiedValues;
    }

    public List<EntityPropertyValues> getEntityPropertyValues(String projectName, List<String> entities, List<String> properties, List<String> reifiedProps) {
        // First get the direct values
        List<Triple> directValues = getEntityTriples(projectName, entities, properties);
        // Add in the reified stuff
        List<Triple> reifiedValues = new ArrayList<Triple>();
        List<EntityPropertyValues> result = new ArrayList<EntityPropertyValues>();
        for(Triple triple : directValues) {
            EntityData entityData = triple.getValue();
            if(entityData.getValueType() == ValueType.Instance || entityData.getValueType() == ValueType.Cls) {
                final List<Triple> reifiedTriples = getEntityTriples(projectName, Arrays.<String>asList(entityData.getName()), reifiedProps);
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

//    /**
//     * Deletes any entities that correspond to the specified entity name.  Since more than one entity could have the
//     * specified name, this could delete multiple entities. The actual behaviour of this implementation
//     * is defined in {@link DeleteEntityChangeFactory}.
//     * @param projectName The name of the project in which the deletion will occur.
//     * @param entityName The name of the entity.  Should be an IRI, but could also be browser text for components that
//     * don't do things correctly.
//     * @param user The user (name)
//     * @param operationDescription A high level description of the change.
//     */
//    public void deleteEntity(String projectName, String entityName, String user, String operationDescription) {
//        OWLAPIProject project = getProject(projectName);
//        UserId userId = getUserId(user);
//        applyChanges(new DeleteEntityChangeFactory(project, userId, operationDescription, entityName));
//    }


    /**
     * Renames all entities that correspond to the specified entity name.  The actual behaviour of this implementation
     * is defined in {@link RenameEntityChangeFactory}.
     * @param projectName The project in which renaming should occur.
     * @param oldName The old name.  Entities that correspond to this name will be renamed.
     * @param newName The new name.  Should be an IRI, but, as usual, we tolerate browser text.
     * @param user The user (name).
     * @param operationDescription A high level description of the change.
     * @return EntityData corresponding to the renamed entity.
     */
    public EntityData renameEntity(String projectName, String oldName, String newName, String user, String operationDescription) {
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        applyChanges(new RenameEntityChangeFactory(project, userId, operationDescription, oldName, newName));
        return getEntity(projectName, newName);

    }

    /**
     * "Creates a class".  The behaviour of this implementation is defined in the {@link CreateClassChangeFactory}.
     *
     * @param projectId
     * @param className
     * @param superCls
     * @param userId
     * @param operationDescription A high level description.  @return EntityData that represents the newly created class.
     * */
    public EntityData createCls(ProjectId projectId, String className, OWLClass superCls, UserId userId, String operationDescription) {
        OWLAPIProject project = getProject(projectId);
        CreateClassChangeFactory cf = new CreateClassChangeFactory(project, userId, operationDescription, className, superCls);
        applyChanges(cf);
        return getRenderingManager(projectId).getEntityData(className, EntityType.CLASS);
    }

    /**
     * In this implementation, this method delegates to the {@link #createCls(edu.stanford.bmir.protege.web.shared.project.ProjectId, String, org.semanticweb.owlapi.model.OWLClass, edu.stanford.bmir.protege.web.shared.user.UserId, String)}
     * method.
     *
     * @param projectId
     * @param clsName The name of the class to "create" this should be an IRI but could also be the browser text.
     * @param superCls
     *@param createMetaClses ?????
     * @param userId
     * @param operationDescription A high level description.   @return EntityData that represents the newly created class.
     */
    public EntityData createCls(ProjectId projectId, String clsName, OWLClass superCls, boolean createMetaClses, UserId userId, String operationDescription) {
        // Not sure what we should do with the meta class here - delegate to the method without the metaclass
        return createCls(projectId, clsName, superCls, userId, operationDescription);
    }

    /**
     * This implementation applies changes which are created using the same procedure as the {@link #createCls(String, String, String, String, String)}
     * method and then adds the specified property value as an annotation to the class.  It seems like this method is
     * only called by the {@link edu.stanford.bmir.protege.web.client.ui.ontology.classes.LabelingClassTreePortlet}, which
     * doesn't appear to be used anywhere.  In any case, the OWL API implementation approaches this from a different point
     * of view - i.e. it uses different strategies in the form of {@link OWLEntityCreatorFactory} objects (much cleaner
     * really).  I vote for getting rid of this method and its associated junk.
     *
     * @param projectId
     * @param clsName The name of the class to be "created".  Should be an IRI, but could also be some browser text.
     * @param superCls
     *@param propertyName The name of the property to add.  In this case, this must correspond to an annotation property, otherwise,
     * it will not be added.
     * @param propertyValue The value of the property to be added.  This should correspond to an OWLAnnotationValue, otherwise
 * it will not be added.
     * @param userId
     * @param operationDescription A high level description of the changes that will take place.    @return EntityData representing the newly "created" class.
     */
    public EntityData createClsWithProperty(ProjectId projectId, String clsName, OWLClass superCls, String propertyName, EntityData propertyValue, UserId userId, String operationDescription) {
        OWLAPIProject project = getProject(projectId);
        CreateClassChangeFactory createClassChangeFactory = new CreateClassChangeFactory(project, userId, operationDescription, clsName, superCls);
        AddClassPropertyChangeFactory addClassPropertyChangeFactory = new AddClassPropertyChangeFactory(project, userId, operationDescription, clsName, propertyName, propertyValue);
        applyChanges(createClassChangeFactory, addClassPropertyChangeFactory);
        return project.getRenderingManager().getEntityData(clsName, EntityType.CLASS);
    }


    /**
     * Gets the subclasses of a given entity.  This implementation uses the {@link AssertedClassHierarchyProvider} that
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



    /**
     * Makes a class a subclass of another class.  This essentially adds a SubClassOf axiom to the root ontology of the
     * specified project.
     * @param projectName The project in which the changes will take place.
     * @param clsName The class name which corresponds to the class which will be made a subclass of the specified class
     * below.  The name should be an IRI, but this implementation will also tolerate it being browser text.
     * @param superClsName The class name which corresponds to the superclass in the SubClassOf axiom.  This should be
     * an IRI but this implementation will also tolerate it being browser text. Not null.
     * @param user The user making the changes. Not null.
     * @param operationDescription A high level description of the change.
     */
    public void addSuperCls(String projectName, String clsName, String superClsName, String user, String operationDescription) {
        if (projectName == null) {
            throw new NullPointerException("projectName must not be null");
        }
        if (clsName == null) {
            throw new NullPointerException("clsName must not be null");
        }
        if (superClsName == null) {
            throw new NullPointerException("superClsName must not be null");
        }
        if (user == null) {
            throw new NullPointerException("user must not be null");
        }
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        applyChanges(new AddSuperClassChangeFactory(project, userId, operationDescription, clsName, superClsName));
    }

    /**
     * Removes a subclass axiom from the ontology.
     * @param projectName
     * @param clsName
     * @param superClsName
     * @param user
     * @param operationDescription
     */
    public void removeSuperCls(String projectName, String clsName, String superClsName, String user, String operationDescription) {
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        applyChanges(new RemoveSuperClassChangeFactory(project, userId, operationDescription, clsName, superClsName));
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
     * Gets the individuals which are asserted to be instances of a particular class.  In this implementation, ClassAssertions
     * in the imports closure of the root ontology are used to compute the returned value.
     * @param projectName The name of the relevant project.
     * @param className A string corresponding to the class IRI, but this implementation will also tolerate browser text.
     * Must not be null.
     * @return A list of EntityData objects which represent the individuals that are instances of the specified class.
     * @see {@link GetIndividualsStrategy}
     */
    public List<EntityData> getIndividuals(String projectName, String className) {
        GetIndividualsStrategy strategy = new GetIndividualsStrategy(getProject(projectName), getUserId(), className);
        return strategy.execute();
    }

    /**
     * Gets a paginated list of individuals.  This implementation just delegates to {@link #getIndividuals(String, String)}
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
        List<EntityData> result = getIndividuals(projectName, className);
        return PaginationServerUtil.pagedRecords(result, start, limit, sort, dir);
    }

    public String getRestrictionHtml(String projectName, String className) {
        //???
        return "Restriction html";
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////
    ////////  Conditions Stuff
    ////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets the condition itmes for a given "root" class.  These correspond to axioms which assert that the class is
     * a subclass of another class, or equivalent to another class.
     * @param projectName The name of the relevant project.
     * @param className The root class name for which the condition items will be generated. This should be an IRI, but
     * this implementation will tolerate it being browser text.
     * @return A list of condition items.  In this implementation {@link ConditionItemRenderer} is used to generate this
     * list.
     */
    public List<ConditionItem> getClassConditions(String projectName, String className) {
        OWLAPIProject project = getProject(projectName);
        OWLClass rootClass = project.getRenderingManager().getEntity(className, EntityType.CLASS);
        ConditionItemRenderer renderer = new ConditionItemRenderer(project, rootClass);
        return renderer.getConditionItems();
    }

    /**
     * For a given "root" class, adds a condition and then returns the new list of conditions for that class.  See
     * {@link AddConditionChangeFactory} for more details of the implementation.
     * @param projectName The name of the relevant project.
     * @param className The class name.  This should be an IRI, but this implementation tolerates browser text as well.
     * @param row The row.  Ignored by this implementation (only specific to P3)
     * @param newCondition A Manchester Syntax representation of the class expression that corresponds to the condition.
     * The entities in this class expression should be rendered using the short form provider for the project in
     * question.
     * @param isNS "Is Necessary & Sufficient".  If set to <code>true</code>, an EquivalentClasses axiom
     * (EquivalentClasses(className newCondition)) will be added
     * to the root ontology of the specified project.  If set to <code>false</code> a SubClassOf axiom will be added to
     * the root ontology of the specified project (SubClassOf(className newCondition)).
     * @param operationDescription A high level description of the change.
     * @return The list of ConditionItems that apply to this root class after the change.
     */
    public List<ConditionItem> addCondition(String projectName, String className, int row, String newCondition, boolean isNS, String operationDescription) {
        // It transpires that the list of condition items that is returned should be the complete list!
        OWLAPIProject project = getProject(projectName);
        AddConditionChangeFactory cf = new AddConditionChangeFactory(project, getUserId(), operationDescription, className, newCondition, isNS);
        applyChanges(cf);
        return getClassConditions(projectName, className);
    }

    /**
     * For a given "root" class, deletes a condition and then returns the new list of conditions for that class.  This
     * implementation parses out the specified condition item into an axiom and then removes this axiom from the imports
     * closure of the root ontology.  See {@link DeleteConditionChangeFactory} for more details of the implementation.
     * @param projectName The name of the relevant project.
     * @param className The class name.  This should be an IRI, but this implementation tolerates browser text as well.
     * @param row The row.  Ignored by this implementation (only specific to P3)
     * @param conditionItem The condition item to be deleted.  This condition item MUST have the {@link ConditionItemType}
     * set, otherwise a runtime exception will be thrown.
     * @param operationDescription A high level description of the change.
     * @return The list of ConditionItems that apply to this root class after the change.
     */
    public List<ConditionItem> deleteCondition(String projectName, String className, ConditionItem conditionItem, int row, String operationDescription) {
        DeleteConditionChangeFactory cf = new DeleteConditionChangeFactory(getProject(projectName), getUserId(), operationDescription, className, conditionItem);
        applyChanges(cf);
        return getClassConditions(projectName, className);
    }

    /**
     * For a given "root" class, replaces a condition with a new condition and then returns the new list of conditions
     * for that class.  This implementation parses out the specified condition item into an axiom and then removes this
     * axiom from the imports closure of the root ontology.  The new condition is parsed into a class expression, and
     * then an axiom that is of the same form (SubClassOf or EquivalentClasses) as the axiom being replaced is generated.
     * Annotations on the axiom being replaced are copied over to the new axiom, which is added to each ontology in the
     * imports closure of the root ontology that the old axiom was contained in.
     * See {@link edu.stanford.bmir.protege.web.server.owlapi.ReplaceConditionChangeFactory} for more details of the implementation.
     * @param projectName The name of the relevant project.
     * @param className The class name.  This should be an IRI, but this implementation tolerates browser text as well.
     * @param row The row.  Ignored by this implementation (only specific to P3)
     * @param conditionItem The condition item to be deleted.  This condition item MUST have the {@link ConditionItemType}
     * set, otherwise a runtime exception will be thrown.
     * @param operationDescription A high level description of the change.
     * @return The list of ConditionItems that apply to this root class after the change.
     */
    public List<ConditionItem> replaceCondition(String projectName, String className, ConditionItem conditionItem, int row, String newCondition, String operationDescription) {
        ReplaceConditionChangeFactory cf = new ReplaceConditionChangeFactory(getProject(projectName), getUserId(), operationDescription, className, conditionItem, newCondition);
        applyChanges(cf);
        return getClassConditions(projectName, className);
    }



    public ConditionSuggestion getConditionAutocompleteSuggestions(String projectName, String condition, int cursorPosition) {
        // This is very confusing.  This method is actually used for error checking as well.  It paints the UI red if
        // an error is reported.  However, the cursor position is irrelevant for this purpose.  Two parses are therefore
        // required
        // So, we need to do two things
        // 1) Ignore the cursor position and check the whole thing.
        // 2) Check for auto completions, paying attention to where the cursor is.
        OWLAPIProject project = getProject(projectName);
        ConditionItemChecker checker = new ConditionItemChecker(project);
        return checker.getConditionSuggestion(condition, cursorPosition);
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

    public String getParentsHtml(String projectName, String className, boolean direct) {
        /// ????
        return "Parents HTML";
    }

    public List<Triple> getRelatedProperties(String projectName, String className) {
        UserId userId = getUserId();
        GetRelatedPropertiesStrategy strategy = new GetRelatedPropertiesStrategy(getProject(projectName), userId, className);
        return strategy.execute();
    }

    public EntityData createObjectProperty(String projectName, String propertyName, String superPropName, String user, String operationDescription) {
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        OWLOntologyChangeFactory changeFactory = new CreateObjectPropertyChangeFactory(project, userId, operationDescription, propertyName, superPropName);
        applyChanges(changeFactory);
        return getRenderingManager(projectName).getEntityData(propertyName, EntityType.OBJECT_PROPERTY);
    }

    public EntityData createDatatypeProperty(String projectName, String propertyName, String superPropName, String user, String operationDescription) {
        // Trial and error reveals some of these names can be null!  Horrible.
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        applyChanges(new CreateDataPropertyChangeFactory(project, userId, operationDescription, propertyName, superPropName));
        return getRenderingManager(projectName).getEntityData(propertyName, EntityType.DATA_PROPERTY);
    }

    public EntityData createAnnotationProperty(String projectName, String propertyName, String superPropName, String user, String operationDescription) {
        // Trial and error reveals some of these names can be null!
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        applyChanges(new CreateAnnotationPropertyChangeFactory(project, userId, operationDescription, propertyName, superPropName));
        return getRenderingManager(projectName).getEntityData(propertyName, EntityType.ANNOTATION_PROPERTY);
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
                return o1.getName().compareToIgnoreCase(o2.getName());
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


    public EntityData createInstance(String projectName, String instName, String typeName, String user, String operationDescription) {
        OWLAPIProject project = getProject(projectName);
        UserId userId = getUserId(user);
        applyChanges(new CreateInstanceChangeFactory(project, userId, operationDescription, instName, typeName));
        return getRenderingManager(projectName).getEntityData(instName, EntityType.NAMED_INDIVIDUAL);
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


        ExternalReferenceStrategy strategy = new ExternalReferenceSubClassStrategy();

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
