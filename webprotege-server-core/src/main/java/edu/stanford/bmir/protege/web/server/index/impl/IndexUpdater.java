package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.IndexUpdatingService;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@ProjectSingleton
public class IndexUpdater {

    private static final Logger logger = LoggerFactory.getLogger(IndexUpdater.class);

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final ProjectOntologiesIndexImpl projectOntologiesIndex;

    private final Collection<RequiresOntologyChangeNotification> indexes;

    @Nonnull
    private final ExecutorService indexUpdaterService;

    @Nonnull
    private final ProjectId projectId;

    private boolean builtIndexes = false;

    @AutoFactory
    @Inject
    public IndexUpdater(@Provided @Nonnull RevisionManager revisionManager,
                        @Provided @Nonnull ProjectOntologiesIndexImpl projectOntologiesIndex,
                        @Provided @Nonnull Set<RequiresOntologyChangeNotification> indexes,
                        @Provided @Nonnull @IndexUpdatingService ExecutorService indexUpdaterService,
                        @Provided @Nonnull ProjectId projectId) {
        this.revisionManager = checkNotNull(revisionManager);
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.indexes = sortedIndexes(projectId, indexes);
        this.indexUpdaterService = checkNotNull(indexUpdaterService);
        this.projectId = checkNotNull(projectId);
    }

    private static ImmutableList<RequiresOntologyChangeNotification> sortedIndexes(
            @Nonnull ProjectId projectId,
            @Nonnull Collection<RequiresOntologyChangeNotification> indexes) {
        var comparator = Comparator.comparing(index -> index instanceof DependentIndex)
                                   .reversed()
                                   .thenComparing(index -> index instanceof ProjectOntologiesIndex)
                                   .reversed();

        return indexes.stream()
                      .sorted(comparator)
                      .peek(index -> logger.info("{} Index {}",
                                                 projectId,
                                                 index.getClass()
                                                      .getSimpleName()))
                      .collect(toImmutableList());

    }

    public synchronized void buildIndexes() {
        if(builtIndexes) {
            return;
        }
        builtIndexes = true;
        var revisions = revisionManager.getRevisions();
        var revisionChanges = revisions.stream()
                                       .map(Revision::getChanges)
                                       .collect(toImmutableList());
        updateIndexesWithRevisions(revisionChanges);
    }

    private synchronized void updateIndexesWithRevisions(ImmutableList<ImmutableList<OntologyChange>> revisions) {
        try {
            var stopwatch = Stopwatch.createStarted();
            var countDownLatch = new CountDownLatch(indexes.size());
            indexes.forEach(index -> buildIndex(index, revisions, countDownLatch));
            countDownLatch.await();
            stopwatch.stop();
            logger.info("{} Built indexes in {} ms",
                        projectId,
                        stopwatch.elapsed()
                                 .toMillis());
        } catch(InterruptedException e) {
            logger.error("{} Interrupted while building indexes", projectId);
        }
    }

    private void buildIndex(@Nonnull RequiresOntologyChangeNotification index,
                            @Nonnull ImmutableList<ImmutableList<OntologyChange>> revisions,
                            @Nonnull CountDownLatch countDownLatch) {
        var updaterTask = new IndexUpdaterTask(projectId, index, revisions, countDownLatch);
        updaterTask.run();
    }

    public synchronized void updateIndexes(ImmutableList<OntologyChange> changes) {
        updateIndexesWithRevisions(ImmutableList.of(changes));
    }


    private static class IndexUpdaterTask implements Runnable {

        private static final Logger logger = LoggerFactory.getLogger(IndexUpdaterTask.class);

        private final ProjectId projectId;

        @Nonnull
        private final RequiresOntologyChangeNotification index;

        @Nonnull
        private final ImmutableList<ImmutableList<OntologyChange>> revisions;

        @Nonnull
        private final CountDownLatch countDownLatch;

        public IndexUpdaterTask(ProjectId projectId,
                                RequiresOntologyChangeNotification index,
                                @Nonnull ImmutableList<ImmutableList<OntologyChange>> revisions,
                                @Nonnull CountDownLatch countDownLatch) {
            this.projectId = checkNotNull(projectId);
            this.index = checkNotNull(index);
            this.revisions = checkNotNull(revisions);
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            var stopwatch = Stopwatch.createStarted();
            revisions.forEach(index::applyChanges);
            countDownLatch.countDown();
            stopwatch.stop();
            logger.info("{}    Built {} in {} ms",
                        projectId,
                        index.getClass()
                             .getSimpleName(),
                        stopwatch.elapsed()
                                 .toMillis());
        }
    }

}
