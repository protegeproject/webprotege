package edu.stanford.bmir.protege.web.client.revisions;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import edu.stanford.bmir.protege.web.client.projectlist.ProjectListResources;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/04/2013
 */
public class RevisionsListViewImpl extends Composite implements RevisionsListView {


    private static final String REVISION_COL_TITLE = "Revision";

    private static final String DATE_TIME_COL_TITLE = "Date";

    private static final String NUMBER_OF_CHANGES_COL_TITLE = "Number of changes";

    private static final String AUTHOR_COL_TITLE = "Author";

    private static final String DOWNLOAD_COL_TITLE = "Download";

    private DownloadRevisionRequestHandler handler = new DownloadRevisionRequestHandler() {
        @Override
        public void handleDownloadRevisionRequest(RevisionNumber revisionNumber) {
            GWT.log("No DownloadRevisionRequestHandler registered");
        }
    };

    private final Timer redrawTimer;


    interface RevisionsListViewImplUiBinder extends UiBinder<HTMLPanel, RevisionsListViewImpl> {

    }

    private static RevisionsListViewImplUiBinder ourUiBinder = GWT.create(RevisionsListViewImplUiBinder.class);



    @UiField(provided = true)
    protected DataGrid<RevisionSummary> dataGrid;

    public RevisionsListViewImpl() {
        DataGrid.Resources res = GWT.create(ProjectListResources.class);
        ProvidesKey<RevisionSummary> providesKey = new ProvidesKey<RevisionSummary>() {
            @Override
            public Object getKey(RevisionSummary item) {
                return item.getRevisionNumber();
            }
        };
        dataGrid = new DataGrid<RevisionSummary>(1000, res, providesKey);
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);

        dataGrid.addColumn(new RevisionIdColumn(), REVISION_COL_TITLE);
        dataGrid.addColumn(new DateColumn(), DATE_TIME_COL_TITLE);
        dataGrid.addColumn(new ChangeCountColumn(), NUMBER_OF_CHANGES_COL_TITLE);
        dataGrid.addColumn(new UserIdColumn(), AUTHOR_COL_TITLE);
        dataGrid.addColumn(new DownloadColumn(), DOWNLOAD_COL_TITLE);
        setWidth("100%");

        dataGrid.setColumnWidth(dataGrid.getColumn(0), "100px");
        dataGrid.setColumnWidth(dataGrid.getColumn(4), "100px");

        dataGrid.setSelectionModel(new SingleSelectionModel<RevisionSummary>(providesKey));


        redrawTimer = new Timer() {
             @Override
             public void run() {
                 dataGrid.redraw();
             }
         };
        redrawTimer.scheduleRepeating(60000);
    }

    @Override
    public void dispose() {
        redrawTimer.cancel();
    }

    public void setDataProvider(AbstractDataProvider<RevisionSummary> dataProvider) {
        dataProvider.addDataDisplay(dataGrid);
    }

    @Override
    public void setDownloadRevisionRequestHandler(DownloadRevisionRequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    private class RevisionIdColumn extends TextColumn<RevisionSummary> {

        private RevisionIdColumn() {
            setSortable(true);
        }

        @Override
        public String getValue(RevisionSummary object) {
            return Integer.toString(object.getRevisionNumber().getValueAsInt());
        }
    }


    private class DateColumn extends TextColumn<RevisionSummary> {


        private DateColumn() {
            setSortable(true);
        }

        @Override
        public String getValue(RevisionSummary object) {
            return TimeUtil.getTimeRendering(object.getTimestamp());
        }
    }


    private class ChangeCountColumn extends TextColumn<RevisionSummary> {

        private ChangeCountColumn() {
            setSortable(true);
        }

        @Override
        public String getValue(RevisionSummary object) {
            return Integer.toString(object.getChangeCount());
        }
    }


    private class UserIdColumn extends TextColumn<RevisionSummary> {

        private UserIdColumn() {
            setSortable(true);
        }

        @Override
        public String getValue(RevisionSummary object) {
            return object.getUserId().getUserName();
        }
    }

    private class DownloadColumn extends Column<RevisionSummary, String> {

        private DownloadColumn() {
            super(new ClickableTextCell());
        }

        @Override
        public String getValue(RevisionSummary object) {
            return "Download revision " + object.getRevisionNumber().getValue();
        }

        @Override
        public VerticalAlignmentConstant getVerticalAlignment() {
            return HasVerticalAlignment.ALIGN_TOP;
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element elem, RevisionSummary object, NativeEvent event) {
            super.onBrowserEvent(context, elem, object, event);
            handler.handleDownloadRevisionRequest(object.getRevisionNumber());
        }

        @Override
        public void render(Cell.Context context, RevisionSummary object, SafeHtmlBuilder sb) {
            sb.appendHtmlConstant("<div style=\"width: 100%; height: 100%; cursor: pointer;\" title=\"Download revision " + object.getRevisionNumber().getValueAsInt() + "\"><img style=\"padding-top: 1px; \" src=\"" + WebProtegeClientBundle.BUNDLE.downloadIcon().getSafeUri().asString() + "\"/></div>");
        }
    }

}