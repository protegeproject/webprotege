package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

public class ProjectLayoutConfiguration extends GenericConfiguration implements Serializable {

    private ProjectId projectId;

    private List<TabConfiguration> tabs;


	//this field has no use, only to whitelist Boolean for the RPC
	private Boolean booleanField;

	public ProjectLayoutConfiguration() {
		tabs = new ArrayList<>();
	}

	public List<TabConfiguration> getTabs() {
		return new ArrayList<>(tabs);
	}

	public void setTabs(List<TabConfiguration> tabs) {
		this.tabs = new ArrayList<>(tabs);
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


	@Override
	public String toString() {
		return toStringHelper("ProjectLayoutConfiguration")
				.addValue(projectId)
				.addValue(tabs)
				.toString();
	}
}