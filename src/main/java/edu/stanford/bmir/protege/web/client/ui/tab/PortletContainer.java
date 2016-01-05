package edu.stanford.bmir.protege.web.client.ui.tab;

import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/01/16
 */
public interface PortletContainer {

    List<EntityPortlet> getPortlets();

    int getColumnCount();

    int getPortletCount(int columnIndex);

    EntityPortlet getPortletAt(int columnIndex, int portletIndex);

    double getColumnWidth(int columnIndex);

    void addColumn(double width);

    void addPortletToColumn(EntityPortlet entityPortlet, int columnIndex);
}
