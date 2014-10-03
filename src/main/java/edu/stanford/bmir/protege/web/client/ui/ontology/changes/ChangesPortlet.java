package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import com.google.common.base.Optional;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.util.PaginationUtil;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.Collection;

public class ChangesPortlet extends AbstractOWLEntityPortlet {
	protected GridPanel changesGrid;
	protected RecordDef recordDef;
	protected Store store;
	protected ChangesProxyImpl proxy;


    private RevisionNumber lastRevisionNumber = RevisionNumber.getRevisionNumber(0);

	public ChangesPortlet(Project project) {
		super(project);
	}

	@Override
	public void initialize() {
		createGrid();
		setHeight(200);
		add(changesGrid);
        addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                ChangesPortlet.this.handleProjectChanged(event);
            }
        });
	}

    private void handleProjectChanged(ProjectChangedEvent event) {
        if(lastRevisionNumber.equals(event.getRevisionNumber())) {
            return;
        }
        lastRevisionNumber = event.getRevisionNumber();
        for(OWLEntityData entityData : event.getSubjects()) {
            if(isSelected(entityData.getEntity())) {
                updateDisplayForSelectedEntity();
                return;
            }
        }
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        updateDisplayForSelectedEntity();
    }

    private void updateDisplayForSelectedEntity() {
        store.removeAll();

        String entityName = "";

        EntityData entity = getEntity();

        if (entity != null) {
            entityName = entity.getName();
            setTitle("Change history for " + UIUtil.getDisplayText(getEntity()));
        } else {
            setTitle("Change history (nothing selected)");
        }


        ProjectId projectId = getProjectId();
        proxy.resetParams();
        proxy.setProjectId(projectId);
        proxy.setEntityName(entityName);

        PagingToolbar pToolbar = (PagingToolbar) changesGrid.getBottomToolbar();
        store.load(0, pToolbar.getPageSize());
    }

    private void createGrid() {
		changesGrid = new GridPanel();

		changesGrid.setAutoWidth(true);
		changesGrid.setAutoExpandColumn("ChangesGrid_ChangeDescCol");
		changesGrid.setStripeRows(true);
		changesGrid.setFrame(true);
		createColumns();

		recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("desc"),
				new StringFieldDef("author"), new DateFieldDef("timestamp"),
				new StringFieldDef("applies") });


		ArrayReader reader = new ArrayReader(recordDef);
		proxy = new ChangesProxyImpl();
		store = new Store(proxy, reader);

		PagingToolbar pToolbar = PaginationUtil.getNewPagingToolbar(store, 20);

		changesGrid.setBottomToolbar(pToolbar);

		changesGrid.setStore(store);

		if (changesGrid.getStore() == null) {
			changesGrid.setStore(store);
		}

		setTitle("Changes");

		//TODO: Uncomment this code after the patch to set entityName for a newly created portlet is set
		//reload();
	}

	private void createColumns() {
		ColumnConfig changeDescCol = new ColumnConfig("Description", "desc");
		changeDescCol.setId("ChangesGrid_ChangeDescCol");
		changeDescCol.setResizable(true);
		changeDescCol.setSortable(true);

		ColumnConfig authorCol = new ColumnConfig("Author", "author");
		authorCol.setResizable(true);
		authorCol.setSortable(true);

		ColumnConfig timestampCol = new ColumnConfig("Timestamp", "timestamp");
		timestampCol.setResizable(true);
		timestampCol.setSortable(true);

		ColumnConfig appliesToCol = new ColumnConfig("Applies to", "applies");
		appliesToCol.setResizable(true);
		appliesToCol.setSortable(true);
		appliesToCol.setHidden(true);

		ColumnConfig[] columns = new ColumnConfig[] { changeDescCol, authorCol,
				timestampCol, appliesToCol };
		ColumnModel columnModel = new ColumnModel(columns);
		changesGrid.setColumnModel(columnModel);
	}
}
