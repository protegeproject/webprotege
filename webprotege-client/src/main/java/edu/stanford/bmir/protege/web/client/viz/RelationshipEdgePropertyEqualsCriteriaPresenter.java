package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.match.CriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.viz.RelationshipEdgePropertyEqualsCriteria;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-08
 */
public class RelationshipEdgePropertyEqualsCriteriaPresenter implements CriteriaPresenter<RelationshipEdgePropertyEqualsCriteria> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RelationshipEdgePropertyEqualsCriteriaView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;


    @Inject
    public RelationshipEdgePropertyEqualsCriteriaPresenter(@Nonnull ProjectId projectId,
                                                           @Nonnull RelationshipEdgePropertyEqualsCriteriaView view,
                                                           @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends RelationshipEdgePropertyEqualsCriteria> getCriteria() {
        return view.getProperty()
                .map(OWLPropertyData::getEntity)
                .map(RelationshipEdgePropertyEqualsCriteria::get);
    }

    @Override
    public void setCriteria(@Nonnull RelationshipEdgePropertyEqualsCriteria criteria) {
        dispatchServiceManager.execute(new GetEntityRenderingAction(projectId, criteria.getProperty()),
                                       result -> view.setProperty((OWLPropertyData) result.getEntityData()));
    }
}
