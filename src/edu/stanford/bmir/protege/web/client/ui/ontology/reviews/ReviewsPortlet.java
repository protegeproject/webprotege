package edu.stanford.bmir.protege.web.client.ui.ontology.reviews;

import java.util.Collection;

import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class ReviewsPortlet extends AbstractEntityPortlet {

    private ReviewsGrid grid;

    public ReviewsPortlet(Project project) {
        super(project);
    }

    @Override
    public void initialize() {
        setLayout(new AnchorLayout());
        setBorder(true);
        setPaddings(15);
        setTitle("Reviews");

        grid = new ReviewsGrid(project);
        add(grid, new AnchorLayoutData("100% 100%"));
        doLayout();
    }

    @Override
    public void reload() {
        if (_currentEntity != null) {
            setTitle("Reviews for " + _currentEntity.getBrowserText());
        }

        grid.setEntity(_currentEntity);
        onRefresh();
    }

    @Override
    protected void onRefresh() {
        grid.refresh();
    }

    /*
     * TODO: Implement this method.
     */
    public Collection<EntityData> getSelection() {
        return null;
    }
}
