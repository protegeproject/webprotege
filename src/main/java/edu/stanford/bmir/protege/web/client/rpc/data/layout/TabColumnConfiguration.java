package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

public class TabColumnConfiguration implements Serializable {

	//width in percentage
	private float width;

	private List<PortletConfiguration> portlets;
	
	public TabColumnConfiguration() {
		portlets = new ArrayList<>();
	}

	public void setPortlets(List<PortletConfiguration> portlets) {
		this.portlets.clear();
		this.portlets.addAll(portlets);
	}

	public List<PortletConfiguration> getPortlets() {
		return new ArrayList<>(portlets);
	}
	
	public void addPortlet(PortletConfiguration portlet) {
		portlets.add(portlet);
	}
	
	public void addPortlet(PortletConfiguration portlet, int index) {
		portlets.add(index, portlet);
	}

	public void setWidth(double width) {
		this.width = (float) width;
	}

	public float getWidth() {
		return width;
	}


	@Override
	public String toString() {
		return toStringHelper("TabColumnConfiguration")
				.add("width", width)
				.add("portletConfigurations", portlets)
				.toString();
	}
	
}
