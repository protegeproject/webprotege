package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import com.gwtext.client.widgets.portal.Portlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TabConfiguration extends GenericConfiguration implements Serializable{
	private static final long serialVersionUID = 9187571983105881720L;

	private String name;

	private String label;

    private String headerCssClass = null;

    private List<TabColumnConfiguration> columns = new ArrayList<TabColumnConfiguration>();

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
	
	public void removeAllPortlet(String javaClassName) {
		for (TabColumnConfiguration column : columns) {
			List<PortletConfiguration> portlets = column.getPortlets();
			for (Iterator<PortletConfiguration> iterator = portlets.iterator(); iterator.hasNext();) {
				PortletConfiguration portlet = iterator.next();
				if (portlet.getName().equals(javaClassName)) {
					iterator.remove();					
				}
			}
		}
	}
}
