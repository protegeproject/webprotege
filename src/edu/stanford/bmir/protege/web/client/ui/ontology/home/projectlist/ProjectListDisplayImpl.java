package edu.stanford.bmir.protege.web.client.ui.ontology.home.projectlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerService;
import edu.stanford.bmir.protege.web.client.rpc.ProjectManagerServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.client.ui.library.common.SimplePanelDisplay;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.OntologiesPortlet;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public class ProjectListDisplayImpl extends FlowPanel implements ProjectListDisplay {

    private CellTable<ProjectData> cellTable = new CellTable<ProjectData>();

    private List<ProjectData> projectData = new ArrayList<ProjectData>();

    private final SingleSelectionModel<ProjectData> cellTableSelectionModel;

    public ProjectListDisplayImpl() {
        cellTable.addColumn(new ProjectNameColumn());
        cellTable.addColumn(new ProjectDesciptionColumn());
        cellTable.addColumn(new ProjectOwnerColumn());
        add(cellTable);
        cellTableSelectionModel = new SingleSelectionModel<ProjectData>();
        cellTable.setSelectionModel(cellTableSelectionModel);
        cellTableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                fireSelectionChanged();
            }
        });
        cellTable.setVisibleRange(0, 3);
        refresh();
    }

    public Widget getDisplayWidget() {
        return this;
    }

    private void refill() {
        ProjectManagerServiceAsync service = GWT.create(ProjectManagerService.class);
        String userName = GlobalSettings.getGlobalSettings().getUserName();
        UserId userId;
        if(userName == null) {
            userId = UserId.getNull();
        }
        else {
            userId = UserId.getUserId(userName);
        }
        service.getProjects(userId, new AsyncCallback<List<ProjectData>>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(List<ProjectData> result) {
                ProjectListDisplayImpl.this.projectData = result;
                for(int i =0 ; i < 100; i++) {
                    projectData.add(new ProjectData("Test " + i, "", "Project " + i, "mh", false));
                }
                cellTable.setRowData(projectData);
            }
        });
    }

    public void setSelectedProject(ProjectId projectId) {
        for(ProjectData data : cellTable.getVisibleItems()) {
            if(data.getName().equals(projectId.getProjectName())) {
                cellTableSelectionModel.setSelected(data, true);
                break;
            }
        }
    }

    public ProjectId getSelectedProject() {
        ProjectData data = cellTableSelectionModel.getSelectedObject();
        return new ProjectId(data.getName());
    }

    public void clearFilters() {
    }

    public void addFilter(ProjectListDisplayFilter filter) {
    }

    public void removeFilter(ProjectListDisplayFilter filter) {
    }

    public void refresh() {
        refill();
        // TODO: Fire changed?
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////
    ////////   Implementation of HasSelectionListeners
    ////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public HandlerRegistration addSelectionHandler(SelectionHandler<ProjectId> projectIdSelectionHandler) {
        return addHandler(projectIdSelectionHandler, SelectionEvent.getType());
    }


    private void fireSelectionChanged() {
        // TODO:
        ProjectData selectedObject = cellTableSelectionModel.getSelectedObject();
        ProjectId selectedItem = new ProjectId(selectedObject.getName());
        SelectionEvent.fire(this, selectedItem);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////
    ////////   Columns for the table
    ////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private class ProjectNameColumn extends TextColumn<ProjectData> {

        @Override
        public String getValue(ProjectData object) {
            return object.getName();
        }
    }

    private class ProjectDesciptionColumn extends TextColumn<ProjectData> {

        @Override
        public String getValue(ProjectData object) {
            return object.getDescription();
        }
    }
    
    private class ProjectOwnerColumn extends TextColumn<ProjectData> {

        @Override
        public String getValue(ProjectData object) {
            return object.getOwner();
        }
    }
}


