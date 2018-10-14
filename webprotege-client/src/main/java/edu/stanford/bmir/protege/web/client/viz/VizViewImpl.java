package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.JSON;
import edu.stanford.bmir.protege.web.client.graphlib.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class VizViewImpl extends Composite implements VizView {

    private static final double DEFAULT_RANK_SEP = 0.4;

    interface VizViewImplUiBinder extends UiBinder<HTMLPanel, VizViewImpl> {

    }

    private static VizViewImplUiBinder ourUiBinder = GWT.create(VizViewImplUiBinder.class);

    @UiField
    HTML imageContainer;

    @UiField
    FocusPanel focusPanel;

    @UiField
    ListBox ranksepListBox;

    @UiField
    Button downloadButton;

    private SettingsChangedHandler settingsChangedHandler = () -> {};

    @Inject
    public VizViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        ranksepListBox.setSelectedIndex(3);
        ranksepListBox.addChangeHandler(event -> settingsChangedHandler.handleSettingsChanged());

        Graph g = Graph.create();
        NodeDetails a = new NodeDetails("NodeA", 333, 444, "Hello");
        g.addNode(a);
        NodeDetails b = new NodeDetails("NodeB", 343, 321, "World");
        g.addNode(b);
        g.addEdge(a, b, new EdgeDetails("Edge-A-B"));
        NodeDetails c = new NodeDetails("NodeC", 300, 100, "!!!");
        g.addNode(c);
        g.addEdge(b, c, new EdgeDetails("Edge-B-C"));

        g.layout();


        GWT.log("[VizDagre] Stringify: " + a.stringify());
        g.getNodes().forEach(nd -> GWT.log("[VizDagre] Node: " + nd.stringify()));
//        Stream.of(g.ed())
//                .forEach(ek -> GWT.log("[VizDagre] EK: " + JSON.stringify(ek) + " --- " + g.edge(ek).stringify()));
        g.getEdges().forEach(e ->
                             {
                                 GWT.log("[VizDagre] Edge: " + e.stringify());
                                 String pts = e.getPoints().map(JSON::stringify).collect(joining("; "));
                                 GWT.log("[VizDagre]     Points: " + pts);
                             });

        //        edge.getPointsList().forEach(p -> GWT.log("[VizDagre] " + p.getX() + ", " + p.getY()));

        GWT.log("[VizDagre] G: " + JSON.stringify(g));
        GWT.log("[VizDagre] G width: " + g.getWidth());
        GWT.log("[VizDagre] G height: " + g.getHeight());
        GWT.log("[VizDagre] G acyclic: " + g.isAcyclic());
        GWT.log("[VizDagre] G node count: " + g.getNodeCount());
        GWT.log("[VizDagre] G edge count: " + g.getEdgeCount());
        GWT.log("[VizDagre] G sources: " + g.getSources().map(n -> n.stringify()).collect(joining(" ")));
        GWT.log("[VizDagre] G sinks: " + g.getSinks().map(n -> n.stringify()).collect(joining(" ")));
        GWT.log("[VizDagre] G successors: " + g.getSuccessors(a.getId()).map(n -> n.stringify()).collect(joining(" ")));
        GWT.log("[VizDagre] G predecessors: " + g.getPredecessors(c.getId()).map(n -> n.stringify()).collect(joining(" ")));


        GraphLibAlgorithm.getNodesInPreorder(g, a)
                .forEach(nd -> GWT.log("[VizDagre] PO Node: " + nd.stringify()));

        GWT.log("[VizDagre] G json: " + g.writeJson());

    }

    @Override
    public void setRendering(@Nonnull String rendering) {
        imageContainer.getElement().setInnerHTML(checkNotNull(rendering));
    }

    @Override
    public double getRankSpacing() {
        String value = ranksepListBox.getSelectedValue();
        if(value.isEmpty()) {
            return DEFAULT_RANK_SEP;
        }
        try {
            return DEFAULT_RANK_SEP * Double.parseDouble(value);
        } catch (NumberFormatException e){
            return DEFAULT_RANK_SEP;
        }
    }

    @Override
    public void setSettingsChangedHandler(@Nonnull SettingsChangedHandler handler) {
        this.settingsChangedHandler = checkNotNull(handler);
    }

    @UiHandler("downloadButton")
    public void downloadButtonClick(ClickEvent event) {
        GWT.log(imageContainer.getElement().getInnerHTML());
        DownloadSvg saver = new DownloadSvg();
        saver.save(imageContainer.getElement(), "entity-graph");
    }
}