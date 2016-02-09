package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

public class TabConfiguration extends GenericConfiguration implements Serializable{

	private String name;

	private String label;

    private String headerCssClass = null;

    private List<TabColumnConfiguration> columns = new ArrayList<>();

	private PortletConfiguration controllingPortlet;

	public TabConfiguration() {
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
    public String getHeaderCssClass() {
        return headerCssClass;
    }
    public void setHeaderCssClass(String className) {
        this.headerCssClass = className;
    }
	public List<TabColumnConfiguration> getColumns() {
		return columns;
	}
	public void setColumns(List<TabColumnConfiguration> columns) {
		this.columns = columns;
	}

	@Deprecated
	public PortletConfiguration getControllingPortlet() {
		return controllingPortlet;
	}
	@Deprecated
	public void setControllingPortlet(PortletConfiguration controllingPortlet) {
		this.controllingPortlet = controllingPortlet;
	}

	//convenience accessor methods to read values of commonly expected properties
	public boolean getClosable() {
		return true;
	}

	@Override
	public String toString() {
		return toStringHelper("TabConfiguration")
				.add("name", name)
				.add("label", label)
				.add("closeable", getClosable())
				.add("columns", columns)
				.toString();
	}
}
