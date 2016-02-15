package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class DummyPerspectiveLinkManager implements PerspectiveLinkManager {

    private Set<PerspectiveId> linkedPerspectives = Sets.newLinkedHashSet();

    @Inject
    public DummyPerspectiveLinkManager() {
        linkedPerspectives.add(new PerspectiveId("Classes"));
        linkedPerspectives.add(new PerspectiveId("Properties"));
        linkedPerspectives.add(new PerspectiveId("Individuals"));
    }

    public void getLinkedPerspectives(final Callback callback) {
        Timer t = new Timer() {
            /**
             * This method will be called when a timer fires. Override it to implement the
             * timer's logic.
             */
            @Override
            public void run() {
                callback.handlePerspectives(Lists.newArrayList(linkedPerspectives));
            }
        };
        t.schedule(1);
    }

    public void removeLinkedPerspective(PerspectiveId perspectiveId, Callback callback) {
        linkedPerspectives.remove(perspectiveId);
        getLinkedPerspectives(callback);
    }

    public void addLinkedPerspective(PerspectiveId perspectiveId, Callback callback) {
        linkedPerspectives.add(perspectiveId);
        getLinkedPerspectives(callback);
    }
}
