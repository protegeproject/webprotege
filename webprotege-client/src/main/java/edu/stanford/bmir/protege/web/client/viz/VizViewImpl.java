package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.graphlib.*;
import elemental.dom.Element;
import elemental.dom.Node;
import elemental.dom.NodeList;

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

    private DownloadHandler downloadHandler = () -> {};

    interface VizViewImplUiBinder extends UiBinder<HTMLPanel, VizViewImpl> {

    }

    private static VizViewImplUiBinder ourUiBinder = GWT.create(VizViewImplUiBinder.class);

    @UiField
    HTMLPanel canvas;

    @UiField
    FocusPanel viewPort;

    @UiField
    ListBox ranksepListBox;

    @UiField
    Button downloadButton;

    @UiField
    TextMeasurerImpl textMeasurer;

    private double scaleFactor = 1.0;

    private int canvasWidth = 0;

    private int canvasHeight = 0;

    private SettingsChangedHandler settingsChangedHandler = () -> {};

    @Inject
    public VizViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        ranksepListBox.setSelectedIndex(1);
        ranksepListBox.addChangeHandler(event -> settingsChangedHandler.handleSettingsChanged());
        viewPort.addKeyDownHandler(this::handleKeyDown);
    }

    private void handleKeyDown(KeyDownEvent event) {
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

    @Override
    public void setDownloadHandler(@Nonnull DownloadHandler handler) {
        this.downloadHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public TextMeasurer getTextMeasurer() {
        return textMeasurer;
    }

    @Override
    public Element getSvgElement() {
        return (Element) canvas.getElement().getElementsByTagName("svg").getItem(0);
    }

    @Override
    public void setGraph(Graph graph) {
        Graph2Svg graph2Svg = new Graph2Svg(textMeasurer);
        Element svg = graph2Svg.createSvg(graph);
        Element canvasElement = (Element) canvas.getElement();
        NodeList childNodes = canvasElement.getChildNodes();
        while(childNodes.getLength() > 0) {
            canvasElement.removeChild(childNodes.item(0));
        }
        canvas.getElement().appendChild((com.google.gwt.dom.client.Element) svg);
        canvasWidth = graph.getWidth();
        canvasHeight = graph.getHeight();
        updateCanvasDimensions();
    }

    @Override
    public double getScaleFactor() {
        return scaleFactor;
    }

    @Override
    public void setScaleFactor(double scaleFactor) {
        if (scaleFactor != this.scaleFactor) {
            this.scaleFactor = scaleFactor;
            updateCanvasDimensions();
        }
    }

    private void updateCanvasDimensions() {
        double percentageX = getHorizontalScrollPercentage();
        double percentageY = getVerticalScrollPercentage();

        canvas.setWidth(canvasWidth * scaleFactor + "px");
        canvas.setHeight(canvasHeight * scaleFactor + "px");

        setHorizontalScrollPercentage(percentageX);
        setVerticalScrollPercentage(percentageY);
    }

    private void setHorizontalScrollPercentage(double percentage) {
        Element viewPortElement = (Element) viewPort.getElement();
        viewPortElement.setScrollLeft((int) (percentage * getHorizontalScrollDistance()));
    }

    private void setVerticalScrollPercentage(double percentage) {
        Element viewPortElement = (Element) viewPort.getElement();
        viewPortElement.setScrollTop((int) (percentage * getVerticalScrollDistance()));
    }

    private double getHorizontalScrollPercentage() {
        Element viewPortElement = (Element) viewPort.getElement();
        Element canvasElement = (Element) canvas.getElement();
        if(canvasElement.getClientWidth() < viewPortElement.getClientWidth()) {
            return 0.5;
        }
        double scrollX = viewPortElement.getScrollLeft();
        return scrollX / getHorizontalScrollDistance();
    }

    private double getVerticalScrollPercentage() {
        Element viewPortElement = (Element) viewPort.getElement();
        Element canvasElement = (Element) canvas.getElement();
        if(canvasElement.getClientHeight() < viewPortElement.getClientHeight()) {
            return 0.5;
        }
        double scrollY = viewPortElement.getScrollTop();
        return scrollY / getVerticalScrollDistance();
    }

    private int getVerticalScrollDistance() {
        Element viewPortElement = (Element) viewPort.getElement();
        Element canvasElement = (Element) canvas.getElement();
        return canvasElement.getScrollHeight() - viewPortElement.getClientHeight();
    }

    private int getHorizontalScrollDistance() {
        Element viewPortElement = (Element) viewPort.getElement();
        Element canvasElement = (Element) canvas.getElement();
        return canvasElement.getScrollWidth() - viewPortElement.getClientWidth();
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
        Element e = (Element) canvas.getElement().getElementsByTagName("svg").getItem(0);
        downloadHandler.handleDownload();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        loadHandler.run();
    }
}