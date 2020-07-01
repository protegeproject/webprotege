package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.data.EntityFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-17
 */
public class EntityNameControlFilterPresenter implements FormControlFilterPresenter {

    @Nonnull
    private final EntityNameControlFilterView view;

    @Nonnull
    private final EntityNameControlDescriptorDto descriptor;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final ProjectId projectId;

    @AutoFactory
    public EntityNameControlFilterPresenter(@Provided @Nonnull EntityNameControlFilterView view,
                                            @Provided @Nonnull EntityCriteriaPresenter entityCriteriaPresenter,
                                            @Provided @Nonnull DispatchServiceManager dispatch,
                                            @Nonnull EntityNameControlDescriptorDto descriptor,
                                            @Provided @Nonnull ProjectId projectId) {
        this.view = view;
        this.descriptor = descriptor;
        this.dispatch = dispatch;
        this.projectId = projectId;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        descriptor.getMatchCriteria()
                  .ifPresent(view::setEntityMatchCriteria);
    }

    @Nonnull
    @Override
    public Optional<PrimitiveFormControlDataMatchCriteria> getFilter() {
        return view.getEntity()
            .map(EntityIsCriteria::get)
            .map(EntityFormControlDataMatchCriteria::get);
    }

    @Override
    public void setFilter(@Nonnull FormRegionFilter filter) {
        PrimitiveFormControlDataMatchCriteria matchCriteria = filter.getMatchCriteria();
        if(!(matchCriteria instanceof EntityFormControlDataMatchCriteria)) {
            return;
        }
        EntityFormControlDataMatchCriteria entityFormControlDataMatchCriteria = (EntityFormControlDataMatchCriteria) matchCriteria;
        EntityMatchCriteria entityMatchCriteria = entityFormControlDataMatchCriteria.getEntityMatchCriteria();
        if (!(entityMatchCriteria instanceof EntityIsCriteria)) {
            return;
        }
        OWLEntity entity = ((EntityIsCriteria) entityMatchCriteria).getEntity();
        dispatch.execute(new GetEntityRenderingAction(projectId, entity),
                         result -> view.setEntity(result.getEntityData()));
    }

    @Override
    public void clear() {
        view.clear();
    }
}
