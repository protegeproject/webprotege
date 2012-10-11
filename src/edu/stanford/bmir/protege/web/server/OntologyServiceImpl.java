package edu.stanford.bmir.protege.web.server;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.ncbo.stanford.bean.concept.ClassBean;
import org.ncbo.stanford.bean.ontology.OntologyBean;
import org.ncbo.stanford.util.BioPortalServerConstants;
import org.ncbo.stanford.util.BioPortalUtil;
import org.ncbo.stanford.util.BioPortalViewOntologyMap;
import org.ncbo.stanford.util.BioportalConcept;
import org.ncbo.stanford.util.HTMLUtil;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.model.event.OntologyEvent;
import edu.stanford.bmir.protege.web.client.rpc.OntologyService;
import edu.stanford.bmir.protege.web.client.rpc.data.AnnotationData;
import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalReferenceData;
import edu.stanford.bmir.protege.web.client.rpc.data.BioPortalSearchData;
import edu.stanford.bmir.protege.web.client.rpc.data.ConditionItem;
import edu.stanford.bmir.protege.web.client.rpc.data.ConditionSuggestion;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityPropertyValues;
import edu.stanford.bmir.protege.web.client.rpc.data.ImportsData;
import edu.stanford.bmir.protege.web.client.rpc.data.MetricData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyType;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protegex.bp.ref.ProtegeUtil;
import edu.stanford.bmir.protegex.bp.ref.ReferenceModel;
import edu.stanford.smi.protege.collab.util.HasAnnotationCache;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.FrameCounts;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.ModelUtilities;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.model.ValueType;
import edu.stanford.smi.protege.query.api.QueryApi;
import edu.stanford.smi.protege.query.api.QueryConfiguration;
import edu.stanford.smi.protege.query.indexer.IndexUtilities;
import edu.stanford.smi.protege.server.RemoteClientProject;
import edu.stanford.smi.protege.server.RemoteServer;
import edu.stanford.smi.protege.server.Session;
import edu.stanford.smi.protege.server.metaproject.MetaProjectConstants;
import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.Tree;
import edu.stanford.smi.protege.util.URIUtilities;
import edu.stanford.smi.protegex.owl.model.OWLAnonymousClass;
import edu.stanford.smi.protegex.owl.model.OWLClass;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLOntology;
import edu.stanford.smi.protegex.owl.model.ProtegeNames;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.RDFSLiteral;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.owl.model.XSPNames;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParseException;
import edu.stanford.smi.protegex.owl.model.classparser.OWLClassParser;
import edu.stanford.smi.protegex.owl.model.classparser.ParserUtils;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFSLiteral;
import edu.stanford.smi.protegex.owl.model.impl.OWLSystemFrames;
import edu.stanford.smi.protegex.owl.model.impl.OWLUtil;
import edu.stanford.smi.protegex.owl.model.util.DLExpressivityChecker;
import edu.stanford.smi.protegex.owl.model.util.ModelMetrics;
import edu.stanford.smi.protegex.owl.ui.code.OWLResourceNameMatcher;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableItem;
import edu.stanford.smi.protegex.owl.ui.conditions.ConditionsTableModel;

/**
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class OntologyServiceImpl extends RemoteServiceServlet implements OntologyService {
    private static final long serialVersionUID = -4229789001933130232L;
    private static final int MIN_SEARCH_STRING_LENGTH = 3;
    private static HashMap<String, BioPortalViewOntologyMap> bpViewOntologyMaps = new HashMap<String, BioPortalViewOntologyMap>();

    protected Project getProject(String projectName) {
        return ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
    }

    public Integer loadProject(String projectName) {
        ServerProject<Project> serverProject = Protege3ProjectManager.getProjectManager().getServerProject(projectName);
        if (serverProject == null) {
            return null;
        }
        // TODO: not clear it is needed...
        /*
        Project prj = serverProject.getProject();
        if (prj != null) {
            HasAnnotationCache.fillHasAnnotationCache(prj.getKnowledgeBase());
        }
         */
        return Integer.valueOf(serverProject.getServerVersion());

    }

    public String getOntologyURI(String projectName) {
        Project project = getProject(projectName);
        RDFResource owlOntology = getOWLOntologyObject(project);
        return owlOntology.getURI();
    }

    public List<AnnotationData> getAnnotationProperties(String projectName, String entityName) {
        ArrayList<AnnotationData> annotations = new ArrayList<AnnotationData>();
        Project project = getProject(projectName);

        // Frames ontologies don't have annotations.
        if (!isOWLOntology(project)) {
            return annotations;
        }

        OWLModel owlModel = (OWLModel) project.getKnowledgeBase();
        RDFResource owlOntology = owlModel.getOWLOntologyByURI(URIUtilities.createURI(entityName));

        // Loop through RDF properties for the ontology object.
        for (Object o : owlOntology.getRDFProperties()) {
            RDFProperty property = (RDFProperty) o;

            // Filter on annotation properties.
            if (property.isAnnotationProperty()) {

                // Loop through property values for each property.
                for (Object value : owlOntology.getPropertyValues(property)) {
                    String propName = property.getBrowserText();
                    AnnotationData annotation = new AnnotationData(propName);

                    /*
                     * TODO: This code needs to be fixed later. It makes the
                     * assumption that all values are strings, but this will not
                     * be true going forward.
                     */
                    if (value instanceof RDFSLiteral) {
                        RDFSLiteral literal = (RDFSLiteral) value;
                        annotation.setLang(literal.getLanguage());
                        annotation.setValue(literal.getString());
                    } else {
                        annotation.setValue(value.toString());
                    }

                    annotations.add(annotation);
                }
            }
        }

        Collections.sort(annotations, new AnnotationDataComparator());

        return annotations;
    }

    class AnnotationDataComparator implements Comparator<AnnotationData> {
        public int compare(AnnotationData ad1, AnnotationData ad2) {
            return ad1.getName().compareTo(ad2.getName());
        }
    }

    private RDFResource getOWLOntologyObject(Project project) {
        RDFResource owlOntology = null;

        if (project != null) {
            OWLModel owlModel = (OWLModel) project.getKnowledgeBase();
            owlOntology = owlModel.getDefaultOWLOntology();
        }

        return owlOntology;
    }

    @SuppressWarnings("rawtypes")
    public List<EntityData> getDirectTypes(String projectName, String instanceName) {
        Project project = getProject(projectName);
        if (project == null) {
            return null;
        }

        KnowledgeBase kb = project.getKnowledgeBase();

        final Instance instance = kb.getInstance(instanceName);
        if (instance == null) {
            return null;
        }

        return createEntityList((List)instance.getDirectTypes());
    }

    @SuppressWarnings("unchecked")
    public List<SubclassEntityData> getSubclasses(String projectName, String className) {
        ArrayList<SubclassEntityData> subclassesData = new ArrayList<SubclassEntityData>();

        Project project = getProject(projectName);
        if (project == null) {
            return subclassesData;
        }
        KnowledgeBase kb = project.getKnowledgeBase();

        Cls superCls = kb.getCls(className);
        if (superCls == null) {
            return subclassesData;
        }

        ArrayList<Cls> subclasses = new ArrayList<Cls>(superCls.getVisibleDirectSubclasses());
        Collections.sort(subclasses, new FrameComparator());

        for (Object element : subclasses) {
            Cls subcls = (Cls) element;

            if (!subcls.isSystem()) {
                SubclassEntityData subclassEntityData = new SubclassEntityData(subcls.getName(),
                        getBrowserText(subcls), createEntityList((List) subcls.getDirectTypes()), subcls.getVisibleDirectSubclassCount());
                subclassesData.add(subclassEntityData);
                subclassEntityData.setLocalAnnotationsCount(HasAnnotationCache.getAnnotationCount(subcls));
                subclassEntityData.setChildrenAnnotationsCount(HasAnnotationCache.getChildrenAnnotationCount(subcls));

                String user = KBUtil.getUserInSession(getThreadLocalRequest());
                if (user != null) {
                    subclassEntityData.setWatch(WatchedEntitiesCache.getCache(project).getWatchType(user, subcls.getName()));
                }

            }
        }

        return subclassesData;
    }

    //TODO: move to utility
    public static String getBrowserText(Frame frame) {
        String bt = frame.getBrowserText();
        if (bt.contains("'")) {
            //delete any leading and trailing 's if present
            bt = bt.replaceAll("^'|'$", "");
            //delete all 's preceding or following any of these characters: [SPACE].-_
            bt = bt.replaceAll("'([ .-_])|([ .-_])'", "$1$2");
        }
        return bt;
    }

    public EntityData getRootEntity(String projectName) {
        Project project = getProject(projectName);

        if (project == null) {
            return null;
        }

        KnowledgeBase kb = project.getKnowledgeBase();
        Cls root = kb.getRootCls();

        if (kb instanceof OWLModel) {
            root = ((OWLModel) kb).getOWLThingClass();
        }

        EntityData rootEd =  createEntityData(root);

        String user = KBUtil.getUserInSession(getThreadLocalRequest());
        rootEd.setWatch(WatchedEntitiesCache.getCache(project).getWatchType(user, root.getName()));

        return rootEd;
    }

    public EntityData getEntity(String projectName, String entityName) {
        Project project = getProject(projectName);

        if (project == null) {
            return null;
        }

        KnowledgeBase kb = project.getKnowledgeBase();
        Frame frame = kb.getFrame(entityName);

        if (frame == null && kb instanceof OWLModel) {
            frame = ((OWLModel) kb).getRDFResource(entityName);
        }

        if (frame == null) {
            return null;
        }

        EntityData ed = createEntityData(frame);
        ed.setTypes(createEntityList((List) ((Instance) frame).getDirectTypes()));
        ed.setWatch(WatchedEntitiesCache.getCache(project).getWatchType(KBUtil.getUserInSession(getThreadLocalRequest()), frame.getName()));

        return ed;
    }

    public ArrayList<EntityData> getSubproperties(String projectName, String propertyName) {
        ArrayList<EntityData> subpropertyData = new ArrayList<EntityData>();

        Project project = getProject(projectName);
        if (project == null) {
            return subpropertyData;
        }

        KnowledgeBase kb = project.getKnowledgeBase();

        ArrayList<Slot> subproperties = new ArrayList<Slot>();
        if (propertyName == null) { // property root case
            subproperties.addAll(getRootProperties(kb));
        } else {
            Slot superProperty = kb.getSlot(propertyName);
            if (superProperty == null) {
                return subpropertyData;
            }
            subproperties.addAll(superProperty.getDirectSubslots());
        }

        Collections.sort(subproperties, new FrameComparator<Slot>());

        for (Slot subprop : subproperties) {
            if (!subprop.isSystem() && subprop.isVisible()) {
                PropertyEntityData entityData = createPropertyEntityData(subprop, null, true);
                entityData.setPropertyType(getPropertyType(subprop));
                subpropertyData.add(entityData);
            }
        }

        //TODO: Fix me. Decide whether to show the OWL properties or not
        if (propertyName == null && kb instanceof OWLModel) {
            OWLModel owlModel = (OWLModel) kb;
            PropertyEntityData entityData1 = createPropertyEntityData(owlModel.getRDFSLabelProperty(), null, true);
            entityData1.setPropertyType(PropertyType.ANNOTATION);
            entityData1.setIsSystem(true);
            subpropertyData.add(entityData1);

            PropertyEntityData entityData2 = createPropertyEntityData(owlModel.getRDFSCommentProperty(), null, true);
            entityData2.setIsSystem(true);
            entityData2.setPropertyType(PropertyType.ANNOTATION);
            subpropertyData.add(entityData2);
        }

        return subpropertyData;
    }

    public static PropertyType getPropertyType(Slot slot) {
        if (slot instanceof OWLObjectProperty) {
            return PropertyType.OBJECT;
        } else if (slot instanceof OWLDatatypeProperty) {
            return PropertyType.DATATYPE;
        } else if (slot instanceof RDFProperty && ((RDFProperty) slot).isAnnotationProperty()) {
            return PropertyType.ANNOTATION;
        }
        return null;
    }

    public void addPropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity,
            EntityData valueEntityData, String user, String operationDescription) {
        Project project = getProject(projectName);
        if (project == null) {
            throw new IllegalArgumentException("Add operation failed. Unknown project: " + projectName);
        }
        KnowledgeBase kb = project.getKnowledgeBase();
        Instance subject = kb.getInstance(entityName);
        if (subject == null) {
            throw new IllegalArgumentException("Add operation failed. Unknown subject: " + entityName);
        }
        Slot property = kb.getSlot(propertyEntity.getName());
        if (property == null) {
            new IllegalArgumentException("Add operation failed. Unknown property: " + propertyEntity.getName());
        }

        Object value = getProtegeObject(kb, valueEntityData, property);
        if (value != null) {
            synchronized (kb) {
                KBUtil.morphUser(kb, user);
                boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
                try {
                    if (runsInTransaction) {
                        kb.beginTransaction(operationDescription);
                    }

                    subject.addOwnSlotValue(property, value);

                    if (runsInTransaction) {
                        kb.commitTransaction();
                    }
                } catch (Exception e) {
                    Log.getLogger().log(
                            Level.SEVERE,
                            "Error at setting value in " + projectName + " subj: " + subject + " pred: " + property
                            + " value: " + value, e);
                    if (runsInTransaction) {
                        kb.rollbackTransaction();
                    }
                    throw new RuntimeException(e.getMessage(), e);
                } finally {
                    KBUtil.restoreUser(kb);
                }
            }
        } else {
            throw new IllegalArgumentException("Add operation failed. Invalid value: " + valueEntityData.getName());
        }
    }

    public void removePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity,
            EntityData valueEntityData, String user, String operationDescription) {
        Project project = getProject(projectName);
        if (project == null) { return;  }
        KnowledgeBase kb = project.getKnowledgeBase();
        Instance subject = kb.getInstance(entityName);
        if (subject == null) { return; }
        Slot property = kb.getSlot(propertyEntity.getName());
        if (property == null) { return; }

        Object value = getProtegeObject(kb, valueEntityData, property);

        if (value == null || value.equals("")) { return; }

        if (value instanceof RDFSLiteral) {
            value = value.toString();
        }

        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                subject.removeOwnSlotValue(property, value);

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(
                        Level.SEVERE,
                        "Error at removing value in " + projectName + " subj: " + subject + " pred: " + property
                        + " value: " + value, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
    }


    public void replacePropertyValue(String projectName, String entityName, PropertyEntityData propertyEntity,
            EntityData oldValue, EntityData newValue, String user, String operationDescription) {
        Project project = getProject(projectName);
        if (project == null) {
            return;
        }
        KnowledgeBase kb = project.getKnowledgeBase();
        boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }
                if (newValue.getName() != null) {
                    addPropertyValue(projectName, entityName, propertyEntity, newValue, user, null);
                    KBUtil.morphUser(kb, user); //hack
                }
                removePropertyValue(projectName, entityName, propertyEntity, oldValue, user, null);
                KBUtil.morphUser(kb, user); //hack

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(
                        Level.SEVERE,  "Error at replacing value in " + projectName + " subj: " + entityName +
                        " pred: " + propertyEntity + " old value: " + oldValue + " new value: " + newValue, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
    }

    public void setPropertyValues(String projectName, String entityName, PropertyEntityData propertyEntity,
            List<EntityData> valueEntityData, String user, String operationDescription) {
        Project project = getProject(projectName);
        if (project == null) {
            throw new IllegalArgumentException("Set operation failed. Unknown project: " + projectName);
        }
        KnowledgeBase kb = project.getKnowledgeBase();
        Instance subject = kb.getInstance(entityName);
        if (subject == null) {
            throw new IllegalArgumentException("Set operation failed. Unknown subject: " + entityName);
        }
        Slot property = kb.getSlot(propertyEntity.getName());
        if (property == null) {
            new IllegalArgumentException("Set operation failed. Unknown property: " + propertyEntity.getName());
        }
        Collection<Object> values = new ArrayList<Object>();
        for (EntityData entityData : valueEntityData) {
            Object value = getProtegeObject(kb, entityData, property);
            if (value != null) {
                values.add(value);
            } else {
                throw new IllegalArgumentException("Set operation failed. Invalid value: " + entityData.getName());
            }
        }
        boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                subject.setOwnSlotValues(property, values);

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(
                        Level.SEVERE,
                        "Error at setting value in " + projectName + " subj: " + subject + " pred: " + property
                        + " value: " + values, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
    }

    private static Object getProtegeObject(KnowledgeBase kb, EntityData entityData, Slot property) {
        if (entityData == null || entityData.getName() == null) {
            return null;
        }
        edu.stanford.bmir.protege.web.client.rpc.data.ValueType valueType = entityData.getValueType();
        if (valueType == null) {
            ValueType propValueType = property.getValueType();  // get Protege value type
            valueType = propValueType == null ?
                    edu.stanford.bmir.protege.web.client.rpc.data.ValueType.String :
                        edu.stanford.bmir.protege.web.client.rpc.data.ValueType.valueOf(propValueType.toString());
        }
        String value = entityData.getName();
        switch (valueType) {
        case String:
            return value;
        case Symbol:
            return value;
        case Instance:
            return kb.getInstance(value);
        case Integer:
            try {
                return new Integer(value);
            } catch (NumberFormatException e) {
                return null;
            }
        case Float:
            try {
                return new Float(value);
            } catch (NumberFormatException e) {
                return null;
            }
        case Boolean:
            return new Boolean(value);
        case Date:
            return value; // FIXME for OWL
        case Literal: {
            if (!(kb instanceof OWLModel)) {
                return value;
            }
            OWLModel owlModel = (OWLModel) kb;
            // FIXME: should work with all datatypes
            RDFSLiteral lit = DefaultRDFSLiteral.create(owlModel, value, owlModel.getRDFSDatatypeByName("xsd:string"));
            return lit;
        }
        case Any: { //default to String...
            return value;
        }
        default:
            return null;
        }
    }

    protected Collection<Slot> getRootProperties(KnowledgeBase kb) {
        List<Slot> results = new ArrayList<Slot>(kb.getSlots());
        Iterator<Slot> i = results.iterator();
        while (i.hasNext()) {
            Slot slot = i.next();
            if (slot.getDirectSuperslotCount() > 0) {
                i.remove();
            }
        }
        Collections.sort(results, new FrameComparator());
        return results;
    }

    public List<Triple> getEntityTriples(String projectName, String entityName) {
        ArrayList<Triple> triples = new ArrayList<Triple>();

        Project project = getProject(projectName);
        if (project == null) {
            return triples;
        }

        KnowledgeBase kb = project.getKnowledgeBase();
        Instance inst = kb.getInstance(entityName);
        if (inst == null) {
            return triples;
        }

        for (Object element : inst.getOwnSlots()) {
            Slot slot = (Slot) element;
            if (!slot.isSystem()) {
                triples.addAll(getTriples(inst, slot));
            }
        }

        // add rdfs:comment and rdfs:label
        if (kb instanceof OWLModel) {
            triples.addAll(getTriples(inst, ((OWLModel) kb).getRDFSCommentProperty()));
            triples.addAll(getTriples(inst, ((OWLModel) kb).getRDFSLabelProperty()));
            // domain and range should not be necessarily retrieved
            triples.addAll(getTriples(inst, ((OWLModel) kb).getRDFSDomainProperty()));
            triples.addAll(getTriples(inst, ((OWLModel) kb).getRDFSRangeProperty()));
        }

        //TODO - should we show this or not?
        /*
        if (inst instanceof Cls) {
            triples.addAll(getPropertiesInDomain((Cls) inst));
        }
         */

        return triples.size() == 0 ? null : triples;
    }

    public List<Triple> getEntityTriples(String projectName, List<String> entities, List<String> properties) {
        ArrayList<Triple> triples = new ArrayList<Triple>();
        Project project = getProject(projectName);
        if (project == null) {
            return triples;
        }

        KnowledgeBase kb = project.getKnowledgeBase();

        for (String entityName : entities) {
            Instance inst = kb.getInstance(entityName);
            if (inst != null) {
                for (String propName : properties) {
                    Slot slot = kb.getSlot(propName);
                    if (slot != null) {
                        triples.addAll(getTriples(inst, slot, true));
                    }
                }
            }
        }
        return triples.size() == 0 ? null : triples;
    }

    public List<Triple> getEntityTriples(String projectName, List<String> entities, List<String> properties, List<String> reifiedProperties) {
        ArrayList<Triple> triples = new ArrayList<Triple>();
        Project project = getProject(projectName);
        if (project == null) {
            return triples;
        }

        KnowledgeBase kb = project.getKnowledgeBase();

        for (String entityName : entities) {
            Instance inst = kb.getInstance(entityName);
            if (inst != null) {
                for (String propName : properties) {
                    Slot slot = kb.getSlot(propName);
                    if (slot != null) {
                        Collection values = inst.getOwnSlotValues(slot);
                        for (Object value : values) {
                            if (value instanceof Instance) {
                                Instance valueInst = (Instance) value;
                                for (String reifiedPropName : reifiedProperties) {
                                    Slot reifiedSlot = kb.getSlot(reifiedPropName);
                                    if (reifiedPropName != null) {
                                        triples.addAll(getTriples(valueInst, reifiedSlot, false));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return triples.size() == 0 ? null : triples;
    }


    public List<EntityPropertyValues> getEntityPropertyValues(String projectName, List<String> entities, List<String> properties, List<String> reifiedProperties) {
        List<EntityPropertyValues> entityPropValues = new ArrayList<EntityPropertyValues>();
        Project project = getProject(projectName);
        if (project == null) {
            return entityPropValues.size() == 0 ? null : entityPropValues;
        }

        KnowledgeBase kb = project.getKnowledgeBase();

        for (String entityName : entities) {
            Instance inst = kb.getInstance(entityName);
            if (inst != null) {
                for (String propName : properties) {
                    Slot slot = kb.getSlot(propName);
                    if (slot != null) {
                        Collection values = inst.getOwnSlotValues(slot);
                        for (Object value : values) {
                            if (value instanceof Instance) {
                                Instance valueInst = (Instance) value;
                                EntityPropertyValues epv = new EntityPropertyValues(createEntityData(valueInst));
                                for (String reifiedPropName : reifiedProperties) {
                                    Slot reifiedSlot = kb.getSlot(reifiedPropName);
                                    if (reifiedSlot != null) {
                                        epv.addPropertyValues(new PropertyEntityData(reifiedSlot.getName()), createEntityList((List<Object>) valueInst.getOwnSlotValues(reifiedSlot)));
                                    }
                                }
                                entityPropValues.add(epv);
                            }
                        }
                    }
                }
            }
        }
        return entityPropValues.size() == 0 ? null : entityPropValues;
    }


    private ArrayList<Triple> getPropertiesInDomain(Cls cls) {
        ArrayList<Triple> triples = new ArrayList<Triple>();
        KnowledgeBase kb = cls.getKnowledgeBase();

        for (Object element : cls.getTemplateSlots()) {
            Slot slot = (Slot) element;

            // hack
            if (slot.isSystem() || slot.getName().startsWith(ProtegeNames.PROTEGE_OWL_NAMESPACE)
                    || slot.getName().startsWith(XSPNames.NS)) {
                continue;
            }
            // TODO: refactor this code

            if (slot.getValueType() == ValueType.INSTANCE) {
                Collection directDomain = slot.getDirectDomain();
                Collection allowedClses = slot.getAllowedClses();
                if (kb instanceof OWLModel) {
                    if (directDomain != null && directDomain.size() > 0 && !directDomain.contains(kb.getRootCls())) {
                        if (allowedClses.size() == 0) {
                            allowedClses.add(kb.getRootCls());
                        }
                        triples.addAll(getClsTriples(cls, slot, allowedClses));
                    }
                } else {
                    triples.addAll(getClsTriples(cls, slot, allowedClses));
                }
            } else if (slot.getValueType() == ValueType.CLS) {
                Collection directDomain = slot.getDirectDomain();
                Collection allowedParents = slot.getAllowedParents();
                if (kb instanceof OWLModel) {
                    if (directDomain != null && directDomain.size() > 0 && !directDomain.contains(kb.getRootCls())) {
                        if (allowedParents.size() == 0) {
                            allowedParents.add(kb.getRootCls());
                        }
                        triples.addAll(getClsTriples(cls, slot, allowedParents));
                    }
                } else {
                    triples.addAll(getClsTriples(cls, slot, allowedParents));
                }
            } else {
                // TODO
                Collection directDomain = slot.getDirectDomain();
                if (kb instanceof OWLModel) {
                    if (directDomain != null && directDomain.size() > 0 && !directDomain.contains(kb.getRootCls())) {
                        triples.add(createTriple(cls, slot, slot.getValueType().toString()));
                    }
                } else {
                    triples.add(createTriple(cls, slot, slot.getValueType().toString()));
                }
            }
        }

        return triples;
    }

    private List<Triple> getClsTriples(Cls cls, Slot slot, Collection values) {
        List<Triple> triples = new ArrayList<Triple>();
        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            Object value = iterator.next();
            Triple triple = new Triple(createEntityData(cls), createPropertyEntityData(slot, cls, false), createEntityData(value));
            triples.add(triple);
        }
        return triples;
    }

    protected Collection<Triple> getTriples(Instance inst, Slot slot) {
        return getTriples(inst, slot, false);
    }

    protected Collection<Triple> getTriples(Instance inst, Slot slot, boolean includeEmptyValues) {
        return getTriples(inst, slot, inst.getOwnSlotValues(slot), includeEmptyValues);
    }

    protected Collection<Triple> getTriples(Instance inst, Slot slot, Collection values) {
        return getTriples(inst, slot, values, false);
    }

    protected Collection<Triple> getTriples(Instance inst, Slot slot, Collection values, boolean includeEmptyValues) {
        ArrayList<Triple> triples = new ArrayList<Triple>();

        if (includeEmptyValues && (values == null || values.size() == 0)) {
            values = new ArrayList();
            values.add(null);
        }

        for (Iterator it = values.iterator(); it.hasNext();) {
            Object object = it.next();
            triples.add(createTriple(inst, slot, object));
        }

        return triples;
    }

    public List<Triple> getRelatedProperties(String projectName, String className) {
        ArrayList<Triple> triples = new ArrayList<Triple>();
        Project project = getProject(projectName);
        if (project == null) {
            return triples;
        }
        KnowledgeBase kb = project.getKnowledgeBase();
        Cls cls = kb.getCls(className);
        if (cls == null) {
            return triples;
        }

        //TODO: this should be reimplemented to match the Properties View output from the rich client
        //TODO: the domains are inherited to the subclasses, which is wrong in OWL (but intuitive..)

        return getPropertiesInDomain(cls);
    }

    protected Triple createTriple(Instance instance, Slot slot, Object object) {
        EntityData subj = createEntityData(instance);
        PropertyEntityData pred = createPropertyEntityData(slot, instance.getDirectType(), false);
        EntityData obj = createEntityData(object, false);
        return new Triple(subj, pred, obj);
    }

    public static EntityData createEntityData(Object object) {
        return createEntityData(object, true);
    }

    public static List<EntityData> createEntityList(List list) {
        ArrayList<EntityData> edList = new ArrayList<EntityData>();
        for (Object element : list) {
            Object obj = element;
            edList.add(createEntityData(obj));
        }
        return edList;
    }

    public static EntityData createEntityData(Object object, boolean computeAnnotations) {
        if (object == null) {
            return null;
        }

        if (object instanceof Frame) {
            Frame objFrame = (Frame) object;
            EntityData entityData;
            if (objFrame instanceof Slot) {
                entityData = new PropertyEntityData(objFrame.getName(), getBrowserText(objFrame), null);
                if (computeAnnotations) {
                    entityData.setChildrenAnnotationsCount(HasAnnotationCache.getChildrenAnnotationCount(objFrame));
                    entityData.setLocalAnnotationsCount(HasAnnotationCache.getAnnotationCount(objFrame));
                }
                ((PropertyEntityData) entityData).setPropertyType(OntologyServiceImpl.getPropertyType((Slot) objFrame));
            } else {
                entityData = new EntityData(objFrame.getName(), getBrowserText(objFrame), null);
                if (computeAnnotations) {
                    entityData.setChildrenAnnotationsCount(HasAnnotationCache.getChildrenAnnotationCount(objFrame));
                    entityData.setLocalAnnotationsCount(HasAnnotationCache.getAnnotationCount(objFrame));
                }
            }
            return entityData;
        } else {
            return new EntityData(object.toString());
        }
    }

    public static PropertyEntityData createPropertyEntityData(Slot property, Cls cls, boolean computeAnnotations) {
        if (property == null) {
            return null;
        }
        PropertyEntityData ped = new PropertyEntityData(property.getName(), getBrowserText(property), null);
        if (computeAnnotations) {
            ped.setChildrenAnnotationsCount(HasAnnotationCache.getChildrenAnnotationCount(property));
            ped.setLocalAnnotationsCount(HasAnnotationCache.getAnnotationCount(property));
        }
        if (property instanceof RDFProperty) {
            //TODO: for now check only if the property is functional
            if (((RDFProperty) property).isFunctional()) {
                ped.setMaxCardinality(1);
            }
        } else {
            ped.setMinCardinality(cls == null ? property.getMinimumCardinality() : cls
                    .getTemplateSlotMinimumCardinality(property));
            ped.setMaxCardinality(cls == null ? property.getMaximumCardinality() : cls
                    .getTemplateSlotMaximumCardinality(property));
        }
        if (property.getValueType() == ValueType.STRING) {
            ped.setValueType(edu.stanford.bmir.protege.web.client.rpc.data.ValueType.String);
        } else if (property.getValueType() == ValueType.INSTANCE || property.getValueType() == ValueType.CLS) {
            ped.setValueType(edu.stanford.bmir.protege.web.client.rpc.data.ValueType.Instance);
            ped.setAllowedValues(createEntityList((List) property.getAllowedClses())); // FIXME
        } else if (property.getValueType() == ValueType.BOOLEAN) {
            ped.setValueType(edu.stanford.bmir.protege.web.client.rpc.data.ValueType.Boolean);
        } else if (property.getValueType() == ValueType.SYMBOL) {
            ped.setValueType(edu.stanford.bmir.protege.web.client.rpc.data.ValueType.Symbol);
            ped.setAllowedValues(createEntityList((List<Object>) property.getAllowedValues()));
        } else {
            ped.setValueType(edu.stanford.bmir.protege.web.client.rpc.data.ValueType.Any);
        }
        return ped;
    }

    public List<EntityData> getIndividuals(String projectName, String className) {
        ArrayList<EntityData> instancesData = new ArrayList<EntityData>();

        Project project = getProject(projectName);

        if (project == null) {
            return instancesData;
        }

        KnowledgeBase kb = project.getKnowledgeBase();
        Cls cls = kb.getCls(className);

        if (cls == null) {
            return instancesData;
        }

        List<Instance> instances = new ArrayList<Instance>(cls.getDirectInstances());
        Collections.sort(instances, new FrameComparator());

        for (Object element : instances) {
            Instance inst = (Instance) element;

            if (inst.isVisible()) { //TODO: is this a good check?
                instancesData.add(createEntityData(inst));
            }
        }

        return instancesData;
    }


    public PaginationData<EntityData> getIndividuals(String projectName, String className, int start, int limit, String sort, String dir) {
        List<EntityData> individuals = getIndividuals(projectName, className);
        return PaginationServerUtil.pagedRecords(individuals, start, limit, sort, dir);
    }


    public ImportsData getImportedOntologies(String projectName) {
        Project project = getProject(projectName);
        ImportsData id = new ImportsData();

        if (isOWLOntology(project)) {
            // OWL ontology
            OWLModel owlModel = (OWLModel) project.getKnowledgeBase();
            id = copyOWLTree(id, owlModel.getDefaultOWLOntology());
        } else {
            // Frames ontology
            Tree<URI> tree = project.getProjectTree();
            id = copyTree(id, tree, tree.getRoot());
        }

        return id;
    }

    private ImportsData copyTree(ImportsData data, Tree<URI> tree, URI node) {
        ImportsData id = data;
        id.setName(URIUtilities.getBaseName(node));

        Set<URI> children = tree.getChildren(node);
        for (URI childNode : children) {
            ImportsData childID = new ImportsData();
            childID = copyTree(childID, tree, childNode);
            id.addImport(childID);
        }

        return id;
    }

    private ImportsData copyOWLTree(ImportsData data, OWLOntology ontology) {
        ImportsData id = data;
        id.setName(ontology.getURI());

        Collection<RDFResource> imports = ontology.getImportResources();
        for (Object o : imports) {
            OWLOntology childOnt = (OWLOntology) o;
            ImportsData childID = new ImportsData(childOnt.getURI());
            childID = copyOWLTree(childID, childOnt);
            id.addImport(childID);
        }

        return id;
    }

    public ArrayList<MetricData> getMetrics(String projectName) {
        ArrayList<MetricData> metrics = new ArrayList<MetricData>();
        Project project = getProject(projectName);

        if (isOWLOntology(project)) {
            // OWL ontology
            OWLModel owlModel = (OWLModel) project.getKnowledgeBase();
            ModelMetrics modelMetrics = new ModelMetrics(owlModel);
            modelMetrics.calculateMetrics();

            Integer count = new Integer(modelMetrics.getNamedClassCount());
            metrics.add(new MetricData("Class count", count.toString()));

            count = new Integer(modelMetrics.getDatatypePropertyCount());
            metrics.add(new MetricData("Datatype property count", count.toString()));

            count = new Integer(modelMetrics.getObjectPropertyCount());
            metrics.add(new MetricData("Object property count", count.toString()));

            count = new Integer(modelMetrics.getAnnotationPropertyCount());
            metrics.add(new MetricData("Annotation property count", count.toString()));

            count = new Integer(modelMetrics.getOwlIndividualCount());
            metrics.add(new MetricData("Individual count", count.toString()));

            DLExpressivityChecker checker = new DLExpressivityChecker(owlModel);
            checker.check();
            metrics.add(new MetricData("DL Expressivity", checker.getDLName()));

        } else {
            // Frames ontology
            FrameCounts frameCounts = project.getKnowledgeBase().getFrameCounts();

            Integer count = new Integer(frameCounts.getTotalClsCount());
            metrics.add(new MetricData("Class count", count.toString()));

            count = new Integer(frameCounts.getTotalSlotCount());
            metrics.add(new MetricData("Slot count", count.toString()));

            count = new Integer(frameCounts.getTotalFacetCount());
            metrics.add(new MetricData("Facet count", count.toString()));

            count = new Integer(frameCounts.getTotalSimpleInstanceCount());
            metrics.add(new MetricData("Instance count", count.toString()));

            count = new Integer(frameCounts.getTotalFrameCount());
            metrics.add(new MetricData("Frame count", count.toString()));
        }

        return metrics;
    }

    private boolean isOWLOntology(Project project) {
        KnowledgeBase kb = project.getKnowledgeBase();
        return (kb instanceof OWLModel) ? true : false;
    }

    public List<OntologyEvent> getEvents(String projectName, long fromVersion) {
        ServerProject<Project> serverProject = Protege3ProjectManager.getProjectManager().getServerProject(projectName, false);
        if (serverProject == null) {
            throw new RuntimeException("Could not get ontology: " + projectName + " from server.");
        }
        return serverProject.isLoaded() ? serverProject.getEvents(fromVersion) : null;
    }

    public EntityData createCls(String projectName, String clsName, String superClsName, String user,
            String operationDescription) {
        return createCls(projectName, clsName, superClsName, true, user, operationDescription);
    }

    public EntityData createCls(String projectName, String clsName, String superClsName, boolean createWithMetaClses, String user, String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        Cls superCls = superClsName == null ? kb.getRootCls() : kb.getCls(superClsName);
        EntityData clsEntity = null;
        Cls cls = null;

        boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                cls = isOWLOntology(project) ?
                        (superCls != null && superCls instanceof OWLNamedClass ?
                                ((OWLModel) kb).createOWLNamedSubclass(clsName, (OWLNamedClass) superCls) :
                                    ((OWLModel) kb).createOWLNamedClass(clsName)) :     //TODO check for RDFSNamedClass as well
                                        kb.createCls(clsName, superCls == null ? kb.getRootClses()
                                                : CollectionUtilities.createCollection(superCls));

                                if(createWithMetaClses && superCls != null){
                                    Collection<Cls> directTypes = superCls.getDirectTypes();
                                    for (Cls type : directTypes) {
                                        if (! cls.hasDirectType(type)) {
                                            cls.addDirectType(type);
                                        }
                                    }
                                }

                                if (runsInTransaction) {
                                    kb.commitTransaction();
                                }
            } catch (Exception e) {
                Log.getLogger().log(Level.SEVERE, "Error at creating class in " + projectName + " class: " + clsName, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Error at creating class " + clsName + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }

        if (cls != null) {
            clsEntity = createEntityData(cls, false);
        }

        return clsEntity;
    }

    public EntityData createClsWithProperty(String projectName, String clsName, String superClsName, String propertyName,
            EntityData propertyValue, String user, String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        Cls superCls = superClsName == null ? kb.getRootCls() : kb.getCls(superClsName);
        EntityData clsEntity = null;
        Cls cls = null;

        Slot property = kb.getSlot(propertyName);
        Object value = getProtegeObject(kb, propertyValue, property);
        if (propertyValue != null && value == null) {
            Log.getLogger().log(Level.WARNING, "Could not set property value " + propertyValue +
                    " for property " + property + " in create cls with property method.");
        }

        boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                cls = isOWLOntology(project) ?
                        (superCls != null && superCls instanceof OWLNamedClass ?
                                ((OWLModel) kb).createOWLNamedSubclass(clsName, (OWLNamedClass) superCls) :
                                    ((OWLModel) kb).createOWLNamedClass(clsName)) :     //TODO check for RDFSNamedClass as well
                                        kb.createCls(clsName, superCls == null ?
                                                kb.getRootClses() :
                                                    CollectionUtilities.createCollection(superCls));

                                if (property != null) {
                                    if (isOWLOntology(project)) {
                                        ((RDFSClass)cls).setPropertyValue((RDFProperty)property, value);
                                    }
                                    else {
                                        cls.setOwnSlotValue(property, value);
                                    }
                                }

                                if (runsInTransaction) {
                                    kb.commitTransaction();
                                }
            } catch (Exception e) {
                Log.getLogger()
                .log(Level.SEVERE, "Error at creating class with property in " + projectName + " class: " + clsName + "property name: " + projectName + " property value: " + propertyValue, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Error at creating class " + clsName + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }

        if (cls != null) {
            clsEntity = createEntityData(cls, false);
        }

        return clsEntity;
    }

    public void deleteEntity(String projectName, String entityName, String user, String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        Frame frame = kb.getFrame(entityName);
        if (frame == null) {
            return;
        }
        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                frame.delete();

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(Level.SEVERE, "Error at deleting in " + projectName + " entity: " + entityName, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Error at deleting " + entityName + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
    }

    public void addSuperCls(String projectName, String clsName, String superClsName, String user,
            String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();
        Cls cls = kb.getCls(clsName);
        Cls superCls = kb.getCls(superClsName);
        if (cls == null || superCls == null) {
            return;
        }

        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                cls.addDirectSuperclass(superCls);

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(
                        Level.SEVERE,
                        "Error at adding direct superclass in " + projectName + " class: " + clsName + " supercls: "
                        + superClsName, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Error at adding to class " + clsName + " superclass: " + superClsName
                        + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
    }

    public void removeSuperCls(String projectName, String clsName, String superClsName, String user,
            String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        Cls cls = kb.getCls(clsName);
        Cls superCls = kb.getCls(superClsName);
        if (cls == null || superCls == null) {
            return;
        }
        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                cls.removeDirectSuperclass(superCls);

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(
                        Level.SEVERE,
                        "Error at removing superclass in " + projectName + " class: " + clsName + " superclass: " + superClsName, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Error at removing from class " + clsName + " superclass: " + superClsName
                        + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
    }

    public List<EntityData> moveCls(String projectName, String clsName, String oldParentName, String newParentName, boolean checkForCycles,
            String user,  String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        Cls cls = kb.getCls(clsName);
        Cls oldParent = kb.getCls(oldParentName);
        Cls newParent = kb.getCls(newParentName);

        if (cls == null || oldParent == null || newParent == null) {
            return null;
        }

        synchronized (kb) {
            KBUtil.morphUser(kb, user);

            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                cls.addDirectSuperclass(newParent);
                cls.removeDirectSuperclass(oldParent);

                //if the operation has created an orphan cycle add root class as a parent
                if (!cls.getSuperclasses().contains(kb.getRootCls())) {
                    cls.addDirectSuperclass(kb.getRootCls());
                }

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(
                        Level.SEVERE,
                        "Error at moving class in " + projectName + " class: " + clsName + " old parent: "
                        + oldParentName + " new parent: " + newParentName, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Error at moving class: " + clsName + " old parent: " + oldParentName
                        + " new parent: " + newParentName + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }

        List<EntityData> res = null;
        if (checkForCycles) {
            if (cls instanceof OWLClass) {
                OWLClass owlcls = (OWLClass)cls;
                //if contains loop
                if (owlcls.getSuperclasses(true).contains(owlcls)) {
                    ArrayList<OWLClass> cyclePath = new ArrayList<OWLClass>();
                    KBUtil.getPathToSuperClass(owlcls, owlcls, cyclePath);
                    //if we really found a cycle (i.e. there was a real cycle that did not involve anonymous classes)
                    if (cyclePath.size() > 1) {
                        res = OntologyServiceImpl.createEntityList(cyclePath);
                    }
                }
            }
            else {
                //if contains loop
                if (cls.getSuperclasses().contains(cls)) {
                    ArrayList<Cls> cyclePath = new ArrayList<Cls>();
                    KBUtil.getPathToSuperClass(cls, cls, cyclePath);
                    res = OntologyServiceImpl.createEntityList(cyclePath);
                }
            }
        }

        return res;
    }

    public List<EntityData> getParents(String projectName, String className, boolean direct) {
        List<EntityData> parents = new ArrayList<EntityData>();
        Project project = getProject(projectName);

        if (project == null || className == null) {
            return parents;
        }

        KnowledgeBase kb = project.getKnowledgeBase();
        Cls cls = kb.getCls(className);
        if (cls == null) {
            return parents;
        }

        Collection<Cls> superClses = direct == true ? cls.getDirectSuperclasses() : cls.getSuperclasses();
        for (Cls parent : superClses) {
            if (!(parent.isSystem() || (parent instanceof RDFResource && ((RDFResource) parent).isAnonymous()))) {
                parents.add(createEntityData(parent));
            }
        }

        return parents;
    }

    //String concatenation on the client is very slow

    public String getParentsHtml(String projectName, String className, boolean direct) {
        StringBuffer buffer = new StringBuffer();
        List<EntityData> parents = getParents(projectName, className, direct);

        buffer.append("<table width=\"100%\" border=\"0\" cellspacing=\"3\"  class=\"restriction_table\">");
        for (EntityData parent : parents) {
            buffer.append("<tr><td>");
            buffer.append(UIUtil.getDisplayText(parent));
            buffer.append("</td>");
            buffer.append("<td class=\"parent-column-right\"><a href=\"");
            buffer.append(UIUtil.LOCAL);
            buffer.append(UIUtil.REMOVE_PREFIX);
            buffer.append(parent.getName());
            buffer.append("\">remove</a></td></tr>");
        }

        buffer.append("</table>");
        return buffer.toString();
    }


    public EntityData createProperty(String projectName, String propertyName, String superPropName,
            PropertyType propertyType, String user, String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        if (kb.getFrame(propertyName) != null) {
            throw new RuntimeException("An entity with the same name already exists!");
        }

        boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);

        if (isOWLOntology(project)) { // OWL
            OWLModel owlModel = (OWLModel) kb;
            RDFProperty property = null;
            synchronized (kb) {
                KBUtil.morphUser(kb, user);
                try {
                    if (runsInTransaction) {
                        kb.beginTransaction(operationDescription);
                    }

                    if (propertyType == PropertyType.OBJECT) {
                        property = owlModel.createOWLObjectProperty(propertyName);
                    } else if (propertyType == PropertyType.DATATYPE) {
                        property = owlModel.createOWLDatatypeProperty(propertyName);
                    } else if (propertyType == PropertyType.ANNOTATION) {
                        property = owlModel.createAnnotationProperty(propertyName);
                    }
                    if (superPropName != null) {
                        RDFProperty superProp = owlModel.getRDFProperty(superPropName);
                        if (superProp != null) {
                            property.addSuperproperty(superProp);
                        }
                    }

                    if (runsInTransaction) {
                        kb.commitTransaction();
                    }
                } catch (Exception e) {
                    Log.getLogger().log(Level.SEVERE,
                            "Error at creating property in " + projectName + " property: " + propertyName, e);
                    if (runsInTransaction) {
                        kb.rollbackTransaction();
                    }
                    throw new RuntimeException("Error at creating property: " + propertyName + ". Message: "
                            + e.getMessage(), e);
                } finally {
                    KBUtil.restoreUser(kb);
                }
            }
            return createEntityData(property, false);
        } else { // Frames
            Slot slot = null;
            synchronized (kb) {
                KBUtil.morphUser(kb, user);
                try {
                    if (runsInTransaction) {
                        kb.beginTransaction(operationDescription);
                    }

                    slot = kb.createSlot(propertyName);
                    slot.setValueType(propertyType == PropertyType.OBJECT ? ValueType.INSTANCE : ValueType.STRING);
                    if (superPropName != null) {
                        Slot superSlot = kb.getSlot(superPropName);
                        if (superSlot != null) {
                            slot.addDirectSuperslot(superSlot);
                        }
                    }

                    if (runsInTransaction) {
                        kb.commitTransaction();
                    }
                } catch (Exception e) {
                    Log.getLogger().log(Level.SEVERE,
                            "Error at creating property in " + projectName + " property: " + propertyName, e);
                    if (runsInTransaction) {
                        kb.rollbackTransaction();
                    }
                    throw new RuntimeException("Error at creating property: " + propertyName + ". Message: "
                            + e.getMessage(), e);
                } finally {
                    KBUtil.restoreUser(kb);
                }
            }
            return createEntityData(slot, false);
        }
    }

    public EntityData createDatatypeProperty(String projectName, String propertyName, String superPropName,
            String user, String operationDescription) {
        return createProperty(projectName, propertyName, superPropName, PropertyType.DATATYPE, user,
                operationDescription);
    }

    public EntityData createObjectProperty(String projectName, String propertyName, String superPropName, String user,
            String operationDescription) {
        return createProperty(projectName, propertyName, superPropName, PropertyType.OBJECT, user, operationDescription);
    }

    public EntityData createAnnotationProperty(String projectName, String propertyName, String superPropName,
            String user, String operationDescription) {
        return createProperty(projectName, propertyName, superPropName, PropertyType.ANNOTATION, user,
                operationDescription);
    }

    public EntityData createInstance(String projectName, String instName, String typeName, String user,
            String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        Cls type = (typeName == null) ? kb.getRootCls() : kb.getCls(typeName);

        if (type == null) {
            Log.getLogger().warning("Could not create instance " + instName + " of type " + typeName + ". Null type");
            throw new IllegalArgumentException("Could not create instance " + instName + " of type " + typeName
                    + ". Null type");
        }

        Instance inst = null;
        synchronized (kb) {
            KBUtil.morphUser(kb, user);

            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                inst = type.createDirectInstance(instName);

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(Level.SEVERE, "Could not create instance " + instName + " of type " + typeName, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new IllegalArgumentException("Could not create instance " + instName + " of type " + typeName);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
        return createEntityData(inst);
    }

    public EntityData createInstanceValue(String projectName, String instName, String typeName, String subjectEntity,
            String propertyEntity, String user, String operationDescription) {

        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();
        Slot slot = kb.getSlot(propertyEntity);

        if (slot == null) {
            Log.getLogger().warning("Invalid property name: " + propertyEntity);
            throw new IllegalArgumentException("Operation failed. Possibly invalid configuration for property " + propertyEntity);
        }

        //if type is null - try to get the range of the property
        if (typeName == null) {
            Collection allowedClses = slot.getAllowedClses();
            if (allowedClses != null && allowedClses.size() > 0) {
                typeName = ((Frame) CollectionUtilities.getFirstItem(allowedClses)).getName();
            }
        }

        EntityData valueData = null;

        synchronized (kb) {
            KBUtil.morphUser(kb, user);

            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                valueData = createInstance(projectName, instName, typeName, user, null);
                if (valueData == null) {
                    if (runsInTransaction) {
                        kb.commitTransaction();
                    }
                    return null;
                }
                KBUtil.morphUser(kb, user); //hack
                PropertyEntityData propEntityData = createPropertyEntityData(slot, null, false);
                addPropertyValue(projectName, subjectEntity, propEntityData, valueData, user, null);
                KBUtil.morphUser(kb, user); //hack
                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(
                        Level.SEVERE,
                        "Could not create instance  " + instName + " of type " + typeName + " for " + subjectEntity
                        + " " + propertyEntity, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Could not create instance  " + instName + " of type " + typeName
                        + " for " + subjectEntity + " " + propertyEntity + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }

        return valueData;
    }


    public EntityData createInstanceValueWithPropertyValue(String projectName, String instName, String typeName, String subjectEntity,
            String propertyEntity, PropertyEntityData instancePropertyEntity, EntityData valueEntityData, String user, String operationDescription) {
        Project project = getProject(projectName);

        if (project == null) {
            return null;
        }
        KnowledgeBase kb = project.getKnowledgeBase();
        if (kb == null) {
            return null;
        }

        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            // original operation description goes in our top-level change ....
            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }
                // setting the operationDescription to null will ensure that we have no nested transactions (which are unnecessary) or duplicate changes...
                EntityData valueData = createInstanceValue(projectName, instName, typeName,
                        subjectEntity, propertyEntity, user, null);
                addPropertyValue(projectName, valueData.getName(), instancePropertyEntity,
                        valueEntityData, user, null);
                KBUtil.morphUser(kb, user);
                if (runsInTransaction){
                    kb.commitTransaction();
                }
                return valueData;
            } catch (RuntimeException e) {
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                // no logging as we are rethrowing an exception already logged by our called methods.
                throw e;
            } finally {
                KBUtil.restoreUser(kb);
            }
        }

    }

    public EntityData renameEntity(String projectName, String oldName, String newName, String user,
            String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        if (isOWLOntology(project)) {
            OWLModel owlModel = (OWLModel) kb;
            oldName = OWLUtil.getInternalFullName(owlModel, oldName);
            newName = OWLUtil.getInternalFullName(owlModel, newName);
        }

        Frame oldFrame = kb.getFrame(oldName);
        Frame newFrame = kb.getFrame(newName);

        if (oldFrame == null || newFrame != null) {
            throw new IllegalArgumentException("Could not rename " + oldName + " to " + newName + ". Null name.");
        }

        synchronized (kb) {
            KBUtil.morphUser(kb, user);

            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                newFrame = oldFrame.rename(newName);

                if (runsInTransaction) {
                    kb.commitTransaction();
                }
            } catch (Exception e) {
                Log.getLogger().log(Level.SEVERE,
                        "Could not rename in " + projectName + " entity old name:" + oldName + " new name: " + newName,
                        e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Could not rename entity, old name: " + oldName + " to new name: " + newName
                        + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
        return createEntityData(newFrame);
    }

    public String getRestrictionHtml(String projectName, String className) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        if (!(kb instanceof OWLModel)) {
            return "";
        }
        OWLModel owlModel = (OWLModel) kb;
        OWLNamedClass cls = owlModel.getOWLNamedClass(className);
        if (cls == null) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append(getConditionsHtml(cls));
        return buffer.toString();
    }

    private StringBuffer getConditionsHtml(OWLNamedClass cls) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<table width=\"100%\" border=\"0\" cellspacing=\"3\"  class=\"restriction_table\">");

        ConditionsTableModel ctm = new ConditionsTableModel(cls.getOWLModel());
        ctm.setCls(cls);
        ctm.refresh();

        for (int i = 0; i < ctm.getRowCount(); i++) {
            String row = (String) ctm.getValueAt(i, 0);
            RDFSClass restr = ctm.getClass(i);
            buffer.append("<tr><td>");
            if (restr != null) {
                buffer.append(getConditionHtmlString(restr));
            } else {
                if (row.equals(ConditionsTableItem.NECESSARY)) {
                    buffer.append("<hr>");
                    buffer.append("<div class=\"restiction_title\">Superclasses (Necessary conditions)</div>");
                } else if (row.equals(ConditionsTableItem.SUFFICIENT)) {
                    buffer.append("<div class=\"restiction_title\">Equivalent classes (Necessary and Sufficient conditions)</div>");
                } else if (row.equals(ConditionsTableItem.INHERITED)) {
                    buffer.append("<hr>");
                    buffer.append("<div class=\"restiction_title\">Inherited</div>");
                } else {
                    buffer.append(row.toString());
                }
            }

            buffer.append("</td></tr>");
        }
        buffer.append("</table>");
        return buffer;
    }

    private static final String delimsStrs[] = {"and", "or", "not", "some", "only", "has", "min", "exactly", "max"};
    private static List<String> delims = Arrays.asList(delimsStrs);

    private String getConditionHtmlString(RDFSClass cls) {
        //Could also use: string.replaceAll("(^|\w)or(\w)", "$1or$2)
        StringBuffer buffer = new StringBuffer();
        StringTokenizer st = new StringTokenizer(cls.getBrowserText(), " \t\n\r\f", true);
        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            if (delims.contains(token)) {
                buffer.append("<span class=\"restriction_delim\">");
                buffer.append(token);
                buffer.append("</span>");
            } else {
                buffer.append(token);
            }
        }

        return buffer.toString();
    }

    class RestrictionComparator implements Comparator<RDFSClass> {
        public int compare(RDFSClass cls1, RDFSClass cls2) {
            if (cls1 instanceof RDFSNamedClass && cls2 instanceof RDFSNamedClass) {
                return cls1.getBrowserText().compareTo(cls2.getBrowserText());
            }
            if (cls1 instanceof RDFSNamedClass && !(cls2 instanceof RDFSNamedClass)) {
                return -1;
            }
            if (!(cls1 instanceof RDFSNamedClass) && cls2 instanceof RDFSNamedClass) {
                return 1;
            }
            // for all other cases
            return cls1.getBrowserText().compareTo(cls2.getBrowserText());
        }
    }

    public List<ConditionItem> getClassConditions(String projectName, String className) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        if (!(kb instanceof OWLModel)) {
            return null;
        }
        OWLModel owlModel = (OWLModel) kb;
        OWLNamedClass cls = owlModel.getOWLNamedClass(className);
        if (cls == null) {
            return null;
        }

        List<ConditionItem> conditions = new ArrayList<ConditionItem>();

        ConditionsTableModel ctm = new ConditionsTableModel(cls.getOWLModel());
        ctm.setCls(cls);
        ctm.refresh();

        for (int i = 0; i < ctm.getRowCount(); i++) {
            String row = (String) ctm.getValueAt(i, 0);
            RDFSClass conditionClass = ctm.getClass(i);
            ConditionItem condItem = new ConditionItem();
            condItem.setIndex(i);
            if (conditionClass != null) {
                condItem.setName(conditionClass.getName());
                condItem.setBrowserText(getConditionHtmlString(conditionClass));
                OWLNamedClass originClass = ctm.getOriginClass(i);
                if (originClass != null) {
                    condItem.setInheritedFromName(originClass.getName());
                    condItem.setInheritedFromBrowserText(originClass.getBrowserText());
                    condItem.setBrowserText(condItem.getBrowserText() + " <span class=\"restriction_separator\"> [from " + originClass.getBrowserText() + "] </span>");
                }
            } else {
                condItem.setName(row);
                condItem.setBrowserText(getSeparatorHtml(row));
            }
            conditions.add(condItem);
        }

        return conditions;
    }

    public List<ConditionItem> deleteCondition(String projectName, String className, ConditionItem conditionItem,  int row,
            String operationDescription) { //operation description is ignored right now - the ctm has a good string
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        if (!(kb instanceof OWLModel)) {
            return null;
        }
        OWLModel owlModel = (OWLModel) kb;
        OWLNamedClass cls = owlModel.getOWLNamedClass(className);
        if (cls == null) {
            return null;
        }

        synchronized (kb) {
            String user = KBUtil.getUserInSession(getThreadLocalRequest());
            KBUtil.morphUser(kb, user);
            try {
                ConditionsTableModel ctm = new ConditionsTableModel(cls.getOWLModel());
                ctm.setCls(cls);
                ctm.refresh();

                //do the transaction and opdescription

                //validity checks
                RDFSClass conditionToDelete = ctm.getClass(row);
                RDFResource conditionFromClient = owlModel.getRDFResource(conditionItem.getName());
                if (conditionToDelete == null || conditionFromClient == null || (!conditionToDelete.equals(conditionFromClient))) {
                    throw new IllegalArgumentException("Cannot delete condition from class " + cls.getBrowserText() +". Condition is not present at class.");
                }

                ctm.deleteRow(row); //treat exceptions, transactions, etc?
            } catch (Exception e) {
                Log.getLogger().log(Level.WARNING, "Error at deleting condition " + conditionItem.getName() + " from class " + className, e);
                throw new RuntimeException("Error at deleting condition " + conditionItem.getName() + " from class " + className);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
        return getClassConditions(projectName, className);
    }

    public ConditionSuggestion getConditionAutocompleteSuggestions(String projectName, String condition, int cursorPosition) {
        Project project = getProject(projectName);
        if (!(project.getKnowledgeBase() instanceof OWLModel)) {
            return null;
        }
        OWLModel owlModel = (OWLModel) project.getKnowledgeBase();
        ConditionSuggestion conditionSuggestion = new ConditionSuggestion();

        String leftString = condition.substring(0, cursorPosition);
        int i = ParserUtils.findSplittingPoint(leftString);
        String prefix = leftString.substring(i, leftString.length());

        //validation is on full expression, autocomplete is only for last word
        synchronized (this) { //sync on this... stupid statics..
            OWLClassParser parser = owlModel.getOWLClassDisplay().getParser();
            try {
                parser.checkClass(owlModel, condition);
                conditionSuggestion.setValid(true);
            } catch (OWLClassParseException e) {
                conditionSuggestion.setMessage(e.getMessage());
                conditionSuggestion.setValid(false);
            }

            //used for the resource name matcher - stupid statics
            try {
                parser.checkClass(owlModel, leftString);
            } catch (OWLClassParseException e) { // intentionally left blank
            }

            if (prefix == null || prefix.length() == 0) {
                return conditionSuggestion;
            }

            OWLResourceNameMatcher resourceMatcher = new OWLResourceNameMatcher();
            Set<RDFResource> matches = resourceMatcher.getMatchingResources(prefix, null, owlModel);
            ArrayList<RDFResource> resourceList = new ArrayList<RDFResource>(matches);

            //need the browser text with quotes
            ArrayList<EntityData> dataList = new ArrayList<EntityData>();
            for (RDFResource res : resourceList) {
                dataList.add(new EntityData(res.getName(), res.getBrowserText()));
            }

            //conditionSuggestion.setSuggestions(createEntityList(resourceList)); //this method removes the quotes from the browser text
            conditionSuggestion.setSuggestions(createEntityList(dataList));
            return conditionSuggestion;
        }
    }

    public List<ConditionItem> replaceCondition(String projectName, String className, ConditionItem conditionItem,
            int row, String newCondition, String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        if (!(kb instanceof OWLModel)) {
            return null;
        }
        OWLModel owlModel = (OWLModel) kb;
        OWLNamedClass cls = owlModel.getOWLNamedClass(className);
        if (cls == null) {
            return null;
        }

        synchronized (kb) {
            String user = KBUtil.getUserInSession(getThreadLocalRequest());
            KBUtil.morphUser(kb, user);
            try {
                ConditionsTableModel ctm = new ConditionsTableModel(cls.getOWLModel());
                ctm.setCls(cls);
                ctm.refresh();

                //do the transaction and opdescription
                //validity checks
                RDFSClass conditionToDelete = ctm.getClass(row);
                RDFResource conditionFromClient = owlModel.getRDFResource(conditionItem.getName());
                if (conditionToDelete == null || conditionFromClient == null || (!conditionToDelete.equals(conditionFromClient))) {
                    throw new IllegalArgumentException("Cannot replace condition from class " + cls.getBrowserText() +". Condition is not present at class.");
                }
                ctm.setValueAt(row, owlModel, newCondition);
            } catch (Exception e) {
                Log.getLogger().log(Level.WARNING, "Error at parsing class expression: " + newCondition + " for class " + className, e);
                throw new RuntimeException("Could not parse expression " + newCondition);
            }  finally { //treat exceptions, transactions, etc?
                KBUtil.restoreUser(kb);
            }
        }

        return getClassConditions(projectName, className);
    }

    public List<ConditionItem> addCondition(String projectName, String className, int row, String newCondition,
            boolean isNS, String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        if (!(kb instanceof OWLModel)) {
            return null;
        }
        OWLModel owlModel = (OWLModel) kb;
        OWLNamedClass cls = owlModel.getOWLNamedClass(className);
        if (cls == null) {
            return null;
        }

        synchronized (kb) {
            String user = KBUtil.getUserInSession(getThreadLocalRequest());
            KBUtil.morphUser(kb, user);
            try {
                ConditionsTableModel ctm = new ConditionsTableModel(cls.getOWLModel());
                ctm.setCls(cls);
                ctm.refresh();

                //do the transaction and opdescription
                //validity checks
                if (ctm.getType(row) != (isNS ? ConditionsTableItem.TYPE_DEFINITION_BASE : ConditionsTableItem.TYPE_SUPERCLASS)) {
                    throw new IllegalArgumentException("Cannot add new condition to class " + cls.getBrowserText() +". Class might have changed.");
                }

                ctm.addEmptyRow(row);
                ctm.setValueAt(row+1, owlModel, newCondition);
            } catch (Exception e) {
                Log.getLogger().log(Level.WARNING, "Error at adding new condition: " + newCondition + " for class " + className, e);
                throw new RuntimeException( "Error at adding new condition: " + newCondition + " for class " + className);
            } finally {  //treat exceptions, transactions, etc?
                KBUtil.restoreUser(kb);
            }
        }

        return getClassConditions(projectName, className);
    }


    private String getSeparatorHtml(String separator) {
        separator = separator.replaceAll(" ", "&nbsp;");
        return "<table width=\"100%\" class=\"restriction_separator\"><tr><td><hr color=\"#E8E8E8\" /></td><td width=\"1px\">" + separator + "</td><td><hr color=\"#E8E8E8\" /></td></tr></table>";
    }

    public Boolean hasWritePermission(String projectName, String userName) {
        Project project = getProject(projectName);
        if (project == null) {
            return Boolean.FALSE;
        }
        if (project instanceof RemoteClientProject) {// is remote project
            RemoteClientProject remoteClientProject = (RemoteClientProject) project;
            RemoteServer server = remoteClientProject.getServer();
            boolean allowed = false;
            try {
                // using a bogus session with the correct user name
                allowed = server.isOperationAllowed(new Session(userName, "(from web protege)", false),
                        MetaProjectConstants.OPERATION_WRITE, projectName);
            } catch (Exception e) {
                Log.getLogger().log(Level.WARNING, "Error at remote call: isOperationAllowed for " + projectName, e);
            }
            return allowed;
        }
        // TODO: in standalone it always returns true - make it work with the
        // metaproject
        return true;
    }

    public PaginationData<EntityData> search(String projectName, String searchString, edu.stanford.bmir.protege.web.client.rpc.data.ValueType valueType, int start, int limit, String sort, String dir) {
        List<EntityData> records = search(projectName, searchString, valueType);
        return PaginationServerUtil.pagedRecords(records, start, limit, sort, dir);
    }


    public List<EntityData> search(String projectName, String searchString) {
        return search(projectName, searchString, null);
    }

    public List<EntityData> search(String projectName, String searchString, edu.stanford.bmir.protege.web.client.rpc.data.ValueType valueType) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        if ((!searchString.startsWith("*"))
                && (!searchString.endsWith("*"))
                && searchString.length() >= MIN_SEARCH_STRING_LENGTH) {
            if (!searchString.startsWith("*")) {
                searchString = "*" + searchString;
            }

            if (!searchString.endsWith("*")) {
                searchString = searchString + "*";
            }
        }

        Collection<Frame> matchedFrames = new ArrayList<Frame>();;

        QueryConfiguration qConf = new QueryApi(kb).install();

        if (qConf == null) {
            //Classic Protege search
            matchedFrames = new HashSet<Frame>(kb.getMatchingFrames(kb.getSystemFrames().getNameSlot(), null,
                    false, searchString, -1));
            if (isOWLOntology(project)) {
                matchedFrames.addAll(kb.getMatchingFrames(((OWLSystemFrames) kb.getSystemFrames()).getRdfsLabelProperty(), null,
                        false, searchString, -1));
            }
        }
        else {
            //search only class value type. The abstract indexer knows about this
            Map<String, String> browerTextToFrameNameMap = IndexUtilities.getBrowserTextToFrameNameMap(kb, searchString);
            List<EntityData> searchResults = new ArrayList<EntityData>();
            for (String browserText : browerTextToFrameNameMap.keySet()) {
                searchResults.add(new EntityData(browerTextToFrameNameMap.get(browserText), browserText));
            }

            return searchResults;
        }

        //Log.getLogger().info("Search string: " + searchString + "  Search results count: " + (matchedFrames == null ? "0" : matchedFrames.size()));

        //filter & sort frames
        ArrayList<Frame> sortedFrames = new ArrayList<Frame>();
        switch (valueType) {
        case Cls:
            for (Frame frame : matchedFrames) {
                if (frame instanceof Cls && !frame.isSystem() && !(frame instanceof OWLAnonymousClass)) {
                    sortedFrames.add(frame);
                }
            }
            break;
        case Instance:
            for (Frame frame : matchedFrames) {
                if (frame instanceof Instance && !frame.isSystem() && !(frame instanceof OWLAnonymousClass)) {
                    sortedFrames.add(frame);
                }
            }
            break;
        case Property:
            for (Frame frame : matchedFrames) {
                if (frame instanceof Slot && !frame.isSystem() && !(frame instanceof OWLAnonymousClass)) {
                    sortedFrames.add(frame);
                }
            }
            break;
        default:    //case valueType == null or not one of the above
        for (Frame frame : matchedFrames) {
            if (!frame.isSystem() && !(frame instanceof OWLAnonymousClass)) {
                sortedFrames.add(frame);
            }
        }
        break;
        }

        // Collections.sort(sortedFrames, new FrameComparator());

        ArrayList<EntityData> results = new ArrayList<EntityData>();
        for (Frame frame : sortedFrames) {
            results.add(createEntityData(frame));
        }

        return results;
    }

    public ArrayList<EntityData> getPathToRoot(String projectName, String entityName) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        ArrayList<EntityData> path = new ArrayList<EntityData>();

        Frame frame = kb.getFrame(entityName);
        if (frame == null || !(frame instanceof Cls)) {
            return path;
        }

        // for now it works only with classes
        Cls entity = kb.getCls(entityName);
        List clsPath = ModelUtilities.getPathToRoot(entity);

        for (Iterator iterator = clsPath.iterator(); iterator.hasNext();) {
            Cls cls = (Cls) iterator.next();
            path.add(createEntityData(cls, false));
        }

        return path;
    }

    // private static String PREFERRED_NAME_PROP = "icdTitle";

    // TODO: search methods to be refactored in another class/interface

    public String getBioPortalSearchContent(String projectName, String entityName, BioPortalSearchData bpSearchData) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();
        Frame frame = kb.getFrame(entityName);
        if (frame == null) {
            return URLUtil.getURLContent(getBioPortalSearchUrl(entityName, bpSearchData));
        }
        // try to search the preferred name, if exists
        /*if (kb instanceof OWLModel) {
            RDFProperty preferredNameProp = ((OWLModel) kb).getRDFProperty(PREFERRED_NAME_PROP);
            if (preferredNameProp != null) {
                try {
                    String preferredName = (String) frame.getOwnSlotValue(preferredNameProp);
                    String prefNameSearch = URLUtil.getURLContent(getBioPortalSearchUrl(preferredName, bpSearchData));
                    if (prefNameSearch != null && prefNameSearch.indexOf("searchBean") > 0) {
                        return prefNameSearch;
                    }
                } catch (Exception e) {
                }
            }
        }
        // try to search the browser text
        String nameSearch = URLUtil.getURLContent(getBioPortalSearchUrl(getBrowserText(frame), bpSearchData));
        if (nameSearch != null && nameSearch.indexOf("searchBean") > 0) {
            return nameSearch;
        }

        // search first rdfs:label if exist..
        if (frame instanceof RDFResource) {
            RDFProperty rdfsLabelProp = ((OWLModel) kb).getSystemFrames().getRdfsLabelProperty();
            Object rdfsLabelO = kb.getOwnSlotValue(frame, rdfsLabelProp);
            if (rdfsLabelO != null) {
                String rdfsLabelString = "";
                rdfsLabelString = rdfsLabelO instanceof RDFSLiteral ? ((RDFSLiteral) rdfsLabelO).getString()
                        : rdfsLabelO.toString();
                return URLUtil.getURLContent(getBioPortalSearchUrl(rdfsLabelString, bpSearchData));
            }
        }
         */
        return "";
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
                String text = HTMLUtil.replaceEOF(ProtegeUtil.getDisplayText(value));
                if (text.startsWith("[")) {
                    text = text.substring(1, text.length() - 1);
                }
                if (text.length() > 0) {
                    String color = i % 2 == 0 ? evenColor : oddColor;
                    buffer.append("<tr>");
                    buffer.append("<td class=\"servBodL\" style=\"background-color:" + color + ";padding:7px;font-weight: bold;\" >");
                    buffer.append(ProtegeUtil.getDisplayText(obj));
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

    public EntityData createExternalReference(String projectName, String entityName, BioPortalReferenceData bpRefData,
            String user, String operationDescription) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        Instance instance = kb instanceof OWLModel ?
                ((OWLModel) kb).getRDFResource(entityName) :
                    kb.getInstance(entityName);

                if (instance == null) {
                    throw new RuntimeException("Failed to import reference. Entity does not exist: " + entityName);
                }

                String referenceProperty = bpRefData.getReferencePropertyName();

                Slot slot = kb instanceof OWLModel ? ((OWLModel) kb).getRDFProperty(referenceProperty) : kb
                        .getSlot(referenceProperty);

                if (slot == null) {
                    throw new RuntimeException("Could not create reference for " + entityName
                            + " because the reference property is not part of the ontology. Property name: "
                            + referenceProperty);
                }

                ReferenceModel referenceModel = null;
                Instance refInstance = null;
                synchronized (kb) {
                    KBUtil.morphUser(kb, user);

                    boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
                    try {
                        if (runsInTransaction) {
                            kb.beginTransaction(operationDescription);
                        }

                        String viewOnOntologyVersionId = getIsViewOnOntologyId(
                                bpRefData.getBpRestBaseUrl(), bpRefData.getOntologyVersionId(), bpRefData.getBpRestCallSuffix());
                        String viewOnOntologyWithLabel =
                            (viewOnOntologyVersionId == null ? null : getIsViewOnOntologyWithLabel(
                                    bpRefData.getBpRestBaseUrl(), bpRefData.getOntologyVersionId(), bpRefData.getBpRestCallSuffix()));

                        referenceModel = new ReferenceModel(kb, bpRefData.importFromOriginalOntology(), bpRefData.createAsClass(),
                                bpRefData.getReferenceClassName(), bpRefData.getUrlPropertyName(),
                                bpRefData.getOntologyNamePropertyName(), bpRefData.getOntologyNameAltPropertyName(),
                                bpRefData.getOntologyIdPropertyName(), bpRefData.getConceptIdPropertyName(),
                                bpRefData.getConceptIdAltPropertyName(), bpRefData.getPreferredLabelPropertyName());

                        referenceModel.setReference_cls(bpRefData.getReferenceClassName());
                        refInstance = referenceModel.createReference(
                                bpRefData.getBpUrl(), bpRefData.getConceptId(), bpRefData.getConceptIdShort(), bpRefData.getOntologyVersionId(),
                                viewOnOntologyVersionId, bpRefData.getPreferredName(), bpRefData.getOntologyName(),
                                viewOnOntologyWithLabel);
                        if (refInstance == null) {
                            Log.getLogger().log(Level.SEVERE,
                                    "Could not create reference in " + projectName + " for entity " + entityName);

                            if (runsInTransaction) {
                                kb.commitTransaction();
                            }
                            throw new RuntimeException("Could not create reference for entity " + entityName);
                        }
                        instance.addOwnSlotValue(slot, refInstance);

                        if (runsInTransaction) {
                            kb.commitTransaction();
                        }
                    } catch (Exception e) {
                        Log.getLogger().log(Level.SEVERE,
                                "Could not import reference in " + projectName + " for entity " + entityName, e);
                        if (runsInTransaction) {
                            kb.rollbackTransaction();
                        }
                        throw new RuntimeException("Could not import reference for entity " + entityName + ". Message: "
                                + e.getMessage(), e);
                    } finally {
                        KBUtil.restoreUser(kb);
                    }
                }
                return createEntityData(refInstance);
    }

    public EntityData replaceExternalReference(String projectName, String entityName, BioPortalReferenceData bpRefData,
            EntityData oldValueEntityData, String user, String operationDescription) {
        Project project = getProject(projectName);
        if (project == null) {
            return null;
        }

        KnowledgeBase kb = project.getKnowledgeBase();

        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                PropertyEntityData oldPropertyEntity = new PropertyEntityData();
                oldPropertyEntity.setName(bpRefData.getReferencePropertyName());
                oldValueEntityData.setValueType(edu.stanford.bmir.protege.web.client.rpc.data.ValueType.Instance); // these are always an instance type, so we can do this.
                removePropertyValue(projectName, entityName, oldPropertyEntity, oldValueEntityData, user, operationDescription);
                KBUtil.morphUser(kb, user); //hack
                final EntityData data = createExternalReference(projectName, entityName, bpRefData, user, operationDescription);
                KBUtil.morphUser(kb, user); //hack

                if (runsInTransaction) {
                    kb.commitTransaction();
                }

                return data;
            } catch (Exception e) {
                Log.getLogger().log(Level.SEVERE,  "Could not replace reference in " + projectName + " for entity " + entityName, e);
                if (runsInTransaction) {
                    kb.rollbackTransaction();
                }
                throw new RuntimeException("Could not replace reference for entity " + entityName + ". Message: " + e.getMessage(), e);
            } finally {
                KBUtil.restoreUser(kb);
            }
        }
    }


    private String getIsViewOnOntologyId(String bpRestBaseUrl, String ontologyVersionId, String bpRestCallSuffix) {
        String viewOnOntologyVersionId = null;
        if (bpRestBaseUrl != null && ontologyVersionId != null) {
            BioPortalViewOntologyMap bpViewOntologyMap = getBPViewOntologyMap(bpRestBaseUrl);
            int bpOntologyVersionId = Integer.parseInt(ontologyVersionId);
            int bpViewOnOntologyId = bpViewOntologyMap.getViewOnOntologyId(bpOntologyVersionId);
            if (bpViewOnOntologyId == BioPortalViewOntologyMap.UNKNOWN) {
                //calculate bpViewOnOntologyId
                bpViewOnOntologyId = org.ncbo.stanford.util.BioPortalUtil.getViewOnOntologyId(bpRestBaseUrl, bpOntologyVersionId, bpRestCallSuffix);
                //if calculation was successfully
                if (bpViewOnOntologyId != BioPortalViewOntologyMap.UNKNOWN) {
                    OntologyBean bpViewOnOntology = (bpViewOnOntologyId == BioPortalViewOntologyMap.NOT_A_VIEW ? null :
                        org.ncbo.stanford.util.BioPortalUtil.getViewOnOntology(bpRestBaseUrl, bpViewOnOntologyId, bpRestCallSuffix));
                    bpViewOntologyMap.setViewOnOntologyId(bpOntologyVersionId, bpViewOnOntologyId, bpViewOnOntology);
                }
            }
            if (bpViewOnOntologyId > 0) {
                viewOnOntologyVersionId = new Integer(bpViewOnOntologyId).toString();
            }
        }
        return viewOnOntologyVersionId;
    }

    private String getIsViewOnOntologyWithLabel(String bpRestBaseUrl, String ontologyVersionId, String bpRestCallSuffix) {
        String viewOnOntologyWithLabel = null;
        if (bpRestBaseUrl != null && ontologyVersionId != null) {
            BioPortalViewOntologyMap bpViewOntologyMap = getBPViewOntologyMap(bpRestBaseUrl);
            int bpOntologyVersionId = Integer.parseInt(ontologyVersionId);
            viewOnOntologyWithLabel = bpViewOntologyMap.getViewOnOntologyDisplayLabel(bpOntologyVersionId);
        }
        return viewOnOntologyWithLabel;
    }

    private BioPortalViewOntologyMap getBPViewOntologyMap(String bpRestBaseURL) {
        BioPortalViewOntologyMap bpViewOntologyMap = bpViewOntologyMaps.get(bpRestBaseURL);
        if (bpViewOntologyMap == null) {
            bpViewOntologyMap = new BioPortalViewOntologyMap();
            bpViewOntologyMaps.put(bpRestBaseURL, bpViewOntologyMap);
        }
        return bpViewOntologyMap;
    }

}
