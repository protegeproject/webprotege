package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.protege.gwt.graphtree.shared.graph.GraphNode;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GetHierarchyRootsResult implements Result {

    @NotNull
    public static GetHierarchyRootsResult get(@Nonnull List<GraphNode<EntityNode>> rootNodes) {
        return new AutoValue_GetHierarchyRootsResult(ImmutableList.copyOf(rootNodes));
    }

    @NotNull
    public static GetHierarchyRootsResult empty() {
        return get(ImmutableList.of());
    }

    @Nonnull
    public abstract ImmutableList<GraphNode<EntityNode>> getRootNodes();
}
