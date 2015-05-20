package edu.stanford.bmir.protege.web.client.ui.portlet.html;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import java.util.Map;

/**
 * @author Csongor Nyulas
 */
public class HtmlMessagePortlet extends AbstractOWLEntityPortlet {


    private HtmlTextComponent htmlTextComponent;

    private String title;

    public HtmlMessagePortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
    }

    @Override
    public void initialize() {
        setBorder(true);
        setPaddings(3);
        initConfiguration();
        htmlTextComponent = new HtmlTextComponent(getProject());
        add(htmlTextComponent);
        doLayout();
        refreshContent();
    }

    @Override
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        super.setPortletConfiguration(portletConfiguration);
        htmlTextComponent.setConfigProperties(portletConfiguration.getProperties());

        initConfiguration();
        refreshContent();
    }

    private void initConfiguration() {
        PortletConfiguration config = getPortletConfiguration();
        if (config == null) {
            return;
        }
        Map<String, Object> properties = config.getProperties();
        if (properties == null) {
            return;
        }

        title = (String) properties.get("label");
    }

    private void refreshContent() {
        setTitle(title == null ? "Welcome to " + getProject().getDisplayName() : title);

        htmlTextComponent.refreshContent();
    }

}
