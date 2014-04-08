package edu.stanford.bmir.protege.web.client.ui.portlet.html;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Csongor Nyulas
 */
public class HtmlMessagePortlet extends AbstractOWLEntityPortlet {


    private HtmlTextComponent htmlTextComponent;

    private String title;

    public HtmlMessagePortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setBorder(true);
        setPaddings(3);
        initConfiguration();
        htmlTextComponent = new HtmlTextComponent(getProject());
        add(htmlTextComponent);
        doLayout();
    }

    @Override
    public void setPortletConfiguration(PortletConfiguration portletConfiguration) {
        super.setPortletConfiguration(portletConfiguration);
        htmlTextComponent.setConfigProperties(portletConfiguration.getProperties());

        initConfiguration();
        refreshContent();
    }

    @Override
    public void reload() {
        onRefresh();
    }

    @Override
    protected void afterRender() {
        //onRefresh();
    }

    @Override
    protected void onRefresh() {
        refreshContent();
    }

    public Collection<EntityData> getSelection() {
        return Collections.emptySet();
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
