package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.graphlib.*;
import elemental.dom.Element;

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

    private static final double ZOOM_DELTA = 0.05;

    @Nonnull
    private Runnable loadHandler = () -> {};

    interface VizViewImplUiBinder extends UiBinder<HTMLPanel, VizViewImpl> {

    }

    private static VizViewImplUiBinder ourUiBinder = GWT.create(VizViewImplUiBinder.class);

    @UiField
    HTML viewPort;

    @UiField
    FocusPanel focusPanel;

    @UiField
    ListBox ranksepListBox;

    @UiField
    Button downloadButton;

    @UiField
    TextMeasurerImpl textMeasurer;

    private double scaleFactor = 1.0;

    private int viewportWidth = 0;

    private int viewportHeight = 0;

    private SettingsChangedHandler settingsChangedHandler = () -> {};

    @Inject
    public VizViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        ranksepListBox.setSelectedIndex(1);
        ranksepListBox.addChangeHandler(event -> settingsChangedHandler.handleSettingsChanged());
        focusPanel.addKeyDownHandler(this::handleKeyDown);
    }

    private void handleKeyDown(KeyDownEvent event) {
        GWT.log("[VizViewImpl] KeyDown " + event.getNativeKeyCode());
        if(event.getNativeKeyCode() == 187) {
            event.preventDefault();
            event.stopPropagation();
            handleZoomIn();
        }
        else if(event.getNativeKeyCode() == 189) {
            event.preventDefault();
            event.stopPropagation();
            handleZoomOut();
        }
        else if(event.getNativeKeyCode() == 48) {
            resetZoomLevel();
        }
    }

    private void handleZoomIn() {
        double sf = getScaleFactor();
        sf += ZOOM_DELTA;
        setScaleFactor(sf);
    }

    private void handleZoomOut() {
        double sf = getScaleFactor();
        if(sf <= ZOOM_DELTA) {
            return;
        }
        sf -= ZOOM_DELTA;
        setScaleFactor(sf);
    }

    private void resetZoomLevel() {
        setScaleFactor(1);
    }

    @Override
    public void setLoadHandler(Runnable handler) {
        this.loadHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public TextMeasurer getTextMeasurer() {
        return textMeasurer;
    }

    @Override
    public void setGraph(Graph graph) {
        viewportWidth = graph.getWidth();
        viewportHeight = graph.getHeight();
        updateViewPortDimensions();
        Graph2Svg graph2Svg = new Graph2Svg(textMeasurer);
        Element svg = graph2Svg.convertToSvg(graph);
        viewPort.getElement().setInnerHTML(svg.getOuterHTML());
    }

    @Override
    public double getScaleFactor() {
        return scaleFactor;
    }

    @Override
    public void setScaleFactor(double scaleFactor) {
        if (scaleFactor != this.scaleFactor) {
            this.scaleFactor = scaleFactor;
            updateViewPortDimensions();
        }
    }

    private void updateViewPortDimensions() {
        viewPort.setWidth(viewportWidth * scaleFactor + "px");
        viewPort.setHeight(viewportHeight * scaleFactor + "px");
    }

    @Override
    public double getRankSpacing() {
        String value = ranksepListBox.getSelectedValue();
        try {
            return 2 * Double.parseDouble(value);
        } catch (NumberFormatException e){
            return 2.0;
        }
    }

    @Override
    public void setSettingsChangedHandler(@Nonnull SettingsChangedHandler handler) {
        this.settingsChangedHandler = checkNotNull(handler);
    }

    @UiHandler("downloadButton")
    public void downloadButtonClick(ClickEvent event) {
        DownloadSvg saver = new DownloadSvg();
        Element e = (Element) viewPort.getElement();
        saver.save(e, "entity-graph");
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        loadHandler.run();
    }
}