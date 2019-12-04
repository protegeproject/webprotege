package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.match.criteria.RelationshipValueThatIsEqualToCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-02
 */
public class RelationshipValueThatIsEqualToCriteriaPresenter implements CriteriaPresenter<RelationshipValueThatIsEqualToCriteria> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RelationValueThatIsEqualToView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public RelationshipValueThatIsEqualToCriteriaPresenter(@Nonnull ProjectId projectId,
                                                           @Nonnull RelationValueThatIsEqualToView view,
                                                           @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }

    @Override
    public Optional<? extends RelationshipValueThatIsEqualToCriteria> getCriteria() {
        return view.getValue()
            .map(OWLPrimitiveData::getObject)
            .map(RelationshipValueThatIsEqualToCriteria::get);
    }

    @Override
    public void setCriteria(@Nonnull RelationshipValueThatIsEqualToCriteria criteria) {
        OWLPrimitive value = criteria.getValue();
        if(value instanceof OWLEntity) {
            dispatchServiceManager.execute(new GetEntityRenderingAction(projectId, (OWLEntity) value),
                                           result -> view.setValue(result.getEntityData()));
        }
        else if(value instanceof OWLLiteral) {
            view.setValue(OWLLiteralData.get((OWLLiteral) value));
        }
    }
}
