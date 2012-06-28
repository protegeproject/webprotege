package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import java.util.Collection;

import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 *
 */
public class WatchedEntitiesPortlet extends AbstractEntityPortlet {

    private WatchedEntitiesGrid grid;

    public WatchedEntitiesPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setLayout(new FitLayout());
        setBorder(true);
        setHeight(300);
        setTitle("Watched Entities " + (GlobalSettings.getGlobalSettings().isLoggedIn() ?
                " for " + GlobalSettings.getGlobalSettings().getUserName() : " - Sign in to see the watched entities!"));

        grid = new WatchedEntitiesGrid(project);
        add(grid);
        onRefresh();
    }

    @Override
    public void reload() {
    }

    @Override
    public void onLogin(String userName) {
        onRefresh();
    }

    @Override
    public void onLogout(String userName) {
        onRefresh();
    }

    @Override
    protected void onRefresh() {
        setTitle("Watched Entities " + (GlobalSettings.getGlobalSettings().isLoggedIn() ?
                " for " + GlobalSettings.getGlobalSettings().getUserName() : " - Sign in to see the watched entities!"));

        grid.reload();
    }

    public Collection<EntityData> getSelection() {
        return null;
    }

}
