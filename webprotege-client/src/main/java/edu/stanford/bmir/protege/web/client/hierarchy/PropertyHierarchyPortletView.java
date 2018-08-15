package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.protege.gwt.graphtree.client.TreeWidget;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 4 Dec 2017
 */
public interface PropertyHierarchyPortletView extends IsWidget {

    interface HierarchyIdSelectedHandler {
        void handleHierarchyIdSelected(@Nonnull HierarchyId hierarchyId);
    }

    void addHierarchy(@Nonnull HierarchyId hierarchyId,
                      @Nonnull String label,
                      @Nonnull TreeWidget<EntityNode, OWLEntity> view);

    void setHierarchyIdSelectedHandler(@Nonnull HierarchyIdSelectedHandler hierarchySwitchedHandler);

    void setSelectedHierarchy(@Nonnull HierarchyId hierarchyId);

    Optional<HierarchyId> getSelectedHierarchyId();

    Optional<TreeWidget<EntityNode, OWLEntity>> getSelectedHierarchy();

}
