package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.inject.project.WatchFile;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.EntityFrameWatch;
import edu.stanford.bmir.protege.web.shared.watches.HierarchyBranchWatch;
import edu.stanford.bmir.protege.web.shared.watches.UserWatch;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityProvider;

import javax.inject.Inject;
import java.io.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
@ProjectSingleton
public class WatchStoreImpl implements WatchStore {



    private static final String ENTITY_FRAME_WATCH_NAME = "EntityFrameWatch";

    private static final String HIERARCHY_BRANCH_WATCH_NAME = "HierarchyBranchWatch";

    private final Set<UserWatch<?>> watches = new HashSet<>();

    private Lock lock = new ReentrantLock();

    private boolean read = false;

    private final File watchFile;

    private final WebProtegeLogger logger;

    @Inject
    public WatchStoreImpl(@WatchFile File watchFile, OWLEntityProvider entityProvider, WebProtegeLogger logger) {
        this.watchFile = checkNotNull(watchFile);
        this.entityProvider = checkNotNull(entityProvider);
        this.logger = logger;
    }

    private final OWLEntityProvider entityProvider;


    @Override
    public void addWatch(UserWatch<?> userWatch) {
        appendChange(Operation.ADD, userWatch);
    }

    @Override
    public void removeWatch(UserWatch<?> userWatch) {
        appendChange(Operation.REMOVE, userWatch);
    }

    @Override
    public Set<UserWatch<?>> getWatches() {
        try {
            lock.lock();
            if(!read) {
                watches.addAll(readWatches());
                read = true;
            }
            return new HashSet<>(watches);
        } finally {
            lock.unlock();
        }
    }

    private Set<UserWatch<?>> readWatches() {
        try {
            if(!watchFile.exists()) {
                return Collections.emptySet();
            }
            Set<UserWatch<?>> result = new HashSet<>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(watchFile), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int pos = line.indexOf(",");
                String opName = line.substring(0, pos);
                String watchTypeName = line.substring(pos + 1, pos = line.indexOf(",", pos + 1));
                String entityTypeName = line.substring(pos + 1, pos = line.indexOf(",", pos + 1));
                String iri = line.substring(pos + 2, pos = line.indexOf(">,"));
                String userName = line.substring(pos + 2);
                OWLEntity entity = parseEntity(entityTypeName, iri);
                final Watch<?> watch;
                if (ENTITY_FRAME_WATCH_NAME.equals(watchTypeName)) {
                    watch = new EntityFrameWatch(entity);
                } else {
                    watch = new HierarchyBranchWatch(entity);
                }
                final UserId userId = UserId.getUserId(userName);
                if (opName.equals(Operation.ADD.name())) {
                    result.add(new UserWatch<>(userId, watch));
                } else {
                    result.remove(new UserWatch<>(userId, watch));
                }
            }
            bufferedReader.close();
            return result;
        } catch (IOException e) {
            logger.error(e);
            return Collections.emptySet();
        }
    }

    private OWLEntity parseEntity(String entityTypeName, String iri) {
        OWLEntity entity;
        if (entityTypeName.equals(EntityType.CLASS.getName())) {
            entity = entityProvider.getOWLClass(IRI.create(iri));
        } else if (entityTypeName.equals(EntityType.OBJECT_PROPERTY.getName())) {
            entity = entityProvider.getOWLObjectProperty(IRI.create(iri));
        } else if (entityTypeName.equals(EntityType.DATA_PROPERTY.getName())) {
            entity = entityProvider.getOWLObjectProperty(IRI.create(iri));
        } else if (entityTypeName.equals(EntityType.ANNOTATION_PROPERTY.getName())) {
            entity = entityProvider.getOWLObjectProperty(IRI.create(iri));
        } else if (entityTypeName.equals(EntityType.NAMED_INDIVIDUAL.getName())) {
            entity = entityProvider.getOWLObjectProperty(IRI.create(iri));
        } else if (entityTypeName.equals(EntityType.DATATYPE.getName())) {
            entity = entityProvider.getOWLObjectProperty(IRI.create(iri));
        } else {
            throw new RuntimeException("Invalid entity type: " + entityTypeName);
        }
        return entity;
    }


    private synchronized void appendChange(Operation operation, UserWatch<?> userWatch) {
        try {
            lock.lock();
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(watchFile, true)));
                pw.print(operation);
                pw.print(",");
                Watch<?> watch = userWatch.getWatch();
                if (watch instanceof EntityFrameWatch) {
                    pw.print(ENTITY_FRAME_WATCH_NAME);
                    pw.print(",");
                    final EntityFrameWatch entityWatch = (EntityFrameWatch) watch;
                    pw.print(entityWatch.getEntity().getEntityType().getName());
                    pw.print(",");
                    pw.print((entityWatch.getEntity().getIRI().toQuotedString()));
                } else if (watch instanceof HierarchyBranchWatch) {
                    pw.print(HIERARCHY_BRANCH_WATCH_NAME);
                    pw.print(",");
                    final HierarchyBranchWatch entityWatch = (HierarchyBranchWatch) watch;
                    pw.print(entityWatch.getEntity().getEntityType().getName());
                    pw.print(",");
                    pw.print((entityWatch.getEntity().getIRI().toQuotedString()));
                } else {
                    throw new RuntimeException("Unknown watch type: " + watch);
                }
                pw.print(",");
                UserId userId = userWatch.getUserId();
                pw.print(userId.getUserName());
                pw.println();
                pw.close();
            } catch (IOException e) {
                logger.error(e);
            }
        } finally {
            lock.unlock();
        }
    }


    private enum Operation {
        ADD,
        REMOVE
    }

}
