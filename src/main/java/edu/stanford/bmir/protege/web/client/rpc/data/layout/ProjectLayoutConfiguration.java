package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectLayoutConfiguration extends GenericConfiguration implements Serializable {
	private static final long serialVersionUID = -1637798442860308838L;

    private ProjectId projectId;

    private List<TabConfiguration> tabs;


	//this field has no use, only to whitelist Boolean for the RPC
	private Boolean booleanField;

	public ProjectLayoutConfiguration() {
		tabs = new ArrayList<TabConfiguration>();
	}

	public List<TabConfiguration> getTabs() {
		return tabs;
	}

	public void setTabs(List<TabConfiguration> tabs) {
		this.tabs = tabs;
	}

	public void removeTab(TabConfiguration tab) {
		tabs.remove(tab);
	}

	public void addTab(TabConfiguration tab) {
		tabs.add(tab);
	}

	public ProjectId getProjectId() {
		return projectId;
	}

	public void setProjectId(ProjectId projectId) {
		this.projectId = projectId;
	}

}