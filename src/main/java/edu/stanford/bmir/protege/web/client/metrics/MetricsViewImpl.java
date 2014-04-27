package edu.stanford.bmir.protege.web.client.metrics;

import bsh.commands.dir;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import edu.stanford.bmir.protege.web.client.csv.CSVGridResources;
import edu.stanford.bmir.protege.web.client.events.RequestRefreshEvent;
import edu.stanford.bmir.protege.web.client.events.RequestRefreshEventHandler;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class MetricsViewImpl extends Composite implements MetricsView {

    private final ListDataProvider<MetricValue> dataProvider;

    interface MetricsViewImplUiBinder extends UiBinder<HTMLPanel, MetricsViewImpl> {
    }

    private static MetricsViewImplUiBinder ourUiBinder = GWT.create(MetricsViewImplUiBinder.class);

    private RequestRefreshEventHandler requestRefreshHandler = new RequestRefreshEventHandler() {
        @Override
        public void handleRequestRefresh(RequestRefreshEvent requestRefreshEvent) {

        }
    };

    @UiField(provided = true)
    protected DataGrid<MetricValue> grid;

    @UiField
    protected Label dirtyLabel;

    public MetricsViewImpl() {
        grid = new DataGrid<MetricValue>(100, CSVGridResources.INSTANCE);
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        grid.addColumn(new TextColumn<MetricValue>() {
            @Override
            public String getValue(MetricValue object) {
                return object.getMetricName();
            }

            @Override
            public String getCellStyleNames(Cell.Context context, MetricValue object) {
                return object.isEmpty() ? "empty-metric" : "non-empty-metric";
            }
        }, "Metric");
        grid.addColumn(new TextColumn<MetricValue>() {
            @Override
            public String getValue(MetricValue object) {
                return object.getBrowserText();
            }

            @Override
            public String getCellStyleNames(Cell.Context context, MetricValue object) {
                return object.isEmpty() ? "empty-metric" : "non-empty-metric";
            }

            @Override
            public void render(Cell.Context context, MetricValue object, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant(object.getBrowserText());
            }
        }, "Value");
        grid.setColumnWidth(0, 70, Style.Unit.PCT);
        dataProvider = new ListDataProvider<MetricValue>();
        dataProvider.addDataDisplay(grid);
        dirtyLabel.setVisible(false);
    }

    @Override
    public void setDirty(boolean dirty) {
        dirtyLabel.setVisible(dirty);
    }

    @UiHandler("dirtyLabel")
    protected void handleDirtyLabelClicked(ClickEvent clickEvent) {
        requestRefreshHandler.handleRequestRefresh(new RequestRefreshEvent());
    }

    @Override
    public void setMetrics(List<MetricValue> metrics) {
        List<MetricValue> list = dataProvider.getList();
        list.clear();
        list.addAll(metrics);
        setDirty(false);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public void setRequestRefreshEventHandler(RequestRefreshEventHandler handler) {
        requestRefreshHandler = checkNotNull(handler);
    }


}
