package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
@ProjectSingleton
public class IndexUpdater {

    private static final Logger logger = LoggerFactory.getLogger(IndexUpdater.class);

    private final LinkedHashSet<RequiresOntologyChangeNotification> listeners = new LinkedHashSet<>();

    @Inject
    public IndexUpdater() {
    }

    public synchronized void registerIndex(@Nonnull RequiresOntologyChangeNotification requiresOntologyChangeNotification) {
        checkNotNull(requiresOntologyChangeNotification);
        listeners.add(requiresOntologyChangeNotification);
    }

    public synchronized void propagateOntologyChanges(List<OntologyChange> changes) {
        listeners.forEach(listener -> {
            try {
                listener.applyChanges(ImmutableList.copyOf(changes));
            } catch(Exception e) {
                logger.error("Index threw exception while updating", e  );
            }
        });
    }

}
