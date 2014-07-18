package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Collection;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 *
 */
public class WatchedEntitiesPortlet extends AbstractOWLEntityPortlet {

    private WatchedEntitiesGrid grid;

    public WatchedEntitiesPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setLayout(new FitLayout());
        setBorder(true);
        setHeight(300);
        setTitle(generateTitle());

        grid = new WatchedEntitiesGrid(getProject());
        add(grid);
        onRefresh();
    }

    private String generateTitle() {
        return "Watched Entities " + (Application.get().isGuestUser() ? " - Sign in to see the watched entities" : " for " + Application.get().getUserDisplayName());
    }

    @Override
    public void reload() {
    }

    @Override
    public void onLogin(UserId userId) {
        onRefresh();
    }

    @Override
    public void onLogout(UserId userId) {
        onRefresh();
    }

    @Override
    protected void onRefresh() {
        setTitle(generateTitle());

        grid.reload();
    }
}
