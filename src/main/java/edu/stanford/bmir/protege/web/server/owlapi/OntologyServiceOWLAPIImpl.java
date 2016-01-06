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
    ///////  Search
    ///////
    ///////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
            logger.info("Error at encoding BP search url", e1);
        }
        String urlString = bpSearchData.getBpRestBaseUrl() + BioPortalServerConstants.CONCEPTS_REST + "/"
                + bpRefData.getOntologyVersionId() + "/?conceptid=" + encodedConceptId;
        urlString = BioPortalUtil.addRestCallSuffixToUrl(urlString, bpSearchData.getBpRestCallSuffix());
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            logger.info("Invalid BP search URL: " + urlString, e);
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
