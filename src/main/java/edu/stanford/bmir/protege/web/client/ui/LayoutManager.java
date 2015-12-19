package edu.stanford.bmir.protege.web.client.ui;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.portal.Portlet;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import java.util.ArrayList;
import java.util.List;

public class LayoutManager {

	private final Project project;

	private final EventBus eventBus;

	private final DispatchServiceManager dispatchServiceManager;

	public LayoutManager(Project project, EventBus eventBus, DispatchServiceManager dispatchServiceManager) {
		this.project = project;
		this.eventBus = eventBus;
		this.dispatchServiceManager = dispatchServiceManager;
	}
	
	public List<AbstractTab> createTabs(SelectionModel selectionModel, ProjectLayoutConfiguration projectLayoutConfig) {
		List<AbstractTab> tabs = new ArrayList<AbstractTab>();
		List<TabConfiguration> tabConfigs = projectLayoutConfig.getTabs();
		for (TabConfiguration tabConfig : tabConfigs) {
			String tabName = tabConfig.getName();
			AbstractTab tab = createTab(selectionModel, tabName);
			if (tab != null) {
				setupTab(tab, tabConfig);
				tabs.add(tab);
			}
		}
		return tabs;
	}
	
	public AbstractTab createTab(SelectionModel selectionModel, String javaClassName) {
		AbstractTab tab = UIFactory.createTab(selectionModel, eventBus, dispatchServiceManager, project, javaClassName);
		return tab;
	}
	
	public void setupTab(AbstractTab tab, TabConfiguration tabConfig) {
		tab.setTabConfiguration(tabConfig);
		tab.setup();
	}
	
//	public EntityPortlet createPortlet(PortletConfiguration portletConfig) {
//		if (portletConfig == null) {return null;}
//		String name = portletConfig.getName();
//		EntityPortlet portlet = UIFactory.createPortlet(project, name);
//		if (portlet == null) {return null;}
//		return portlet;
//	}
	
	public ColumnLayoutData[] createColumnLayoutData(List<Float> cols) {
		if (cols == null) { return null; }
		ColumnLayoutData[] cld = new ColumnLayoutData[cols.size()];
		for (int i = 0; i< cols.size(); i++) {
			cld[i] = new ColumnLayoutData(cols.get(i));
		}
		return cld;
	}
	
	public PortletConfiguration createPortletConfiguration(EntityPortlet portlet) {
		if (portlet == null) { return null; }
		PortletConfiguration portletConfiguration = new PortletConfiguration();
		portletConfiguration.setName(portlet.getClass().getName());
		portletConfiguration.setHeight(((Portlet)portlet).getHeight());
		portletConfiguration.setWidth(((Portlet)portlet).getWidth());
		return portletConfiguration;
	}
	
	public void removeTab(AbstractTab tab) {
		TabConfiguration tabConfig = tab.getTabConfiguration();
		if (tabConfig == null) { return; }
		project.getProjectLayoutConfiguration().removeTab(tabConfig);
	}
	
	public AbstractTab addTab(SelectionModel selectionModel, String javaClassName) {
		AbstractTab tab = createTab(selectionModel, javaClassName);
		if (tab == null) { return null; }
		setupTab(tab, tab.getDefaultTabConfiguration());
		project.getProjectLayoutConfiguration().addTab(tab.getTabConfiguration());
		return tab;
	}

	
}
