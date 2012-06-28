package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabColumnConfiguration implements Serializable {
	private static final long serialVersionUID = -5672508668193806999L;
	
	//width in percentage
	private float width;
	private List<PortletConfiguration> portlets;
	
	public TabColumnConfiguration() {
		portlets = new ArrayList<PortletConfiguration>();
	}

	public void setPortlets(List<PortletConfiguration> portlets) {
		this.portlets = portlets;
	}

	public List<PortletConfiguration> getPortlets() {
		return portlets;
	}
	
	public void addPortelt(PortletConfiguration portlet) {
		portlets.add(portlet);
	}
	
	public void addPortelt(PortletConfiguration portlet, int index) {
		portlets.add(index, portlet);
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getWidth() {
		return width;
	}
	
}
