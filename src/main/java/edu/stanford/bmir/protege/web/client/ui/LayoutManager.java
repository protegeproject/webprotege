package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.portal.Portlet;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.inject.ApplicationClientInjector;
import edu.stanford.bmir.protege.web.client.inject.WebProtegeClientInjector;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.TabConfiguration;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import java.util.ArrayList;
import java.util.List;

public class LayoutManager {

	private final ProjectId projectId;

	private final ProjectLayoutConfiguration layoutConfiguration;

	@Inject
	public LayoutManager(ProjectId projectId, ProjectLayoutConfiguration layoutConfiguration) {
		this.projectId = projectId;
		this.layoutConfiguration = layoutConfiguration;
	}

	public List<AbstractTab> createTabs(ProjectLayoutConfiguration projectLayoutConfig) {
		List<AbstractTab> tabs = new ArrayList<AbstractTab>();
		List<TabConfiguration> tabConfigs = projectLayoutConfig.getTabs();
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
		GWT.log("[LayoutManager] Creating tab: " + javaClassName);
		UIFactory uiFactory = WebProtegeClientInjector.getUiFactory(projectId);
		return uiFactory.createTab(javaClassName);
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
		layoutConfiguration.removeTab(tabConfig);
	}
	
	public AbstractTab addTab(String javaClassName) {
		AbstractTab tab = createTab(javaClassName);
		if (tab == null) { return null; }
		setupTab(tab, tab.getDefaultTabConfiguration());
		layoutConfiguration.addTab(tab.getTabConfiguration());
		return tab;
	}

	
}
