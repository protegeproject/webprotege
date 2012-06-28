package edu.stanford.bmir.protege.web.client.ui.portlet.html;

import java.util.Collection;
import java.util.Map;

import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

/**
 * @author Csongor Nyulas
 */
public class HtmlMessagePortlet extends AbstractEntityPortlet {


    private HtmlTextComponent htmlTextComponent;

    private String title;

    public HtmlMessagePortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setLayout(new FitLayout());
        setBorder(true);
        setPaddings(3);

        initConfiguration();
        htmlTextComponent = new HtmlTextComponent(project);
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

    @Override
    public void onLogin(String userName) {
        //onRefresh();
    }

    @Override
    public void onLogout(String userName) {
        //onRefresh();
    }

    public Collection<EntityData> getSelection() {
        return null;
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
        setTitle(title == null ? "Welcome to " + project.getProjectName() : title);

        htmlTextComponent.refreshContent();
    }

}
