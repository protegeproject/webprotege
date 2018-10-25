package edu.stanford.bmir.protege.web.client.viz;

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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public interface VizView extends IsWidget {

    Element getSvgElement();

    void clearGraph();

    Optional<NodeDetails> getMostRecentTargetNode();

    void displayLargeGraphMessage(OWLEntityData rootEntity, int nodes, int edges,
                                  @Nonnull Runnable displayGraphCallback);

    void setGraph(@Nonnull OWLEntity rootEntity, @Nonnull Graph graph);

    void updateGraph(Graph graph);

    void setLoadHandler(Runnable handler);

    void setDownloadHandler(@Nonnull DownloadHandler handler);

    boolean isVisible();

    double getRankSpacing();

    void setSettingsChangedHandler(@Nonnull SettingsChangedHandler handler);

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

    interface DownloadHandler {

        void handleDownload();
    }

    interface SettingsChangedHandler {

        void handleSettingsChanged();
    }
}
