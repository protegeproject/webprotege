package edu.stanford.bmir.protege.web.server.watches;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.app.App;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectFileStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectFileStoreFactory;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.user.UserDetails;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityVisitorExAdapter;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class WatchManagerImpl implements WatchManager, HasDispose {

    private static final String WATCHES_FILE_NAME = "watches.csv";

    private static final String ENTITY_FRAME_WATCH_NAME = "EntityFrameWatch";

    private static final String HIERARCHY_BRANCH_WATCH_NAME = "HierarchyBranchWatch";

    private ExecutorService emailExecutor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setPriority(Thread.MIN_PRIORITY).build());

    private Multimap<UserId, Watch<?>> userId2Watch = HashMultimap.create();

    private Multimap<Watch<?>, UserId> watch2UserId = HashMultimap.create();

    private Multimap<Object, Watch<?>> watchObject2Watch = HashMultimap.create();

    private OWLAPIProject project;


    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();

    private Lock writeLock = readWriteLock.writeLock();

    private File watchFile;

    private final WebProtegeProperties webProtegeProperties;

    private final WebProtegeLogger logger;

    public WatchManagerImpl(OWLAPIProject project) {
        this.project = project;
        this.webProtegeProperties = WebProtegeInjector.get().getInstance(WebProtegeProperties.class);
        this.logger = WebProtegeInjector.get().getInstance(WebProtegeLogger.class);
        OWLAPIProjectFileStoreFactory instance = WebProtegeInjector.get().getInstance(OWLAPIProjectFileStoreFactory.class);
        final OWLAPIProjectFileStore projectFileStore = instance.get(project.getProjectId());
        watchFile = new File(projectFileStore.getProjectDirectory(), WATCHES_FILE_NAME);

        if(watchFile.exists()) {
            readWatches();
        }
        project.getEventManager().addHandler(ClassFrameChangedEvent.TYPE, new ClassFrameChangedEventHandler() {
            @Override
            public void classFrameChanged(ClassFrameChangedEvent event) {
                handleFrameChanged(event.getEntity());
            }
        });
        project.getEventManager().addHandler(ObjectPropertyFrameChangedEvent.TYPE, new ObjectPropertyFrameChangedEventHandler() {
            @Override
            public void objectPropertyFrameChanged(ObjectPropertyFrameChangedEvent event) {
                handleFrameChanged(event.getEntity());
            }
        });
        project.getEventManager().addHandler(DataPropertyFrameChangedEvent.TYPE, new DataPropertyFrameChangedEventHandler() {
            @Override
            public void dataPropertyFrameChanged(DataPropertyFrameChangedEvent event) {
                handleFrameChanged(event.getEntity());
            }
        });
        project.getEventManager().addHandler(AnnotationPropertyFrameChangedEvent.TYPE, new AnnotationPropertyFrameChangedEventHandler() {
            @Override
            public void annotationPropertyFrameChanged(AnnotationPropertyFrameChangedEvent event) {
                handleFrameChanged(event.getEntity());
            }
        });
        project.getEventManager().addHandler(NamedIndividualFrameChangedEvent.TYPE, new NamedIndividualFrameChangedEventHandler() {
            @Override
            public void namedIndividualFrameChanged(NamedIndividualFrameChangedEvent event) {
                handleFrameChanged(event.getEntity());
            }
        });
    }

    public Set<Watch<?>> getWatches(UserId userId) {
        try {
            readLock.lock();
            return new HashSet<Watch<?>>(userId2Watch.get(checkNotNull(userId)));
        }
        finally {
            readLock.unlock();
        }
    }

    public void addWatch(Watch<?> watch, UserId userId) {
        insertWatch(watch, userId);
        appendChange(Operation.ADD, watch, userId);
        project.getEventManager().postEvent(new WatchAddedEvent(project.getProjectId(), watch, userId));
    }

    private void insertWatch(Watch<?> watch, UserId userId) {
        try {
            writeLock.lock();
            userId2Watch.put(checkNotNull(userId), checkNotNull(watch));
            watch2UserId.put(watch, userId);
            watchObject2Watch.put(watch.getWatchedObject(), watch);
        }
        finally {
            writeLock.unlock();
        }
    }

    public void removeWatch(Watch<?> watch, UserId userId) {
        try {
            writeLock.lock();
            boolean removed = uninsertWatch(watch, userId);
            if (removed) {
                appendChange(Operation.REMOVE, watch, userId);
                project.getEventManager().postEvent(new WatchRemovedEvent(project.getProjectId(), watch, userId));
            }
        }
        finally {
            writeLock.unlock();
        }
    }

    private boolean uninsertWatch(Watch<?> watch, UserId userId) {
        boolean removed;
        try {
            writeLock.lock();
            removed = userId2Watch.remove(checkNotNull(userId), checkNotNull(watch));
            watch2UserId.remove(watch, userId);
            watchObject2Watch.remove(watch.getWatchedObject(), watch);
        }
        finally {
            writeLock.unlock();
        }
        return removed;
    }

    public void clearWatches(UserId userId) {
        try {
            writeLock.lock();
            userId2Watch.removeAll(checkNotNull(userId));
        }
        finally {
            writeLock.unlock();
        }
    }

    @Override
    public Set<Watch<?>> getDirectWatches(Object watchedObject, UserId userId) {
        try {
            readLock.lock();
            return new HashSet<Watch<?>>(watchObject2Watch.get(watchedObject));
        }
        finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean hasEntityBasedWatch(OWLEntity entity, UserId userId) {
        try {
            readLock.lock();
            for (Watch<?> watch : userId2Watch.get(userId)) {
                if (watch instanceof EntityBasedWatch && ((EntityBasedWatch) watch).getEntity().equals(entity)) {
                    return true;
                }
            }
            return false;
        }
        finally {
            readLock.unlock();
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void handleFrameChanged(OWLEntity entity) {
        try {
            readLock.lock();
            List<Watch<?>> watches = new ArrayList<Watch<?>>();
            watches.addAll(watchObject2Watch.get(entity));
            for (OWLEntity anc : getRelatedWatchEntities(entity)) {
                watches.addAll(watchObject2Watch.get(anc));
            }
            for (Watch<?> watch : watches) {
                for (UserId userId : watch2UserId.get(watch)) {
                    // Dispatch
                    fireWatch(watch, userId, entity);
                }
            }
        }
        finally {
            readLock.unlock();
        }
    }


    private Set<? extends OWLEntity> getRelatedWatchEntities(OWLEntity entity) {
        return entity.accept(new OWLEntityVisitorExAdapter<Set<? extends OWLEntity>>() {
            @Override
            protected Set<? extends OWLEntity> getDefaultReturnValue(OWLEntity object) {
                return Collections.emptySet();
            }

            @Override
            public Set<? extends OWLEntity> visit(OWLClass desc) {
                return project.getClassHierarchyProvider().getAncestors(desc);
            }

            @Override
            public Set<? extends OWLEntity> visit(OWLDataProperty property) {
                return project.getDataPropertyHierarchyProvider().getAncestors(property);
            }

            @Override
            public Set<? extends OWLEntity> visit(OWLObjectProperty property) {
                return project.getObjectPropertyHierarchyProvider().getAncestors(property);
            }

            @Override
            public Set<? extends OWLEntity> visit(OWLNamedIndividual individual) {
                Set<OWLClassExpression> types = individual.getTypes(project.getRootOntology().getImportsClosure());
                Set<OWLClass> result = new HashSet<OWLClass>();
                for(OWLClassExpression ce : types) {
                    if(!ce.isAnonymous()) {
                        result.addAll(project.getClassHierarchyProvider().getAncestors(ce.asOWLClass()));
                    }
                }
                return result;
            }
        });
    }

    private void fireWatch(final Watch<?> watch, final UserId userId, final OWLEntity entity) {

        emailExecutor.submit(new Runnable() {
            @Override
            public void run() {
                UserDetails userDetails = MetaProjectManager.getManager().getUserDetails(userId);
                final String displayName = "watched project";
                final String emailSubject = String.format("Changes made in %s by %s", displayName, userDetails.getDisplayName());
                String message = "\nChanges were made to " + entity.getEntityType().getName() + " " + project.getRenderingManager().getBrowserText(entity) + " " + entity.getIRI().toQuotedString();
                message = message + (" on " + new Date() + "\n\n");
                message = message + "You can view this " + entity.getEntityType().getName() + " at the link below:";
                StringBuilder directLinkBuilder = new StringBuilder();
                directLinkBuilder.append("http://");
                directLinkBuilder.append(webProtegeProperties.getApplicationHostName());
                directLinkBuilder.append("#Edit:projectId=");
                directLinkBuilder.append(project.getProjectId().getId());
                directLinkBuilder.append(";tab=ClassesTab&id=");
                directLinkBuilder.append(URLEncoder.encode(entity.getIRI().toString()));
                message += "\n" + directLinkBuilder.toString();
                App.get().getMailManager().sendMail(userDetails.getEmailAddress().or("Not specified"), emailSubject, message);

            }
        });
    }

    @Override
    public void dispose() {
    }

    private void readWatches() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(watchFile), "utf-8"));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                int pos = line.indexOf(",");
                String opName = line.substring(0, pos);
                String watchTypeName = line.substring(pos + 1, pos = line.indexOf(",", pos + 1));
                String entityTypeName = line.substring(pos + 1, pos = line.indexOf(",", pos + 1));
                String iri = line.substring(pos + 2, pos = line.indexOf(">,"));
                String userName = line.substring(pos + 2);
                OWLEntity entity = parseEntity(entityTypeName, iri);
                final Watch<?> watch;
                if(ENTITY_FRAME_WATCH_NAME.equals(watchTypeName)) {
                    watch = new EntityFrameWatch(entity);
                }
                else {
                    watch = new HierarchyBranchWatch(entity);
                }
                final UserId userId = UserId.getUserId(userName);
                if(opName.equals(Operation.ADD.name())) {
                    insertWatch(watch, userId);
                }
                else {
                    uninsertWatch(watch, userId);
                }
            }
            bufferedReader.close();
        }
        catch (IOException e) {
            logger.severe(e);
        }

    }

    private OWLEntity parseEntity(String entityTypeName, String iri) {
        OWLEntity entity;
        if(entityTypeName.equals(EntityType.CLASS.getName())) {
            entity = project.getDataFactory().getOWLClass(IRI.create(iri));
        }
        else if(entityTypeName.equals(EntityType.OBJECT_PROPERTY.getName())) {
            entity = project.getDataFactory().getOWLObjectProperty(IRI.create(iri));
        }
        else if(entityTypeName.equals(EntityType.DATA_PROPERTY.getName())) {
            entity = project.getDataFactory().getOWLObjectProperty(IRI.create(iri));
        }
        else if(entityTypeName.equals(EntityType.ANNOTATION_PROPERTY.getName())) {
            entity = project.getDataFactory().getOWLObjectProperty(IRI.create(iri));
        }
        else if(entityTypeName.equals(EntityType.NAMED_INDIVIDUAL.getName())) {
            entity = project.getDataFactory().getOWLObjectProperty(IRI.create(iri));
        }
        else if(entityTypeName.equals(EntityType.DATATYPE.getName())) {
            entity = project.getDataFactory().getOWLObjectProperty(IRI.create(iri));
        }
        else {
            throw new RuntimeException("Invalid entity type: " + entityTypeName);
        }
        return entity;
    }

    private synchronized void appendChange(Operation operation, Watch<?> watch, UserId userId) {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(watchFile, true)));
            pw.print(operation);
            pw.print(",");
            if(watch instanceof EntityFrameWatch) {
                pw.print(ENTITY_FRAME_WATCH_NAME);
                pw.print(",");
                final EntityFrameWatch entityWatch = (EntityFrameWatch) watch;
                pw.print(entityWatch.getEntity().getEntityType().getName());
                pw.print(",");
                pw.print((entityWatch.getEntity().getIRI().toQuotedString()));
            }
            else if(watch instanceof HierarchyBranchWatch) {
                pw.print(HIERARCHY_BRANCH_WATCH_NAME);
                pw.print(",");
                final HierarchyBranchWatch entityWatch = (HierarchyBranchWatch) watch;
                pw.print(entityWatch.getEntity().getEntityType().getName());
                pw.print(",");
                pw.print((entityWatch.getEntity().getIRI().toQuotedString()));
            }
            else {
                throw new RuntimeException("Unknown watch type: " + watch);
            }
            pw.print(",");
            pw.print(userId.getUserName());
            pw.println();
            pw.close();
        }
        catch (IOException e) {
            logger.severe(e);
        }
    }


    private enum Operation {
        ADD,
        REMOVE
    }
}
