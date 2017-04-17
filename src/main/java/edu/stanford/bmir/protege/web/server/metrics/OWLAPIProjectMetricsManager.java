package edu.stanford.bmir.protege.web.server.metrics;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;
import edu.stanford.bmir.protege.web.shared.metrics.MetricsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class OWLAPIProjectMetricsManager {

    public final WebProtegeLogger logger;

    private List<MetricCalculator> metrics = Lists.newArrayList();

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private Map<MetricCalculator, MetricValue> valueCache = Maps.newLinkedHashMap();

    private Set<MetricCalculator> dirtyMetrics = Sets.newHashSet();

    private HasPostEvents<ProjectEvent<?>> eventBus;

    private ProjectId projectId;

    @Inject
    public OWLAPIProjectMetricsManager(ProjectId projectId,
                                       List<MetricCalculator> metrics,
                                       HasPostEvents<ProjectEvent<?>> eventBus,
                                       WebProtegeLogger logger) {
        this.projectId = projectId;
        this.logger = logger;
        this.eventBus = eventBus;
        this.metrics.addAll(metrics);
        markAllAsDirty();
    }

    private void markAllAsDirty() {
        dirtyMetrics.addAll(metrics);
    }

    public void handleOntologyChanges(List<? extends OWLOntologyChange> changes) {
        try {
            writeLock.lock();
            for(MetricCalculator metric : metrics) {
                if(metric.getStateAfterChanges(changes) == OWLAPIProjectMetricState.DIRTY) {
                    dirtyMetrics.add(metric);
                }
            }
            if(!dirtyMetrics.isEmpty()) {
                eventBus.postEvent(new MetricsChangedEvent(projectId));
            }
        }
        finally {
            writeLock.unlock();
        }
    }

    private void recomputeDirtyMetrics() {
        try {
            writeLock.lock();
            for(Iterator<MetricCalculator> metricIt = dirtyMetrics.iterator(); metricIt.hasNext(); ) {
                MetricCalculator metric = metricIt.next();
                metricIt.remove();
                try {
                    MetricValue metricValue = metric.computeValue();
                    valueCache.put(metric, metricValue);
                } catch (Exception e) {
                    logger.error(e);
                    // Mark as not computed
                    valueCache.put(metric, null);
                }
            }
        } finally {
            writeLock.lock();
        }
    }

    public List<MetricValue> getMetrics() {
        logger.info("getMetrics()");
        Stopwatch stopwatch = Stopwatch.createStarted();
        recomputeDirtyMetrics();
        List<MetricValue> result = readMetrics();
        long ms = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        logger.info("getMetrics().  Retrieved metrics in %d ms", ms);
        return result;
    }

    private List<MetricValue> readMetrics() {
        readLock.lock();
        try {
            List<MetricValue> result = Lists.newArrayList();
            for(MetricCalculator metric : metrics) {
                MetricValue value = valueCache.get(metric);
                if (value != null) {
                    result.add(value);
                }
            }
            return result;
        }
        finally {
            readLock.unlock();
        }
    }
}
