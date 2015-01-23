package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.RenderableGetObjectResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetEntityAnnotationsAction;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Set;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class AnnotationsGrid extends GridPanel implements HasProjectId {

	protected ColumnModel columnModel;
	protected ProjectId projectId;
	protected EntityData _currentEntity;
	protected RecordDef recordDef;
	protected Store store;

	public AnnotationsGrid(ProjectId projectId) {
		this.projectId = projectId;
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

//		OntologyServiceManager.getInstance().getAnnotationProperties(
//				projectName, _currentEntity.getName(), new GetAnnotations());

        IRI entityIRI = IRI.create(_currentEntity.getName());
        DispatchServiceManager.get().execute(new GetEntityAnnotationsAction(entityIRI, getProjectId()), new GetAnnotationsHandler());
	}

    public ProjectId getProjectId() {
        return projectId;
    }

    class GetAnnotationsHandler extends AbstractAsyncHandler<RenderableGetObjectResult<Set<OWLAnnotation>>> {

		public void onFailure(Throwable caught) {
			GWT.log("RPC error getting ontology annotations", caught);
		}

		public void onSuccess(RenderableGetObjectResult<Set<OWLAnnotation>> result) {
			for (OWLAnnotation data : result.getObject()) {
                final OWLAnnotationProperty property = data.getProperty();
                String name = result.getBrowserTextMap().getBrowserText(property).or(property.getIRI().toQuotedString());
                if(data.getValue() instanceof OWLLiteral) {
                    OWLLiteral literalValue = (OWLLiteral) data.getValue();
                    final String value = literalValue.getLiteral();
                    final String lang;
                    if(literalValue.hasLang()) {
                        lang = literalValue.getLang();
                    }
                    else {
                        lang = "";
                    }
                    Record record = recordDef.createRecord(new Object[] { name, value, lang });
                    store.add(record);
                }

			}
			
			/*
			 * Annotations grid will not refresh properly w/out these lines.
			 */
			store.commitChanges();
			reconfigure(store, columnModel);
		}
	}
}
