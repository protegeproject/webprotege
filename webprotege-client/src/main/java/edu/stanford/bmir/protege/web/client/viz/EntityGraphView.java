package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.graphlib.Graph;
import edu.stanford.bmir.protege.web.client.graphlib.NodeDetails;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import elemental.dom.Element;
import elemental.events.Event;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-12
 */
public interface EntityGraphView extends IsWidget {


    @Nonnull
    AcceptsOneWidget getFilterContainer();

    Element getSvgElement();

    void clearGraph();

    Optional<NodeDetails> getMostRecentTargetNode();

    void setGraph(@Nonnull OWLEntity rootEntity, @Nonnull Graph graph);

    void setPrunedToLimit(boolean b);

    void updateGraph(Graph graph);

    void setDownloadHandler(@Nonnull EntityGraphPresenter.DownloadHandler handler);

    void setViewSettingsHandler(@Nonnull EntityGraphPresenter.ViewSettingsHandler handler);

    boolean isVisible();

    @Nonnull
    TextMeasurer getTextMeasurer();

    void setNodeClickHandler(@Nonnull Consumer<NodeDetails> nodeClickHandler);

    void setNodeDoubleClickHandler(@Nonnull Consumer<NodeDetails> nodeDoubleClickHandler);

    void setNodeContextMenuClickHandler(@Nonnull Consumer<NodeDetails> nodeContextMenuClickHandler);

    void addContextMenuAction(@Nonnull UIAction uiAction);

    void setNodeMouseOverHandler(BiConsumer<NodeDetails, Event> nodeMouseOverHandler);

    void setNodeMouseOutHandler(BiConsumer<NodeDetails, Event> nodeMouseOverHandler);

    void setNodeMouseEnterHandler(BiConsumer<NodeDetails, Event> nodeMouseEnterHandler);

    void setNodeMouseLeaveHandler(BiConsumer<NodeDetails, Event> nodeMouseLeaveHandler);
}
