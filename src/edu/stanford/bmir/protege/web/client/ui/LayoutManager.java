package edu.stanford.bmir.protege.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.portal.Portlet;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;

public class LayoutManager {
	private Project project;

	public LayoutManager(Project project) {
		this.project = project;
	}
	
	public List<AbstractTab> createTabs(ProjectConfiguration projectConfig) {
		List<AbstractTab> tabs = new ArrayList<AbstractTab>();
		List<TabConfiguration> tabConfigs = projectConfig.getTabs();
		for (TabConfiguration tabConfig : tabConfigs) {
			String tabName = tabConfig.getName();
			AbstractTab tab = createTab(tabName);
			if (tab != null) {
				setupTab(tab, tabConfig);
				tabs.add(tab);
			}
		}
		return tabs;
	}
	
	public AbstractTab createTab(String javaClassName) {
		AbstractTab tab = UIFactory.createTab(project, javaClassName);
		return tab;
	}
	
	public void setupTab(AbstractTab tab, TabConfiguration tabConfig) {
		tab.setTabConfiguration(tabConfig);
		tab.setup();
	}
	
	public EntityPortlet createPortlet(PortletConfiguration portletConfig) {
		if (portletConfig == null) {return null;}
		String name = portletConfig.getName();
		EntityPortlet portlet = UIFactory.createPortlet(project, name);
		if (portlet == null) {return null;}
		return portlet;
	}
	
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
		project.getProjectConfiguration().removeTab(tabConfig);
	}
	
	public AbstractTab addTab(String javaClassName) {
		AbstractTab tab = createTab(javaClassName);
		if (tab == null) { return null; }
		setupTab(tab, tab.getDefaultTabConfiguration());
		project.getProjectConfiguration().addTab(tab.getTabConfiguration());
		return tab;
	}

	
}
