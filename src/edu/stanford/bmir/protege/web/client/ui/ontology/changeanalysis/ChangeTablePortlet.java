package edu.stanford.bmir.protege.web.client.ui.ontology.changeanalysis;

import com.gwtext.client.widgets.PagingToolbar;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.changes.ChangesPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.changes.ChangesProxyImpl;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * @author Sean Falconer <sean.falconer@stanford.edu>
 */
public class ChangeTablePortlet extends ChangesPortlet {

	public ChangeTablePortlet(Project project) {
		super(project);
	}
	
	public ChangesProxyImpl getDataProxy() {
		return proxy;
	}
	
	@Override
	public void reload() {
		if(!(proxy instanceof FilteringChangesProxyImpl)) {
			proxy = new FilteringChangesProxyImpl();
			store.setDataProxy(proxy);
		}
		store.removeAll();

		String entityName = "";
		String projectName = "";

		EntityData entity = getEntity();

		if (entity != null) {
			entityName = entity.getName();
			setTitle("Change history for " + UIUtil.getDisplayText(getEntity()));
		} else {
			setTitle("Change history (nothing selected)");
		}

		if (project != null) {
			projectName = project.getProjectName();
		} else {
		    return;
		}
		
		proxy.resetParams();
		proxy.setProjectName(projectName);
		proxy.setEntityName(entityName);
		
		PagingToolbar pToolbar = (PagingToolbar) changesGrid.getBottomToolbar();
		store.load(0, pToolbar.getPageSize());
		
	}
}
