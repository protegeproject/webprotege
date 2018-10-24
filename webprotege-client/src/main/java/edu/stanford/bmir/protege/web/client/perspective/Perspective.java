package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.perspective.HasPerspectiveId;
import edu.stanford.protege.widgetmap.client.HasRootNode;
import edu.stanford.protege.widgetmap.client.RootNodeChangedHandler;
import edu.stanford.protege.widgetmap.shared.node.Node;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public interface Perspective extends IsWidget, HasRootNode, RequiresResize, HasDispose, HasPerspectiveId {

    void setRootNode(Optional<Node> rootNode);

    void setRootNodeChangedHandler(RootNodeChangedHandler handler);

    void setNodePropertiesChangedHandler(@Nonnull Consumer<TerminalNode> nodePropertiesChangedHandler);

    void dropView(String className);

    void setEmptyPerspectiveWidget(IsWidget widget);

    void setViewsCloseable(boolean closeable);

}
