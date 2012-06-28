package edu.stanford.bmir.protege.web.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import edu.stanford.bmir.protege.web.client.model.event.AbstractOntologyEvent;
import edu.stanford.bmir.protege.web.client.model.event.EntityCreateEvent;
import edu.stanford.bmir.protege.web.client.model.event.EntityDeleteEvent;
import edu.stanford.bmir.protege.web.client.model.event.EntityRenameEvent;
import edu.stanford.bmir.protege.web.client.model.event.EventType;
import edu.stanford.bmir.protege.web.client.model.event.OntologyEvent;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;
import edu.stanford.smi.protege.collab.util.HasAnnotationCache;
import edu.stanford.smi.protege.event.ClsAdapter;
import edu.stanford.smi.protege.event.ClsEvent;
import edu.stanford.smi.protege.event.ClsListener;
import edu.stanford.smi.protege.event.KnowledgeBaseAdapter;
import edu.stanford.smi.protege.event.KnowledgeBaseEvent;
import edu.stanford.smi.protege.event.KnowledgeBaseListener;
import edu.stanford.smi.protege.event.SlotAdapter;
import edu.stanford.smi.protege.event.SlotEvent;
import edu.stanford.smi.protege.event.SlotListener;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.Log;

public class ServerEventManager {

    /*
     * TODO: this is a memory leak: we keep all the change events
     * since the server was started.. this set can get veeery big.
     * We need to serialize it somehow.
     */
    private List<OntologyEvent> events;

    private ServerProject<Project> serverProject;
    private KnowledgeBaseListener kbListener;
    private ClsListener clsListener;
    private SlotListener slotListener;
    //private FrameListener frameListener;

    /*
     * TODO: commenting out the frame listener for now -> no widget uses it so far
     * and it generates too many events and the clients do not need them for now.
     * Think about an alternative way of handling property value events - they are too verbose..
     */

    // TODO: We should be able to start with a different version number
    public ServerEventManager(ServerProject serverProject) {
        this.serverProject = serverProject;
        this.events = new ArrayList<OntologyEvent>();

        createListeners();
        addListeners();
    }

    private void createListeners() {
        kbListener = createKBListener();
        clsListener = createClsListener();
        slotListener = createSlotListener();
        //frameListener = createFrameListener();
    }

    private KnowledgeBaseListener createKBListener() {
        kbListener = new KnowledgeBaseAdapter() {
            @Override
            public void clsCreated(KnowledgeBaseEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                if (isAnonymousName(event.getCls().getName())) {
                    return;
                }
                events.add(createEvent(event, EventType.CLASS_CREATED, getEntityDataList(event.getCls().getDirectSuperclasses())));
            }

            @Override
            public void slotCreated(KnowledgeBaseEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                events.add(createEvent(event, EventType.PROPERTY_CREATED, getEntityDataList(event.getSlot().getDirectSuperslots())));
            }

            @Override
            public void instanceCreated(KnowledgeBaseEvent event) {
                //TODO: commenting out for now - it generates too much - find a better way
                //events.add(createEvent(event, EventType.INDIVIDUAL_CREATED, null));
            }

            @Override
            public void clsDeleted(KnowledgeBaseEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                if (isAnonymousName(event.getCls().getName())) {
                    return;
                }
                events.add(deleteEvent(event, EventType.CLASS_DELETED, getEntityDataList(event.getCls().getDirectSuperclasses())));
            }

            @Override
            public void slotDeleted(KnowledgeBaseEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                events.add(deleteEvent(event, EventType.PROPERTY_DELETED, getEntityDataList(event.getSlot().getDirectSuperslots())));
            }

            @Override
            public void frameReplaced(KnowledgeBaseEvent event) {
                events.add(replaceEvent(event, EventType.ENTITY_RENAMED, event.getOldName()));
            }
        };
        return kbListener;
    }

    private ClsListener createClsListener() {
        clsListener = new ClsAdapter() {
            @Override
            public void directSubclassRemoved(ClsEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                if (isAnonymousName(event.getCls().getName()) || isAnonymousName(event.getSubclass().getName())) {
                    return;
                }
                events.add(createEvent(event, EventType.SUBCLASS_REMOVED, getEntityDataList(CollectionUtilities.createCollection(event.getSubclass())))); //3rd arg - the subclass
            }

            @Override
            public void directSubclassAdded(ClsEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                if (isAnonymousName(event.getCls().getName()) || isAnonymousName(event.getSubclass().getName())) {
                    return;
                }
                events.add(createEvent(event, EventType.SUBCLASS_ADDED, getSubclassEntityDataList(CollectionUtilities.createCollection(event.getSubclass()), true)));
            }

            @Override
            public void directInstanceAdded(ClsEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                if (isAnonymousName(event.getCls().getName())) {
                    return;
                }
                events.add(individualAddedRemovedEvent(event));
            };

            @Override
            public void directInstanceRemoved(ClsEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                if (isAnonymousName(event.getCls().getName())) {
                    return;
                }
                events.add(individualAddedRemovedEvent(event));
            };

        };
        return clsListener;
    }

    private SlotListener createSlotListener() {
        slotListener = new SlotAdapter() {
            @Override
            public void directSubslotRemoved(SlotEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                events.add(createEvent(event, EventType.SUBPROPERTY_REMOVED, getEntityDataList(CollectionUtilities
                        .createCollection(event.getSubslot())))); //3rd arg - the subclass
            }

            @Override
            public void directSubslotAdded(SlotEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                events.add(createEvent(event, EventType.SUBPROPERTY_ADDED, getEntityDataList(CollectionUtilities
                        .createCollection(event.getSubslot()))));
            }
        };
        return slotListener;
    }
/*
    private FrameListener createFrameListener() {
        frameListener = new FrameAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void ownSlotValueChanged(FrameEvent event) {
                if (event.isReplacementEvent()) {
                    return;
                }
                Frame frame = event.getFrame();
                Slot slot = event.getSlot();

                //FIXME: check if you want to filter out all system slots!!
                if (slot.isSystem()) {
                    return;
                }

                Collection oldValues = event.getOldValues();
                Collection newValues = frame.getOwnSlotValues(slot);

                if (newValues == null) {
                    newValues = new ArrayList();
                }
                if (oldValues == null) {
                    oldValues = new ArrayList();
                }

                newValues = new ArrayList(newValues);
                oldValues = new ArrayList(oldValues);

                int eventType = EventType.PROPERTY_VALUE_CHANGED;
                if (oldValues.containsAll(newValues)) {
                    eventType = EventType.PROPERTY_VALUE_REMOVED;
                } else {
                    if (newValues.containsAll(oldValues)) {
                        eventType = EventType.PROPERTY_VALUE_ADDED;
                    } else {
                        eventType = EventType.PROPERTY_VALUE_CHANGED;
                    }
                }

                EntityData subject = OntologyServiceImpl.createEntityData(frame);
                PropertyEntityData prop = OntologyServiceImpl.createPropertyEntityData(slot, null, false);

                if (eventType == EventType.PROPERTY_VALUE_ADDED) {
                    newValues.removeAll(oldValues);
                    Collection<EntityData> addedEntityValues = getEntityDataList(newValues);
                    PropertyValueEvent pve = new PropertyValueEvent(subject, prop, addedEntityValues, null, eventType,
                            event.getUserName(), getServerRevision() + 1);
                    events.add(pve);
                } else if (eventType == EventType.PROPERTY_VALUE_REMOVED) {
                    oldValues.removeAll(newValues);
                    Collection<EntityData> removedEntityValues = getEntityDataList(oldValues);
                    PropertyValueEvent pve = new PropertyValueEvent(subject, prop, null, removedEntityValues,
                            eventType, event.getUserName(), getServerRevision() + 1);
                    events.add(pve);
                } else if (eventType == EventType.PROPERTY_VALUE_CHANGED) {
                    Collection tempNewValues = new ArrayList(newValues);
                    newValues.removeAll(oldValues);
                    Collection<EntityData> addedEntityValues = getEntityDataList(newValues);
                    PropertyValueEvent pve1 = new PropertyValueEvent(subject, prop, addedEntityValues, null,
                            EventType.PROPERTY_VALUE_ADDED, event.getUserName(), getServerRevision() + 1);
                    events.add(pve1);
                    oldValues.removeAll(tempNewValues);
                    Collection<EntityData> removedEntityValues = getEntityDataList(oldValues);
                    PropertyValueEvent pve2 = new PropertyValueEvent(subject, prop, null, removedEntityValues,
                            EventType.PROPERTY_VALUE_REMOVED, event.getUserName(), getServerRevision() + 1);
                    events.add(pve2);

                }
            }
        };
        return frameListener;
    }
*/
    public void startListening() {
        addListeners();
    }

    private void addListeners() {
        KnowledgeBase kb = serverProject.getProject().getKnowledgeBase();
        kb.addKnowledgeBaseListener(kbListener);
        kb.addClsListener(clsListener);
        kb.addSlotListener(slotListener);
        //kb.addFrameListener(frameListener);
    }

    private void removeListeners() {
        KnowledgeBase kb = serverProject.getProject().getKnowledgeBase();
        kb.removeKnowledgeBaseListener(kbListener);
        kb.removeClsListener(clsListener);
        //kb.removeFrameListener(frameListener);
    }

    public int getServerRevision() {
        return events.size();
    }

    public ArrayList<OntologyEvent> getEvents(long fromVersion) {
        return getEvents(fromVersion, events.size());
    }

    public ArrayList<OntologyEvent> getEvents(long fromVersion, long toVersion) {
        ArrayList<OntologyEvent> fromToEvents = new ArrayList<OntologyEvent>();

        //TODO: check these conditions
        if (fromVersion < 0) {
            fromVersion = 0;
        }
        if (toVersion > events.size()) {
            toVersion = events.size();
        }
        for (long i = fromVersion; i < toVersion; i++) {
            fromToEvents.add(events.get((int) i)); //fishy
        }

        //Log.getLogger().info("SERVER: GetEvents from: " + fromVersion + " to: " + toVersion + " events size: " + events.size() + " Events: " + fromToEvents);

        return fromToEvents.size() == 0 ? null : fromToEvents;
    }

    public void dispose() {
        try { //dispose should never fail
            removeListeners();
        } catch (Exception e) {
            Log.getLogger().log(Level.WARNING, "Error at disposing the server event manager." , e);
        }
    }

    /*
     * Utility methods
     */

    List<EntityData> getEntityDataList(Collection frames) {
        return getEntityDataList(frames, false);
    }

    @SuppressWarnings("unchecked")
    ArrayList<EntityData> getEntityDataList(Collection frames, boolean computeAnnotation) {
        if (frames == null) {
            return null;
        }

        ArrayList<EntityData> entityDataList = new ArrayList<EntityData>();

        for (Iterator iterator = frames.iterator(); iterator.hasNext();) {
            entityDataList.add(OntologyServiceImpl.createEntityData(iterator.next(), computeAnnotation));
        }
        return entityDataList;
    }


    List<EntityData> getSubclassEntityDataList(Collection<Cls> clses, boolean computeAnnotation) {
        if (clses == null) {
            return null;
        }

        ArrayList<EntityData> entityDataList = new ArrayList<EntityData>();
        for (Object o : clses) {
            if (o instanceof Cls) {
                Cls subcls = (Cls) o;
                //sending also the types of the subclass,because it is needed by the cls tree. Not a good enough reason
                SubclassEntityData subclassEntityData = new SubclassEntityData(subcls.getName(),
                        OntologyServiceImpl.getBrowserText(subcls), getEntityDataList(subcls.getDirectTypes()), subcls.getVisibleDirectSubclassCount());
                if (computeAnnotation) {
                    subclassEntityData.setLocalAnnotationsCount(HasAnnotationCache.getAnnotationCount(subcls));
                    subclassEntityData.setChildrenAnnotationsCount(HasAnnotationCache.getChildrenAnnotationCount(subcls));
                }
                entityDataList.add(subclassEntityData);
            }
        }
        return entityDataList;
    }

    private OntologyEvent createEvent(KnowledgeBaseEvent event, int type, List<EntityData> superEntities) {
        EntityData entity = OntologyServiceImpl.createEntityData(event.getFrame());
        return new EntityCreateEvent(entity, type, event.getUserName(), superEntities, getServerRevision() + 1);
    }

    private OntologyEvent createEvent(ClsEvent event, int type, List<EntityData> superEntities) {
        EntityData entity = OntologyServiceImpl.createEntityData(event.getCls());
        //TODO: change type of event
        return new EntityCreateEvent(entity, type, event.getUserName(), superEntities, getServerRevision() + 1);
    }

    private OntologyEvent createEvent(SlotEvent event, int type, List<EntityData> superEntities) {
        EntityData entity = OntologyServiceImpl.createEntityData(event.getSlot());
        //TODO: change type of event
        return new EntityCreateEvent(entity, type, event.getUserName(), superEntities, getServerRevision() + 1);
    }

    private OntologyEvent deleteEvent(KnowledgeBaseEvent event, int type, List<EntityData> superEntities) {
        EntityData entity = OntologyServiceImpl.createEntityData(event.getFrame());
        return new EntityDeleteEvent(entity, type, event.getUserName(), superEntities, getServerRevision() + 1);
    }

    private OntologyEvent replaceEvent(KnowledgeBaseEvent event, int type, String oldName) {
        EntityData entity = OntologyServiceImpl.createEntityData(event.getNewFrame());
        return new EntityRenameEvent(entity, oldName, event.getUserName(), getServerRevision() + 1);
    }

    private OntologyEvent individualAddedRemovedEvent(ClsEvent event) {
        EntityData entity = OntologyServiceImpl.createEntityData(event.getCls());
        return new AbstractOntologyEvent(entity, EventType.INDIVIDUAL_ADDED_OR_REMOVED, event.getUserName(), getServerRevision() + 1);
    }

    private boolean isAnonymousName(String name) { //should apply only to OWL
        return name.length() > 0 && name.charAt(0) == '@';
    }

}
