package edu.stanford.bmir.protege.web.client.ui.projectlist;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.DownloadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.LoadProjectRequestHandler;
import edu.stanford.bmir.protege.web.client.ui.projectmanager.TrashManagerRequestHandler;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/10/2013
 */
public class ProjectListViewUIImpl extends Composite implements ProjectListView {

    interface ProjectListViewUIImplUiBinder extends UiBinder<HTMLPanel, ProjectListViewUIImpl> {

    }

    private static ProjectListViewUIImplUiBinder ourUiBinder = GWT.create(ProjectListViewUIImplUiBinder.class);



    @UiField(provided = true)
    protected DataGrid<ProjectListEntry> projectTable;

    @UiField(provided = true)
    protected AbstractPager projectTablePager;

    private final ListDataProvider<ProjectListEntry> listDataProvider;

    private DownloadProjectRequestHandler downloadProjectRequestHandler = new DownloadProjectRequestHandler() {
        @Override
        public void handleProjectDownloadRequest(ProjectId projectId) {
            GWT.log("handleProjectDownloadRequest:  No handler registered.");
        }
    };

    private LoadProjectRequestHandler loadProjectRequestHandler = new LoadProjectRequestHandler() {
        @Override
        public void handleProjectLoadRequest(ProjectId projectId) {
            GWT.log("handleProjectLoadRequest: No handler registered.");
        }
    };

    private TrashManagerRequestHandler trashManagerRequestHandler = new TrashManagerRequestHandler() {
        @Override
        public void handleMoveProjectToTrash(ProjectId projectId) {
            GWT.log("handleMoveProjectToTrash: No handler registered.");
        }

        @Override
        public void handleRemoveProjectFromTrash(ProjectId projectId) {
            GWT.log("handleRemoveProjectFromTrash: No handler registered.");
        }
    };





    public ProjectListViewUIImpl() {
        ProvidesKey<ProjectListEntry> keyProvider = new ProvidesKey<ProjectListEntry>() {
            @Override
            public Object getKey(ProjectListEntry item) {
                return item.getProjectId();
            }
        };
        DataGrid.Resources res = GWT.create(ProjectListResources.class);

        projectTable = new DataGrid<ProjectListEntry>(Integer.MAX_VALUE, res, keyProvider);
        projectTablePager = new SimplePager(SimplePager.TextLocation.RIGHT, false, true);

        final ProjectDisplayNameColumn projectNameColumn = new ProjectDisplayNameColumn();
        projectTable.addColumn(projectNameColumn, "Project Name");
        projectTable.addColumn(new ProjectDescriptionColumn(), "Description");
        final TextColumn<ProjectListEntry> ownerColumn = new TextColumn<ProjectListEntry>() {
            @Override
            public String getValue(ProjectListEntry object) {
                return object.getProjectDetails().getOwner().getUserName();
            }
        };
        ownerColumn.setSortable(true);
        ownerColumn.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

        final DownloadColumn downloadColumn = new DownloadColumn();

        final TrashColumn trashColumn = new TrashColumn();


        projectTable.addColumn(ownerColumn, "Owner");
        projectTable.addColumn(downloadColumn, "Download");
        projectTable.addColumn(trashColumn);

//        projectTable.setPageSize(Integer.MAX_VALUE);

        projectTable.setColumnWidth(projectNameColumn, "300px");
        projectTable.setColumnWidth(ownerColumn, "150px");
        projectTable.setColumnWidth(downloadColumn, "90px");
        projectTable.setColumnWidth(trashColumn, "60px");


        projectTable.getColumnSortList().push(projectNameColumn);

        projectTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.BOUND_TO_SELECTION);


        projectTable.setRowCount(Integer.MAX_VALUE);



        listDataProvider = new ListDataProvider<ProjectListEntry>(keyProvider);
        listDataProvider.addDataDisplay(projectTable);

        ColumnSortEvent.ListHandler sortHandler = new ColumnSortEvent.ListHandler<ProjectListEntry>(Collections.<ProjectListEntry>emptyList());
        sortHandler.setComparator(projectNameColumn, new Comparator<ProjectListEntry>() {
            @Override
            public int compare(ProjectListEntry o1, ProjectListEntry o2) {
                return o1.getProjectId().getId().compareTo(o2.getProjectId().getId());
            }
        });

        final SingleSelectionModel<ProjectListEntry> selectionModel = new SingleSelectionModel<ProjectListEntry>(keyProvider);
        projectTable.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                SelectionEvent.fire(ProjectListViewUIImpl.this, selectionModel.getSelectedObject().getProjectId());
            }
        });

        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);


        projectTable.setPageSize(500);
        projectTablePager.setDisplay(projectTable);

    }

    @Override
    public void setDownloadProjectRequestHandler(DownloadProjectRequestHandler handler) {
        this.downloadProjectRequestHandler = checkNotNull(handler);
    }

    @Override
    public void setLoadProjectRequestHandler(LoadProjectRequestHandler handler) {
        this.loadProjectRequestHandler = checkNotNull(handler);
    }

    @Override
    public void setTrashManagerRequestHandler(TrashManagerRequestHandler handler) {
        this.trashManagerRequestHandler = checkNotNull(handler);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<ProjectId> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    @Override
    public void setSelectedProject(ProjectId projectId) {
        SingleSelectionModel<ProjectListEntry> selectionModel = (SingleSelectionModel<ProjectListEntry>) projectTable.getSelectionModel();
        int index = 0;
        for(ProjectListEntry entry : projectTable.getVisibleItems()) {
            if(entry.getProjectId().equals(projectId)) {
                selectionModel.setSelected(entry, true);
                projectTable.getRowElement(index).scrollIntoView();
                return;
            }
            index++;
        }
    }

    @Override
    public void setListData(List<ProjectDetails> projectDetails) {
        List<ProjectListEntry> entries = new ArrayList<ProjectListEntry>();
        for(ProjectDetails details : projectDetails) {
            entries.add(new ProjectListEntry(details));
        }
        listDataProvider.getList().clear();
        listDataProvider.getList().addAll(entries);
    }

    @Override
    public void addListData(ProjectDetails projectDetails) {
        listDataProvider.getList().add(0, new ProjectListEntry(projectDetails));
    }

    private class ProjectDisplayNameColumn extends Column<ProjectListEntry, String> {

        private ProjectDisplayNameColumn() {
            super(new ClickableTextCell() {

            });
            setSortable(true);
        }

        @Override
        public String getValue(ProjectListEntry object) {
            return object.getProjectDetails().getDisplayName();
        }

        @Override
        public void render(Cell.Context context, ProjectListEntry object, SafeHtmlBuilder sb) {
            final String projectName = object.getProjectDetails().getDisplayName();
            SafeHtmlBuilder projectNameBuilder = new SafeHtmlBuilder();
            String escapedProjectName = projectNameBuilder.appendEscaped(projectName).toSafeHtml().asString();
            sb.appendHtmlConstant("<div style=\"width: 100%; height: 100%; color: #6982AB; cursor: pointer;\" title=\"Open " + escapedProjectName + "\">" + escapedProjectName + "</div>");
        }

        @Override
        public VerticalAlignmentConstant getVerticalAlignment() {
            return HasVerticalAlignment.ALIGN_TOP;
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element elem, ProjectListEntry object, NativeEvent event) {
            super.onBrowserEvent(context, elem, object, event);
            loadProjectRequestHandler.handleProjectLoadRequest(object.getProjectId());
        }
    }


    private class ProjectDescriptionColumn extends Column<ProjectListEntry, String> {

        private ProjectDescriptionColumn() {
            super(new AbstractCell<String>() {
                @Override
                public void render(Context context, String value, SafeHtmlBuilder sb) {
                    sb.appendHtmlConstant(value);
                }
            });
        }

        @Override
        public String getValue(ProjectListEntry object) {
            return object.getProjectDetails().getDescription();
        }

        @Override
        public VerticalAlignmentConstant getVerticalAlignment() {
            return HasVerticalAlignment.ALIGN_TOP;
        }
    }



    private class DownloadColumn extends Column<ProjectListEntry, String> {

        private DownloadColumn() {
            super(new ClickableTextCell());
        }

        @Override
        public String getValue(ProjectListEntry object) {
            return "Download " + object.getProjectId();
        }

        @Override
        public VerticalAlignmentConstant getVerticalAlignment() {
            return HasVerticalAlignment.ALIGN_TOP;
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element elem, ProjectListEntry object, NativeEvent event) {
            super.onBrowserEvent(context, elem, object, event);
            downloadProjectRequestHandler.handleProjectDownloadRequest(object.getProjectId());
        }

        @Override
        public void render(Cell.Context context, ProjectListEntry object, SafeHtmlBuilder sb) {
            sb.appendHtmlConstant("<div style=\"width: 100%; height: 100%; cursor: pointer;\" title=\"Download latest version of project\"><img style=\"padding-top: 1px; \" src=\"" + WebProtegeClientBundle.BUNDLE.downloadIcon().getSafeUri().asString() + "\"/></div>");
        }
    }



    private class TrashColumn extends Column<ProjectListEntry, String> {

        private TrashColumn() {
            super(new ClickableTextCell());
        }

        @Override
        public String getValue(ProjectListEntry object) {
            return "Trash " + object.getProjectId();
        }

        @Override
        public VerticalAlignmentConstant getVerticalAlignment() {
            return HasVerticalAlignment.ALIGN_TOP;
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element elem, ProjectListEntry object, NativeEvent event) {
            super.onBrowserEvent(context, elem, object, event);
            if(isOwnerOfProjectEntry(object)) {
                if(object.getProjectDetails().isInTrash()) {
                    trashManagerRequestHandler.handleRemoveProjectFromTrash(object.getProjectId());
                }
                else {
                    trashManagerRequestHandler.handleMoveProjectToTrash(object.getProjectId());
                }
            }
        }

        @Override
        public void render(Cell.Context context, ProjectListEntry object, SafeHtmlBuilder sb) {
            if (isOwnerOfProjectEntry(object)) {
                final String title = object.getProjectDetails().isInTrash() ? "Remove project from trash" : "Move project to trash";
                sb.appendHtmlConstant("<div style=\"width: 100%; cursor: pointer;\" title=\"" + title + "\"><img style=\"padding-top: 2px; \" src=\"images/trash.png\"/></div>");
            }
        }

        private boolean isOwnerOfProjectEntry(ProjectListEntry object) {
            return object.getProjectDetails().getOwner().equals(Application.get().getUserId());
        }
    }



}
