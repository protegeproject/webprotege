package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectConfiguration extends GenericConfiguration implements Serializable {
	private static final long serialVersionUID = -1637798442860308838L;

	private List<TabConfiguration> tabs;
	private String ontologyName;

	//this field has no use, only to whitelist Boolean for the RPC
	private Boolean booleanField;

	public ProjectConfiguration() {
		tabs = new ArrayList<TabConfiguration>();
		ontologyName = null;
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

	public String getOntologyName() {
		return ontologyName;
	}

	public void setOntologyName(String ontologyName) {
		this.ontologyName = ontologyName;
	}

}