package edu.stanford.bmir.protege.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.stanford.bmir.protege.web.client.rpc.ChAOService;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.client.rpc.data.ReviewData;
import edu.stanford.bmir.protege.web.client.rpc.data.Watch;
import edu.stanford.bmir.protegex.chao.ChAOKbManager;
import edu.stanford.bmir.protegex.chao.annotation.api.Annotation;
import edu.stanford.bmir.protegex.chao.annotation.api.AnnotationFactory;
import edu.stanford.bmir.protegex.chao.annotation.api.Review;
import edu.stanford.bmir.protegex.chao.change.api.Change;
import edu.stanford.bmir.protegex.chao.change.api.ChangeFactory;
import edu.stanford.bmir.protegex.chao.change.api.Composite_Change;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.OntologyComponentFactory;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.Ontology_Component;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.Reviewer;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.Timestamp;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.User;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.impl.DefaultTimestamp;
import edu.stanford.bmir.protegex.chao.util.interval.TimeIntervalCalculator;
import edu.stanford.smi.protege.collab.changes.ChAOUtil;
import edu.stanford.smi.protege.collab.util.HasAnnotationCache;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Frame;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.Transaction;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.RDFSNamedClass;
import edu.stanford.smi.protegex.server_changes.ServerChangesUtil;

/**
 * @author Csongor Nyulas <csongor.nyulas@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class ChAOServiceImpl extends RemoteServiceServlet implements ChAOService {
    private static final long serialVersionUID = -6201244374065093182L;

    public KnowledgeBase getChAOKb(String projectName) {
        Project prj = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        if (prj == null) {
            return null;
        }
        return ChAOKbManager.getChAOKb(prj.getKnowledgeBase());
    }

    protected Project getProject(String projectName) {
        return ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
    }

    /*
     * Notes methods
     */

    public Boolean archiveNote(String projectName, String nodeId, boolean archive){
        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        if (project == null) {  return false; }

        KnowledgeBase kb = project.getKnowledgeBase();

        KnowledgeBase chaoKb = ChAOKbManager.getChAOKb(kb);
        if (chaoKb == null) {
            return false;
        }
        AnnotationFactory factory = new AnnotationFactory(chaoKb);
        Annotation annotation = factory.getAnnotation(nodeId);
        try {
            annotation.setArchived(archive);
        } catch (Exception e) {
            Log.getLogger().warning("Could not archive note. Most likely, ChAO does not have an 'archived' slot");
            return false;
        }
        return true;
    }

    public List<NotesData> getReplies(String projectName, String noteId, boolean topLevelOnly){
        ArrayList<NotesData> notes = new ArrayList<NotesData>();
        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        if(project == null){
            return notes;
        }

        KnowledgeBase kb = project.getKnowledgeBase();

        KnowledgeBase chaoKb = ChAOKbManager.getChAOKb(kb);
        if (chaoKb == null) {
            return notes;
        }
        AnnotationFactory factory = new AnnotationFactory(chaoKb);
        Annotation annotation = factory.getAnnotation(noteId);

        NotesData note = getDiscussionThread(annotation, chaoKb, false);
        if(topLevelOnly && note.getReplies() != null){
            for(NotesData reply : note.getReplies()){
                reply.setReplies(null);
            }
        }

        return note.getReplies();
    }

    public NotesData editNote(String projectName, NotesData note, String noteId) {
        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        if (project == null) {
            return note;
        }

        KnowledgeBase kb = project.getKnowledgeBase();

        KnowledgeBase chaoKb = ChAOKbManager.getChAOKb(kb);
        if (chaoKb == null) {
            return note;
        }
        AnnotationFactory factory = new AnnotationFactory(chaoKb);
        Annotation annotation = factory.getAnnotation(noteId);
        annotation.setBody(note.getBody());
        annotation.setSubject(note.getSubject());
        //annotation.set(note.getType())

        //set type
        Instance annInstance = factory.getWrappedProtegeInstance(annotation);

        Cls oldTypeCls = annInstance.getDirectType();

        String newType = note.getType();
        if (newType == null || newType.length() == 0) {
            newType = "Comment";
        }
        Cls newTypeCls = chaoKb.getCls(newType);
        if (newTypeCls == null) {
            newTypeCls = factory.getCommentClass();
        }

        if ( ! oldTypeCls.equals(newTypeCls) ) {
            annInstance.addDirectType(newTypeCls);
            annInstance.removeDirectType(oldTypeCls);
            annotation = factory.getAnnotation(noteId);
        }
        //end set type


        note.setEntity(new EntityData(factory.getProtegeName(annotation)));
        note.setBody(annotation.getBody());
        note.setAuthor(annotation.getAuthor());
        note.setSubject(annotation.getSubject());

        Timestamp ts = annotation.getCreated();
        if (ts != null) {
            note.setCreationDate(ts.getDate());
        }
        note.setType(annInstance.getDirectType().getName());
        note.setLatestUpdate(new Date());
        return note;
    }

    public List<NotesData> getNotes(String projectName, String entityName, boolean ontologyLevelNotes, boolean topLevelNotesOnly){
        ArrayList<NotesData> notes = new ArrayList<NotesData>();
        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);

        if (project == null) {  return notes; }

        KnowledgeBase kb = project.getKnowledgeBase();

        Collection<Annotation> annotations = new ArrayList<Annotation>();

        if (ontologyLevelNotes) {
            annotations = ChAOUtil.getTopLevelDiscussionThreads(kb);
        } else {
            Frame frame = kb.getFrame(entityName);
            if (frame == null) {
                return notes;
            }
            Ontology_Component ontologyComponent = ChAOUtil.getOntologyComponent(frame, true);
            if (ontologyComponent == null) {
                return notes;
            }
            annotations = new ArrayList<Annotation>(ontologyComponent.getAssociatedAnnotations());
        }

        for (Annotation annotation : annotations) {
            NotesData note = getDiscussionThread(annotation, ChAOKbManager.getChAOKb(kb), topLevelNotesOnly);
            notes.add(note);
        }
        Collections.sort(notes, new NotesDataComparator());
        return notes;
    }

    public List<NotesData> getNotes(String projectName, String entityName, boolean ontologyLevelNotes) {
        return getNotes(projectName, entityName, ontologyLevelNotes, false);
    }

    private NotesData getDiscussionThread(Annotation annotation, KnowledgeBase changesKb, boolean topLevelOnly) {
        NotesData note = createNoteData(annotation, changesKb);
        try {
            note.setArchived(annotation.hasArchived() ? annotation.getArchived() : false);
        } catch (Exception e) {
            if (Log.getLogger().getLevel().equals(Level.FINE)) {
                Log.getLogger().fine("Could not set archived flag on NotesData. Most likely ChAO does not contain a 'archived' slot");
            }
        }
        String d = note.getCreationDate();

        Date updateDate = DefaultTimestamp.getDateParsed(d);
        if(updateDate != null){
            note.setLatestUpdate(DefaultTimestamp.getDateParsed(d));
        }

        NotesData tempNote = topLevelOnly ? new NotesData(): note;

        for (Annotation annotation2 : annotation.getAssociatedAnnotations()) {
            Annotation reply = annotation2;
            tempNote.addReply(getDiscussionThread(reply, changesKb, topLevelOnly));
        }

        if(tempNote.getReplies()!= null && tempNote.getReplies().size() > 0){
            NotesDataComparator comparator = new NotesDataComparator();
            Collections.sort(tempNote.getReplies(), comparator);

            int val = comparator.compare(note, tempNote.getReplies().get(0));
            if(val > 0){
                note.setLatestUpdate(tempNote.getReplies().get(0).getLatestUpdate());
            }
        }

        note.setNumOfReplies(tempNote.getNumOfReplies());

        return note;
    }

    private NotesData createNoteData(Annotation annotation, KnowledgeBase changesKb) {
        NotesData note = new NotesData();
        AnnotationFactory factory = new AnnotationFactory(changesKb);
        note.setEntity(new EntityData(factory.getProtegeName(annotation)));
        note.setBody(annotation.getBody());
        note.setAuthor(annotation.getAuthor());
        note.setSubject(annotation.getSubject());
        try { //backwards compatibility - archive was not there in previous versions
            note.setArchived(annotation.hasArchived());
        } catch (Exception e) {
            Log.getLogger().warning("Did not set archived status for note. Most likely archive slot is not present in Chao.");
        }
        Timestamp ts = annotation.getCreated();
        if (ts != null) {
            note.setCreationDate(ts.getDate());
        }
        note.setType(factory.getAnnotationType(annotation));
        return note;
    }

    public Collection<EntityData> getAvailableNoteTypes(String projectName) {
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return null;
        }
        AnnotationFactory factory = new AnnotationFactory(chaoKb);
        Cls annotCls = factory.getAnnotationClass();
        Collection<EntityData> allAnnotationTypes = new ArrayList<EntityData>();
        for (Object obj : annotCls.getSubclasses()) {
            Cls annotSubCls = (Cls) obj;
            if (!annotSubCls.isAbstract()) {
                allAnnotationTypes.add(OntologyServiceImpl.createEntityData(annotSubCls, false));
            }
        }
        return allAnnotationTypes;
    }

    public NotesData createNote(String projectName, NotesData newNote, boolean topLevel) {
        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        KnowledgeBase kb = project.getKnowledgeBase();
        KnowledgeBase changeKb = ChAOUtil.getChangesKb(kb);
        if (changeKb == null) {
            return null;
        }

        String type = newNote.getType();
        if (type == null || type.length() == 0) {
            type = "Comment";
        }
        AnnotationFactory factory = new AnnotationFactory(changeKb);
        Annotation annotation = null;
        String annotatedEntityName = (newNote.getAnnotatedEntity() == null ? null : newNote.getAnnotatedEntity()
                .getName());
        Instance annotatedInst = null;
        if (!topLevel && annotatedEntityName != null) {
            annotatedInst = kb.getInstance(annotatedEntityName);
            if (annotatedInst == null) {
                annotatedInst = changeKb.getInstance(annotatedEntityName);
            }
        }
        Cls annotationCls = changeKb.getCls(type);
        if (annotationCls == null) {
            annotationCls = factory.getCommentClass();
        }
        annotation = ChAOUtil.createAnnotationOnAnnotation(kb, annotatedInst, annotationCls);

        factory.fillDefaultValues(annotation);
        annotation.setAuthor(newNote.getAuthor());
        annotation.setBody(newNote.getBody());
        annotation.setSubject(newNote.getSubject());


        NotesData note = createNoteData(annotation, changeKb);
        String d = note.getCreationDate();
        note.setLatestUpdate(DefaultTimestamp.getDateParsed(d));
        return note;
    }

    public void deleteNote(String projectName, String noteId) {
        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        if (project == null) {
            return;
        }
        KnowledgeBase kb = project.getKnowledgeBase();
        KnowledgeBase chaoKb = ChAOKbManager.getChAOKb(kb);
        KnowledgeBase changeKb = ChAOUtil.getChangesKb(kb);
        if (chaoKb == null) {
            return;
        }
        AnnotationFactory factory = new AnnotationFactory(chaoKb);
        Annotation note = factory.getAnnotation(noteId);
        if (note != null) {
            // TODO- hack because annotation cache is not working //TODO: not
            // sure this is true
            Collection<Ontology_Component> annotatedOntComps = ServerChangesUtil.getAnnotatedOntologyComponents(note);
            for (Ontology_Component annotatedOntComp : annotatedOntComps) {
                String annotatedEntityName = annotatedOntComp.getCurrentName();
                if (annotatedEntityName == null) {
                    annotatedEntityName = annotatedOntComp.getInitialName();
                }
                if (annotatedEntityName != null) {
                    Frame annotatedInst = kb.getInstance(annotatedEntityName);
                    if (annotatedInst == null) {
                        annotatedInst = changeKb.getInstance(annotatedEntityName);
                    }

                    if (annotatedInst != null) {
                        HasAnnotationCache.removeAnnotation(annotatedInst);
                    }
                }
            }

            note.delete();
        } else {
            // TODO: note is not present.. maybe throw exception?
        }
    }

    /*
     * Change methods
     */

    public ChangeData createChange(String projectName, String userName, String entityName, final String action, final String context) {
        try {
            Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
            if (project == null) {
                return null;
            }
            KnowledgeBase kb = project.getKnowledgeBase();
            KnowledgeBase changeKb = ChAOUtil.getChangesKb(kb);
            if (changeKb == null) {
                return null;
            }
            Frame frame = kb.getFrame(entityName);
            if (frame == null) {
                return null;
            }
            Ontology_Component ontologyComponent = ChAOUtil.getOntologyComponent(frame, true);

            if (ontologyComponent == null){
                return null;
            }

            ChangeFactory factory = new ChangeFactory(changeKb);
            final Composite_Change composite_change = factory.createComposite_Change(null);
            composite_change.setAction(action);
            composite_change.setAuthor(userName);

            composite_change.setContext(context);
            final Timestamp timestamp = DefaultTimestamp.getTimestamp(changeKb);
            composite_change.setTimestamp(timestamp);
            composite_change.setApplyTo(ontologyComponent);// must go last ....
            return createChangeData(userName, context, timestamp.getDateParsed());
        } catch (RuntimeException e) {
            log("Caught when trying to append change in project " + projectName + " for user " + userName + ", entity = " + entityName + ", action = " + action + " and context = " + context, e);
            throw e;
        }
    }

    public Collection<ChangeData> getChanges(String projectName, Date start, Date end) {
        ArrayList<ChangeData> changeData = new ArrayList<ChangeData>();
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return changeData;
        }

        TimeIntervalCalculator tic = TimeIntervalCalculator.get(chaoKb);
        Collection<Change> changes = tic.getTopLevelChanges(start, end);

        return getChangeData(changes);
    }

    public PaginationData<ChangeData> getChanges(String projectName, Date startDate, Date endDate, int start, int limit, String sort, String dir) {
            ArrayList<ChangeData> changeData = (ArrayList<ChangeData>) getChanges(projectName, startDate, endDate);
            return PaginationServerUtil.pagedRecords(changeData, start, limit, sort, dir);
     }

    public PaginationData<ChangeData> getChanges(String projectName, String entityName, int start, int limit, String sort, String dir) {
        ArrayList<ChangeData> changeData = (ArrayList<ChangeData>) getChanges(projectName, entityName);
        return PaginationServerUtil.pagedRecords(changeData, start, limit, sort, dir);
    }

    public Collection<ChangeData> getChanges(String projectName, String entityName) {
        ArrayList<ChangeData> changeData = new ArrayList<ChangeData>();
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return changeData;
        }
        Frame frame = getProject(projectName).getKnowledgeBase().getFrame(entityName);
        if (frame == null) {
            return changeData; // TODO: rather throw exception?
        }
        return getChangeData(ChAOUtil.getTopLevelChanges(frame));
    }

    protected Collection<ChangeData> getChangeData(Collection<Change> changes) {
        ArrayList<ChangeData> changeData = new ArrayList<ChangeData>();
        if (changes != null) {
            for (Change change : changes) {
                ChangeData data = new ChangeData();
                data.setAuthor(change.getAuthor());
                data.setDescription(getChangeDescription(change.getContext()));
                data.setTimestamp(change.getTimestamp().getDateParsed());
                changeData.add(data);
            }
        }
        return changeData;
    }

    private String getChangeDescription(String text) {
        if (text == null) {
            return "No details";
        }
        int index = text.indexOf(Transaction.APPLY_TO_TRAILER_STRING);
        if (index > 0) {
            return text.substring(0, index);
        }
        return text;
    }

    public Integer getNumChanges(String projectName, Date start, Date end) {
        Integer retval = 0;
        KnowledgeBase changesKB = getChAOKb(projectName);
        if (changesKB == null) {
            return retval;
        }
        TimeIntervalCalculator tic = TimeIntervalCalculator.get(changesKB);
        Collection<Change> changes = tic.getTopLevelChanges(start, end);
        if (changes != null) {
            retval = changes.size();
        }
        return new Integer(retval);
    }

    public PaginationData<ChangeData> getWatchedEntities(String projectName, String userName, int start, int limit, String sort, String dir) {
        ArrayList<ChangeData> watchedEntities = (ArrayList<ChangeData>) getWatchedEntities(projectName, userName);
        return PaginationServerUtil.pagedRecords(watchedEntities, start, limit, sort, dir);
    }

    private ChangeData createChangeData(String author, String description, Date timestamp){
       return new ChangeData(author, description, timestamp);
    }

    /*
     * Watched entities methods
     */

    //TODO: do we want to get the changes of the watched entities only from the last access time?
    //Now, we get all changes..
    public Collection<ChangeData> getWatchedEntities(String projectName, String userName) {

        ArrayList<ChangeData> changeData = new ArrayList<ChangeData>();

       // return changeData;

        //FIXME: intentionally commented out because it is toooo slow!!
       if (userName == null) {  return changeData; }
        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);

        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) { return changeData; }

        User user = ServerChangesUtil.getUser(chaoKb, userName);
        if (user == null) { return changeData;  }

        //TODO: duplicated logic with NotificationService - use common methods that should be extracted to a util class
        //This implementation does not use a cache for the watched entities

        // Get watched entities for ChAO user
        Collection<Ontology_Component> allOcs = new ArrayList<Ontology_Component>();
        Collection<Ontology_Component> watchedEntities = user.getWatchedEntity();
        if (watchedEntities != null) {
            allOcs.addAll(watchedEntities);
        }
        Collection<Ontology_Component> watchedBranches = user.getWatchedBranch();
        Collection<Ontology_Component> allOCsInWatchedBranches = getAllOCsInWatchedBranches(project.getKnowledgeBase(), watchedBranches);
        //we need this to be a list, not a set, because order is important - sorting is too expensive, so that's why the extra logic here
        for (Ontology_Component oc : allOCsInWatchedBranches) {
            if (!allOcs.contains(oc)) {
                allOcs.add(oc);
            }
        }

        return getChangesofOntologyComponents(project.getKnowledgeBase(), allOcs);

    }

    @SuppressWarnings("unchecked")
    private Collection<Ontology_Component> getAllOCsInWatchedBranches(KnowledgeBase kb, Collection<Ontology_Component> watchedBranches) {
        Collection<Frame> frames = new ArrayList<Frame>();
        for (Ontology_Component oc: watchedBranches) {
            String name = oc.getCurrentName();
            if (name != null) {
                Frame frame = kb.getFrame(name);
                if (frame != null) {
                    frames.add(frame);
                    if (frame instanceof Cls) {
                        Cls cls = (Cls) frame;
                        if (kb instanceof OWLModel) {
                            if (cls instanceof RDFSNamedClass) { //we don't include anonymous classes
                                frames.addAll(((RDFSNamedClass)cls).getSubclasses(true));
                            }
                        } else {
                            frames.addAll(cls.getSubclasses());
                        }
                    }
                }
            }
        }
        return getOntology_Components(frames);
    }

    private Collection<Ontology_Component> getOntology_Components(Collection<Frame> frames) {
        Collection<Ontology_Component> ocs = new ArrayList<Ontology_Component>();
        for (Frame frame : frames) {
            Ontology_Component oc = ServerChangesUtil.getOntologyComponent(frame);
            if (oc != null) {
                ocs.add(oc);
            }
        }
        return ocs;
    }

    //returns not only the changes, but also the notes
    private Collection<ChangeData> getChangesofOntologyComponents(KnowledgeBase kb, Collection<Ontology_Component> ocs) {
        ArrayList<ChangeData> changeData = new ArrayList<ChangeData>();
        if (ocs == null) { return changeData; }

        for (Ontology_Component entity : ocs) {
            // Get the name and browser text of the entity
            String name = entity.getCurrentName();
            EntityData entityData = null;
            if (name == null) { //delete frame
                entityData = new EntityData("Deleted_entity", "Deleted entity"); //TODO: try to get the deletion name
            } else {
                Frame frame = kb.getFrame(name);
                String browserText = frame.getBrowserText();
                entityData = new EntityData(name, browserText);
            }

            // Get changes for entity
            Collection<Change> changes = entity.getChanges();
            for (Change change : changes) {
                if (change.getPartOfCompositeChange() == null) { // get only top level changes
                    ChangeData data = new ChangeData();
                    data.setEntityData(entityData);
                    data.setAuthor(change.getAuthor());
                    data.setDescription(Transaction.removeApplyTo(change.getContext()));
                    data.setTimestamp(change.getTimestamp().getDateParsed());
                    changeData.add(data);
                }
            }

            Collection<Annotation> notes = entity.getAssociatedAnnotations();
            for (Annotation note : notes) {
                ChangeData data = new ChangeData();
                data.setEntityData(entityData);
                data.setAuthor(note.getAuthor());
                String subject = note.getSubject();
                data.setDescription("New note with subject: " + (subject == null ? ("no subject") : subject));
                data.setTimestamp(note.getCreated().getDateParsed());
                changeData.add(data);
            }
        }
        return changeData;
    }

    public EntityData addWatchedEntity(String projectName, String userName, String watchedEntityName) {
        if (userName == null) {
            return null;
        }
        final Project project = getProject(projectName);
        if (project == null){
            return null;
        }
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return null;
        }

        OntologyComponentFactory factory = new OntologyComponentFactory(chaoKb);
        User user = factory.getUserByName(userName);
        if (user == null) {
            user = factory.createUser(null);
            user.setName(userName);
        }

        //FIXME: add checks for null pointer!
        Frame frame = project.getKnowledgeBase().getFrame(watchedEntityName);
        Ontology_Component oc = ServerChangesUtil.getOntologyComponent(frame, true);
        user.addWatchedEntity(oc);

        //FIXME: this is not necessary: the listeners should take care of this
        WatchedEntitiesCache.getCache(project).addEntityWatch(watchedEntityName, userName);

        EntityData entityData = OntologyServiceImpl.createEntityData(frame, true);
        entityData.setWatch(WatchedEntitiesCache.getCache(project).isWatchedBranch(userName, watchedEntityName)  ?
                Watch.BOTH : Watch.ENTITY_WATCH);

        return entityData;
    }

   public EntityData addWatchedBranch(String projectName, String userName, String watchedEntityName) {
        if (userName == null) {
            return null;
        }
        final Project project = getProject(projectName);
        if (project == null){
            return null;
        }
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return null;
        }
        OntologyComponentFactory factory = new OntologyComponentFactory(chaoKb);
        User user = factory.getUserByName(userName);
        if (user == null) {
            user = factory.createUser(null);
            user.setName(userName);
        }
        Frame frame = project.getKnowledgeBase().getFrame(watchedEntityName);
        Ontology_Component oc = ServerChangesUtil.getOntologyComponent(frame, true);
        user.addWatchedBranch(oc);

       //FIXME: this is not necessary: the listeners should take care of this
       WatchedEntitiesCache.getCache(project).addBranchWatch(watchedEntityName, userName);

        EntityData entityData = OntologyServiceImpl.createEntityData(frame, true);
        entityData.setWatch(WatchedEntitiesCache.getCache(project).isWatchedEntity(userName, watchedEntityName)  ?
                Watch.BOTH : Watch.BRANCH_WATCH);

        return entityData;
    }

   //FIXME: why this doesn't return EntityData like the other watched methods?
    public void removeAllWatches(String projectName, String userName, String watchedEntityName) {
        if (userName == null) {
            return;
        }
        Project project = getProject(projectName);
        if (project == null){
            return ;
        }
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return;
        }
        OntologyComponentFactory factory = new OntologyComponentFactory(chaoKb);
        User user = factory.getUserByName(userName);
        if (user == null) {
            return;
        }
        //FIXME: null checks!!
        Frame frame = project.getKnowledgeBase().getFrame(watchedEntityName);
        Ontology_Component oc = ServerChangesUtil.getOntologyComponent(frame, false);

        if (oc != null) {
            user.removeWatchedEntity(oc);
            //FIXME: this is not necessary: the listeners should take care of this
            WatchedEntitiesCache.getCache(project).removeEntityWatch(watchedEntityName, userName);
            user.removeWatchedBranch(oc);
            //FIXME: this is not necessary: the listeners should take care of this
            WatchedEntitiesCache.getCache(project).removeBranchWatch(watchedEntityName, userName);
        }
    }

    /*
     * Review methods
     */

    public ArrayList<String> getReviewers(String projectName) {
        ArrayList<String> list = new ArrayList<String>();
        KnowledgeBase chaoKb = getChAOKb(projectName);

        // Shortcut
        if (chaoKb == null) {
            return null;
        }

        OntologyComponentFactory factory = new OntologyComponentFactory(chaoKb);
        Collection<Reviewer> reviewers = factory.getAllReviewerObjects(true);
        for (Reviewer reviewer : reviewers) {
            list.add(reviewer.getName());
        }

        return list;
    }

    public ArrayList<ReviewData> getReviews(String projectName, String entityName) {
        ArrayList<ReviewData> reviews = new ArrayList<ReviewData>();

        // Shortcut
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return null;
        }

        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        Frame frame = project.getKnowledgeBase().getFrame(entityName);
        Ontology_Component oc = ServerChangesUtil.getOntologyComponent(frame, true);

        Collection<Annotation> annotations = oc.getAssociatedAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.canAs(Review.class)) {
                ReviewData data = new ReviewData();
                data.setAuthor(annotation.getAuthor());
                data.setBody(annotation.getBody());

                Timestamp timestamp = annotation.getCreated();
                if (timestamp != null) {
                    Date date = timestamp.getDateParsed();
                    data.setCreated(date);
                }

                data.setSubject(annotation.getSubject());
                reviews.add(data);
            }
        }

        return reviews;
    }

    public void requestReview(String projectName, String entityName, List<String> reviewerNames) {
        // Shortcut
        if (reviewerNames == null) {
            return;

        }

        // Shortcut
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return;
        }

        for (String name : reviewerNames) {
            Reviewer reviewer = new OntologyComponentFactory(chaoKb).getReviewer(name);
            Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
            Frame frame = project.getKnowledgeBase().getFrame(entityName);
            Ontology_Component oc = ServerChangesUtil.getOntologyComponent(frame, true);
            reviewer.addPendingReview(oc);
        }
    }

    public void addReview(String projectName, String userName, NotesData data) {
        // Shortcut
        KnowledgeBase chaoKb = getChAOKb(projectName);
        if (chaoKb == null) {
            return;
        }

        Project project = ProjectManagerFactory.getProtege3ProjectManager().getProject(projectName);
        if (project == null) {
            return;
        }

        // Get the entity in the ontology that we want to annotate.
        String name = data.getAnnotatedEntity().getName();
        Frame frame = project.getKnowledgeBase().getFrame(name);

        // Create a new annotation (type "Review")
        Cls annotationCls = chaoKb.getCls(data.getType());
        Annotation annotation = ChAOUtil.createAnnotationOnAnnotation(project.getKnowledgeBase(), frame, annotationCls);
        annotation.setAuthor(data.getAuthor());
        annotation.setSubject(data.getSubject());
        annotation.setBody(data.getBody());
        annotation.setCreated(DefaultTimestamp.getTimestamp(chaoKb));

        /*
         * Now that review for this entity is added/completed, remove it as
         * pending from current user.
         */
        User user = new OntologyComponentFactory(chaoKb).getUserByName(userName);
        if (user != null) { // Need to check for this right? Because user might
            // not be in chao ontology?
            if (user instanceof Reviewer) {
                // Get the entity from the changes & annotations ontology.
                Ontology_Component oc = ServerChangesUtil.getOntologyComponent(frame, true);
                ((Reviewer) user).removePendingReview(oc);
            }
        }
    }

}
