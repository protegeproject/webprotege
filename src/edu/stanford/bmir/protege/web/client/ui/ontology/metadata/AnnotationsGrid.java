package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;

import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.AnnotationData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class AnnotationsGrid extends GridPanel {

	protected ColumnModel columnModel;
	protected String projectName;
	protected EntityData _currentEntity;
	protected RecordDef recordDef;
	protected Store store;

	public AnnotationsGrid(String projectName) {
		this.projectName = projectName;
		createGrid();
	}
	
	protected void createGrid() {
		ColumnConfig propCol = new ColumnConfig();
		propCol.setHeader("Property");
		propCol.setDataIndex("prop");
		propCol.setResizable(true);
		propCol.setSortable(true);

		ColumnConfig valueCol = new ColumnConfig();
		valueCol.setHeader("Value");
		valueCol.setId("AnnotationsGrid_ValueColumn");
		valueCol.setDataIndex("val");
		valueCol.setResizable(true);
		valueCol.setSortable(true);
		
		ColumnConfig langCol = new ColumnConfig();
		langCol.setHeader("Language");
		langCol.setDataIndex("lang");
		langCol.setResizable(true);
		langCol.setSortable(true);
		
		ColumnConfig[] columns = new ColumnConfig[]{propCol, valueCol, langCol};
		columnModel = new ColumnModel(columns);  
		setColumnModel(columnModel);
		
		setHeight(400);
		setAutoWidth(true);
		setStripeRows(true);
		setAutoExpandColumn("AnnotationsGrid_ValueColumn");
		setId("annotations-grid");
		
		recordDef = new RecordDef(
			new FieldDef[] {
				new StringFieldDef("prop"),
				new StringFieldDef("val"),
				new StringFieldDef("lang")
			}
		);

		ArrayReader reader = new ArrayReader(recordDef);
		MemoryProxy proxy = new MemoryProxy(new Object[][]{});
		store = new Store(proxy, reader);
		store.load();
		
		/*
		 * GWT-Ext complains if grid is reinitialized because it thinks that 
		 * a store has already been assigned.  Will throw error "Cannot 
		 * change configuration property 'store' after the component has 
		 * been rendered". 
		 */  
		if (getStore() == null) {
			setStore(store);
		}
	}
	
	public void setEntity(EntityData newEntity) {		
		if (_currentEntity != null && _currentEntity.equals(newEntity)) {
			return;
		}
		
		store.removeAll();
		_currentEntity = newEntity;
		
		if (_currentEntity == null) return;

		reload();
	}
	
	public void reload() {		
		OntologyServiceManager.getInstance().getAnnotationProperties(
				projectName, _currentEntity.getName(), new GetAnnotations());
	}
	
	class GetAnnotations extends AbstractAsyncHandler<List<AnnotationData>> {

		public void handleFailure(Throwable caught) {
			GWT.log("RPC error getting ontology annotations", caught);
		}

		public void handleSuccess(List<AnnotationData> result) {
			for (AnnotationData data : result) {
				Record record = recordDef.createRecord(new Object[] { data.getName(), data.getValue(), data.getLang() });
				store.add(record);
			}
			
			/*
			 * Annotations grid will not refresh properly w/out these lines.
			 */
			store.commitChanges();
			reconfigure(store, columnModel);
		}
	}
}
