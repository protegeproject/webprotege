package edu.stanford.bmir.protege.web.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import edu.stanford.bmir.icd.claml.ICDContentModel;
import edu.stanford.bmir.icd.claml.ICDContentModelConstants;
import edu.stanford.bmir.protege.icd.export.ExportICDClassesJob;
import edu.stanford.bmir.protege.web.client.rpc.ICDService;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityPropertyValues;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;
import edu.stanford.bmir.protege.web.client.ui.icd.DisplayStatus;
import edu.stanford.smi.protege.collab.util.HasAnnotationCache;
import edu.stanford.smi.protege.exception.ProtegeException;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.ui.FrameComparator;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;

public class ICDServiceImpl extends OntologyServiceImpl implements ICDService {

    private static final long serialVersionUID = -10148579388542388L;
    private static final String EXCEL_FILE_EXTENSION = ".xls";

    private static final String PROPERTY_LINEARIZATION_PARENT = "http://who.int/icd#linearizationParent";

    @SuppressWarnings("rawtypes")
    public EntityData createICDCls(final String projectName, String clsName, Collection<String> superClsNames,
            String title, String sortingLabel, boolean createICDSpecificEntities, final String user, final String operationDescription,
            final String reasonForChange) {
        Project project = getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();

        ICDContentModel cm = new ICDContentModel((OWLModel) kb);

        RDFSNamedClass cls = null;

        if (clsName != null && ((OWLModel) kb).getRDFSNamedClass(clsName) != null) {
            throw new RuntimeException("A class with the same name '" + clsName + "' already exists in the model.");
        }

        boolean runsInTransaction = KBUtil.shouldRunInTransaction(operationDescription);
        synchronized (kb) {
            KBUtil.morphUser(kb, user);
            try {
                if (runsInTransaction) {
                    kb.beginTransaction(operationDescription);
                }

                cls = cm.createICDCategory(clsName, superClsNames, true, createICDSpecificEntities);

                if (clsName != null) {
                    cls.addPropertyValue(cm.getIcdCodeProperty(), clsName);
                }

                RDFResource titleTerm = cm.createTitleTerm();
                cm.fillTerm(titleTerm, null, title, null);
                cm.addTitleTermToClass(cls, titleTerm);

                if (sortingLabel != null) {
                    cls.setPropertyValue(cm.getSortingLabelProperty(), sortingLabel);
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

        //TODO: the note is not created here anymore, but with a different remote call from the client
        //so, theoretically, we do no need the reason for change here, but it doesn't hurt for now
        EntityData entityData = createEntityData(cls, false);
        if (reasonForChange != null && reasonForChange.length() > 0) { //this should be handled already
            entityData.setLocalAnnotationsCount(1);
        }

        entityData.setTypes(createEntityList((List) cls.getDirectTypes()));

        return entityData;
    }

    public List<EntityPropertyValues> getEntityPropertyValuesForLinearization(String projectName, List<String> entities, List<String> properties,
            List<String> reifiedProps) {
        List<EntityPropertyValues> entityPropertyValues = getEntityPropertyValues(projectName, entities, properties, reifiedProps);
        if (entityPropertyValues == null){
            entityPropertyValues = new ArrayList<EntityPropertyValues>();
        }
        ArrayList<EntityPropertyValues> result = new ArrayList<EntityPropertyValues>(entityPropertyValues);

        //update display label of linearization parent to default parent, if it makes sense
        if (entities != null && entities.size() == 1) {
            List<EntityData> directParents = getParents(projectName, entities.get(0), true);
            if (directParents.size() == 1) {
                String parentBrowserText = directParents.get(0).getBrowserText();
                PropertyEntityData propLinParentED = new PropertyEntityData(PROPERTY_LINEARIZATION_PARENT);
                for (EntityPropertyValues entityPV : result) {
                    List<EntityData> values = entityPV.getPropertyValues(propLinParentED);
                    if (values == null ) {
                        values = new ArrayList<EntityData>();
                    }
                    if ( values.isEmpty() ) {
                        values.add(new EntityData());
                    }
                    String linParentName = values.get(0).getName();
                    if (linParentName == null || "".equals(linParentName)) {
                        ArrayList<EntityData> newValues = new ArrayList<EntityData>();
                        for (EntityData value : values) {
                            value.setName(null);
                            value.setBrowserText("[" + parentBrowserText + "]");
                            newValues.add(value);
                        }
                        entityPV.setPropertyValues(propLinParentED, newValues);
                    }
                }
            }
        }
        Collections.sort(result, new LinearizationEPVComparator());
        return result;
    }

    public String exportICDBranch(String projectName, String parentClass, String userName){
        Project project = getProject(projectName);
        if (project == null) {
            return null;
        }
        KnowledgeBase kb = project.getKnowledgeBase();

        Cls cls = kb.getCls(parentClass);
        if (cls == null) {
            return null;
        }

        String fileName = getExportFileName(projectName, cls.getBrowserText().replaceAll("'",""), userName);
        String exportDirectory = ApplicationProperties.getICDExportDirectory();
        final String exportFilePath = exportDirectory + fileName;

        Log.getLogger().info("Started the export of " + cls.getBrowserText() + " for user " + userName + " on " + new Date());
        long t0 = System.currentTimeMillis();

        ExportICDClassesJob exportJob = new ExportICDClassesJob(getProject(projectName).getKnowledgeBase(), exportFilePath, parentClass);
        try {
            exportJob.execute();
        } catch (ProtegeException e) {
            Log.getLogger().log(Level.SEVERE, "Error at exporting " + cls.getBrowserText() + " to file " + fileName, e);
            return null;
        }

        Log.getLogger().info("Export of " + cls.getBrowserText() + " for user " + userName + " took " + (System.currentTimeMillis() - t0)/1000 + " seconds.");

        return fileName;
    }

    private String getExportFileName(String projectName, String entity, String user) {
        StringBuffer fileName = new StringBuffer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HHmmss");
        final String formattedDate = sdf.format(new Date());

        fileName.append(formattedDate);
        fileName.append("_");
        fileName.append(entity);
        fileName.append("_");
        if (user != null) {
            fileName.append(user);
        }
        fileName.append(EXCEL_FILE_EXTENSION);

        return fileName.toString();
    }

    private class LinearizationEPVComparator implements Comparator<EntityPropertyValues> {

        private final PropertyEntityData linearizationViewPED =
            new PropertyEntityData("http://who.int/icd#linearizationView");

        public int compare(EntityPropertyValues epv1, EntityPropertyValues epv2) {
            //We should not have null values, but if we happen to have we wish to leave the them at the end
            if (epv1 == null) {
                return 1;
            }
            if (epv2 == null) {
                return -1;
            }
            EntityData lin1 = null;
            EntityData lin2 = null;
            List<EntityData> lins;
            lins = epv1.getPropertyValues(linearizationViewPED);
            if (lins != null && lins.size() > 0) {
                lin1 = lins.get(0);
            }
            lins = epv2.getPropertyValues(linearizationViewPED);
            if (lins != null && lins.size() > 0) {
                lin2 = lins.get(0);
            }
            //We should not have null values, but if we happen to have we wish to leave the them at the end
            if (lin1 == null) {
                return 1;
            }
            if (lin2 == null) {
                return -1;
            }

            return lin1.getBrowserText().compareTo(lin2.getBrowserText());
        }
    }

    //have to correspond to the ones in InheritedTagsGrid
    private final static String COL_TAG = "tag";
    private final static String COL_INH_FOM = "inheritedFrom";

    public List<EntityPropertyValues> getSecondaryAndInheritedTags(String projectName, String clsName) {
        Project project = getProject(projectName);
        if (project == null) { return null; }

        OWLModel owlModel = (OWLModel) project.getKnowledgeBase();
        RDFSNamedClass cls = owlModel.getRDFSNamedClass(clsName);
        if (cls == null) { return null; }

        //set browserText for values that come from primary

        List<EntityPropertyValues> inhTagsList = new ArrayList<EntityPropertyValues>();

        ICDContentModel cm = new ICDContentModel(owlModel);
        Map<RDFResource, List<RDFSNamedClass>> tag2inhFrom = cm.getInvolvedTags(cls);

        RDFResource localPrimaryTag = cm.getAssignedPrimaryTag(cls);
        Collection<RDFResource> localSecondaryTags = cm.getAssignedSecondaryTags(cls);

        PropertyEntityData tagEdProp = new PropertyEntityData(COL_TAG);
        PropertyEntityData inhFromEdProp = new PropertyEntityData(COL_INH_FOM);

        for (Iterator<RDFResource> iteratorTags = tag2inhFrom.keySet().iterator(); iteratorTags.hasNext();) {
            RDFResource tagInst = iteratorTags.next();

            EntityData tag = createEntityData(tagInst);
            List<RDFSNamedClass> inhFromClses = tag2inhFrom.get(tagInst);

            EntityPropertyValues epv = new EntityPropertyValues();
            epv.setSubject(tag);

            EntityPropertyValues suplEpv = null;

            epv.addPropertyValue(tagEdProp, tag);
            List<RDFSNamedClass> realInheritedClasses = new ArrayList<RDFSNamedClass>();
            realInheritedClasses.addAll(inhFromClses);
            //remove local primary TAG
            if (tagInst.equals(localPrimaryTag)) {
                realInheritedClasses.remove(cls);
            }

            //split value list, if it contains local secondary TAG
            if (localSecondaryTags !=null && localSecondaryTags.contains(tagInst)) {
                realInheritedClasses.remove(cls);       //TODO see if we need to remove multiple appearances of cls in case a tag is added as secondary tag multiple times

                suplEpv = new EntityPropertyValues();
                suplEpv.setSubject(tag); //not ideal - we need a subject for the values, ideally should be null
                suplEpv.addPropertyValue(tagEdProp, tag);
                suplEpv.addPropertyValues(inhFromEdProp, new ArrayList<EntityData>());
                inhTagsList.add(suplEpv);
            }

            //create value list for inherited classes; add "(P)" if inherited from primary TAG of superclass
            if ( ! realInheritedClasses.isEmpty() ) {
                List<EntityData> inheritedClsesEdList = new ArrayList<EntityData>();
                for (RDFSNamedClass inhCls : realInheritedClasses) {
                    EntityData ed = createEntityData(inhCls);
                    if (tagInst.equals(cm.getAssignedPrimaryTag(inhCls))) {
                        ed.setBrowserText(ed.getBrowserText() + " (P)");
                    }
                    inheritedClsesEdList.add(ed);
                }
                epv.addPropertyValues(inhFromEdProp, inheritedClsesEdList);
                inhTagsList.add(epv);
            }
        }
        Collections.sort(inhTagsList, new InheritedTagEpvComparator());
        return inhTagsList;
    }


    @Override
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

        ICDContentModel cm = new ICDContentModel((OWLModel) getProject(projectName).getKnowledgeBase());
        RDFProperty displayStatusProp = cm.getDisplayStatusProperty();
        RDFProperty isObsoleteProp = cm.getIsObsoleteProperty();

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
                setDisplayStatus(subcls, displayStatusProp, subclassEntityData, cm);
                setObsoleteStatus(subcls, isObsoleteProp, subclassEntityData);

                String user = KBUtil.getUserInSession(getThreadLocalRequest());
                if (user != null) {
                    subclassEntityData.setWatch(WatchedEntitiesCache.getCache(project).getWatchType(user, subcls.getName()));
                }

            }
        }
        return subclassesData;
    }

    private void setDisplayStatus(Cls cls, Slot displayStatusSlot, EntityData entity, ICDContentModel cm) {
        RDFResource status = (RDFResource) cls.getOwnSlotValue(displayStatusSlot);
        if (status == null) {
            return;
        }
        String prop = "status";
        if (status.equals(cm.getDisplayStatusBlueInst())) {
            entity.setProperty(prop, DisplayStatus.B.toString());
        } else if (status.equals(cm.getDisplayStatusYellowInst())) {
            entity.setProperty(prop, DisplayStatus.Y.toString());
        } else if (status.equals(cm.getDisplayStatusRedInst())) {
            entity.setProperty(prop, DisplayStatus.R.toString());
        }
    }


    private void setObsoleteStatus(Cls cls, Slot obsoleteSlot, EntityData entity) {
        Boolean isObsolete = (Boolean) cls.getOwnSlotValue(obsoleteSlot);
        if (isObsolete != null && isObsolete == true) {
            entity.setProperty("obsolete", "true");
        }
    }

    class InheritedTagEpvComparator implements Comparator<EntityPropertyValues> {
        public int compare(EntityPropertyValues epv1, EntityPropertyValues epv2) {
            Collection<EntityData> inhTags1 = epv1.getPropertyValues(new PropertyEntityData(COL_INH_FOM));
            Collection<EntityData> inhTags2 = epv2.getPropertyValues(new PropertyEntityData(COL_INH_FOM));
            if (inhTags1.size() == inhTags2.size()) {
                EntityData tag1 = CollectionUtilities.getFirstItem(epv1.getPropertyValues(new PropertyEntityData(COL_TAG)));
                EntityData tag2 = CollectionUtilities.getFirstItem(epv2.getPropertyValues(new PropertyEntityData(COL_TAG)));
                return tag2.getBrowserText().compareTo(tag1.getBrowserText());
            }
            return inhTags1.size() - inhTags2.size();
        }

    }

    public EntityPropertyValues changeIndexType(String projectName, String subject, String indexEntity,
            List<String> reifiedProps, String indexType) {
        Project project = getProject(projectName);
        if (project == null) {
            return null;
        }
        KnowledgeBase kb = project.getKnowledgeBase();
        ICDContentModel cm = new ICDContentModel((OWLModel) kb);

        Cls cls = kb.getCls(subject);
        if (cls == null || (! (cls instanceof RDFResource))) {
            return null;
        }
        RDFResource subjResource = (RDFResource)cls;

        Instance indexInst = kb.getInstance(indexEntity);
        if (indexInst == null) {
            return null;
        }

        boolean convertToSynonym = ICDContentModelConstants.TERM_SYNONYM_CLASS.equals(indexType);
        boolean convertToNarrower = ICDContentModelConstants.TERM_NARROWER_CLASS.equals(indexType);
        boolean convertToBaseIndex = ! (convertToSynonym || convertToNarrower);

        RDFSNamedClass termSynonymClass = cm.getTermSynonymClass();
        RDFSNamedClass termNarrowerClass = cm.getTermNarrowerClass();
        RDFSNamedClass termBaseIndexClass = cm.getTermBaseIndexClass();
        if (termSynonymClass != null && termNarrowerClass != null && termBaseIndexClass != null) {
            if (convertToSynonym && (! indexInst.hasDirectType(termSynonymClass))) {
                indexInst.addDirectType(termSynonymClass);
            }
            else if (convertToNarrower && (! indexInst.hasDirectType(termNarrowerClass))) {
                indexInst.addDirectType(termNarrowerClass);
            }
            else if (convertToBaseIndex && (! indexInst.hasDirectType(termBaseIndexClass))) {
                indexInst.addDirectType(termBaseIndexClass);
            }

            if ((convertToNarrower || convertToBaseIndex) && indexInst.hasDirectType(termSynonymClass)) {
                indexInst.removeDirectType(termSynonymClass);
            }
            if ((convertToSynonym || convertToBaseIndex) && indexInst.hasDirectType(termNarrowerClass)) {
                indexInst.removeDirectType(termNarrowerClass);
            }
            if ((convertToSynonym || convertToNarrower) && indexInst.hasDirectType(termBaseIndexClass)) {
                indexInst.removeDirectType(termBaseIndexClass);
            }
        }

        RDFProperty synonymProperty = cm.getSynonymProperty();
        if (synonymProperty != null) {
            if (convertToSynonym && (! subjResource.hasPropertyValue(synonymProperty, indexInst))) {
                subjResource.addPropertyValue(synonymProperty, indexInst);
            }
            else if ((convertToNarrower || convertToBaseIndex) && subjResource.hasPropertyValue(synonymProperty, indexInst)) {
                subjResource.removePropertyValue(synonymProperty, indexInst);
            }
        }
        RDFProperty narrowerProperty = cm.getNarrowerProperty();
        if (narrowerProperty != null) {
            if (convertToNarrower && (! subjResource.hasPropertyValue(narrowerProperty, indexInst))) {
                subjResource.addPropertyValue(narrowerProperty, indexInst);
            }
            else if ((convertToSynonym || convertToBaseIndex) && subjResource.hasPropertyValue(narrowerProperty, indexInst)) {
                subjResource.removePropertyValue(narrowerProperty, indexInst);
            }
        }
        RDFProperty baseIndexProperty = cm.getBaseIndexProperty();
        if (baseIndexProperty != null) {
            if ((convertToBaseIndex)&& (! subjResource.hasPropertyValue(baseIndexProperty, indexInst))) {
                subjResource.addPropertyValue(baseIndexProperty, indexInst);
            }
            else if ((convertToSynonym || convertToNarrower)&& subjResource.hasPropertyValue(baseIndexProperty, indexInst)) {
                subjResource.removePropertyValue(baseIndexProperty, indexInst);
            }
        }

        EntityPropertyValues res = new EntityPropertyValues(new EntityData(subject));
        for (String reifiedPropName : reifiedProps) {
            Slot reifiedSlot = kb.getSlot(reifiedPropName);
            if (reifiedSlot != null) {
                res.addPropertyValues(new PropertyEntityData(reifiedSlot.getName()), createEntityList((List<Object>) indexInst.getOwnSlotValues(reifiedSlot)));
            }
        }
        return res;
    }

    public EntityPropertyValues changeInclusionFlagForIndex(String projectName, String subject, String indexEntity,
            List<String> reifiedProps, boolean isInclusionFlag) {
        Project project = getProject(projectName);
        if (project == null) {
            return null;
        }
        KnowledgeBase kb = project.getKnowledgeBase();
        ICDContentModel cm = new ICDContentModel((OWLModel) kb);

        Cls cls = kb.getCls(subject);
        if (cls == null || (! (cls instanceof RDFResource))) {
            return null;
        }
        RDFResource subjResource = (RDFResource)cls;

        Instance indexInst = kb.getInstance(indexEntity);
        if (indexInst == null) {
            return null;
        }

        RDFSNamedClass termBaseIndexClass = cm.getTermBaseInclusionClass();
        if (termBaseIndexClass != null) {
            if (isInclusionFlag && (! indexInst.hasDirectType(termBaseIndexClass))) {
                indexInst.addDirectType(termBaseIndexClass);
            }
            else {
                indexInst.removeDirectType(termBaseIndexClass);
            }
        }

        RDFProperty indexBaseInclusionProperty = cm.getIndexBaseInclusionProperty();
        if (indexBaseInclusionProperty != null) {
            if (isInclusionFlag && (! subjResource.hasPropertyValue(indexBaseInclusionProperty, indexInst))) {
                subjResource.addPropertyValue(indexBaseInclusionProperty, indexInst);
            }
            else {
                subjResource.removePropertyValue(indexBaseInclusionProperty, indexInst);
            }
        }

        EntityPropertyValues res = new EntityPropertyValues(new EntityData(subject));
        for (String reifiedPropName : reifiedProps) {
            Slot reifiedSlot = kb.getSlot(reifiedPropName);
            if (reifiedSlot != null) {
                res.addPropertyValues(new PropertyEntityData(reifiedSlot.getName()), createEntityList((List<Object>) indexInst.getOwnSlotValues(reifiedSlot)));
            }
        }
        return res;
    }

}
