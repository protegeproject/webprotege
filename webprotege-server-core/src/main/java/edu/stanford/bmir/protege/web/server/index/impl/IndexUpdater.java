package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.DependentIndex;
import edu.stanford.bmir.protege.web.server.index.IndexUpdatingService;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.lang.management.ManagementFactory;
import java.util.Collection;
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

    private final Collection<UpdatableIndex> indexes;

    @Nonnull
    private final ExecutorService indexUpdaterService;

    @Nonnull
    private final ProjectId projectId;

    private boolean builtIndexes = false;

    @AutoFactory
    @Inject
    public IndexUpdater(@Provided @Nonnull RevisionManager revisionManager,
                        @Provided @Nonnull Set<UpdatableIndex> indexes,
                        @Provided @Nonnull @IndexUpdatingService ExecutorService indexUpdaterService,
                        @Provided @Nonnull ProjectId projectId) {
        this.revisionManager = checkNotNull(revisionManager);
        this.indexes = checkNotNull(indexes);
        this.indexUpdaterService = checkNotNull(indexUpdaterService);
        this.projectId = checkNotNull(projectId);
    }

    private Multimap<Integer, UpdatableIndex> getRankedIndexes() {
        var graph = GraphBuilder.directed().allowsSelfLoops(false).<UpdatableIndex>build();
        for(var index : indexes) {
            graph.addNode(index);
            if(index instanceof DependentIndex) {
                var dependentIndex = (DependentIndex) index;
                dependentIndex.getDependencies()
                              .stream()
                              .map(dep -> (UpdatableIndex) dep)
                              .forEach(dependency -> graph.putEdge(index, dependency));
            }
        }
        var rankMap = HashMultimap.<Integer, UpdatableIndex>create();
        for(var index : indexes) {
            var rank = getRank(index, graph);
            rankMap.put(rank, index);
        }
        return rankMap;
    }

    private int getRank(UpdatableIndex index, Graph<UpdatableIndex> depGraph) {
        int depth = 0;
        for(var successor : depGraph.successors(index)) {
            int successorDepth = 1 + getRank(successor, depGraph);
            if(successorDepth > depth) {
                depth = successorDepth;
            }
        }
        return depth;
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
        var rankedIndexes = getRankedIndexes();
        var ranks = rankedIndexes.keySet().size();
        for(var rank = 0; rank < ranks; rank++) {
            var rankIndexes = rankedIndexes.get(rank);
            buildIndexesWithRevisions(rank, rankIndexes, revisions);
        }

    }

    private void buildIndexesWithRevisions(int rank,
                                           Collection<UpdatableIndex> indexes,
                                           ImmutableList<ImmutableList<OntologyChange>> revisions) {
        try {
            var stopwatch = Stopwatch.createStarted();
            var countDownLatch = new CountDownLatch(indexes.size());
            indexes.forEach(index -> buildIndex(rank, index, revisions, countDownLatch));
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


    private void buildIndex(int rank,
                            @Nonnull UpdatableIndex index,
                            @Nonnull ImmutableList<ImmutableList<OntologyChange>> revisions,
                            @Nonnull CountDownLatch countDownLatch) {
        var updaterTask = new IndexUpdaterTask(projectId, rank, index, revisions, countDownLatch);
        indexUpdaterService.submit(updaterTask);
    }

    public synchronized void updateIndexes(ImmutableList<OntologyChange> changes) {
        updateIndexesWithRevisions(ImmutableList.of(changes));
    }


    private static class IndexUpdaterTask implements Runnable {

        private static final Logger logger = LoggerFactory.getLogger(IndexUpdaterTask.class);

        private final ProjectId projectId;

        private final int rank;

        @Nonnull
        private final UpdatableIndex index;

        @Nonnull
        private final ImmutableList<ImmutableList<OntologyChange>> revisions;

        @Nonnull
        private final CountDownLatch countDownLatch;

        public IndexUpdaterTask(ProjectId projectId,
                                int rank,
                                UpdatableIndex index,
                                @Nonnull ImmutableList<ImmutableList<OntologyChange>> revisions,
                                @Nonnull CountDownLatch countDownLatch) {
            this.projectId = checkNotNull(projectId);
            this.rank = rank;
            this.index = checkNotNull(index);
            this.revisions = checkNotNull(revisions);
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            var stopwatch = Stopwatch.createStarted();
            var indexName = index.getClass().getSimpleName();
            var threadMxBean = ManagementFactory.getThreadMXBean();
            var cpu0 = threadMxBean.getCurrentThreadCpuTime();
            revisions.forEach(index::applyChanges);
            countDownLatch.countDown();
            var cpu1 = threadMxBean.getCurrentThreadCpuTime();
            var cpuTimeMs = (cpu1 - cpu0) / (1_000_000);
            stopwatch.stop();
            logger.info("{}    Built {} in {} ms of user-time ({} ms wall-clock)",
                        projectId,
                        indexName,
                        cpuTimeMs,
                        stopwatch.elapsed()
                                 .toMillis());
        }
    }

}
