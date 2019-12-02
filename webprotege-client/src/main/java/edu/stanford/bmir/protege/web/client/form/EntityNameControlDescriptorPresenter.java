package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
public class EntityNameControlDescriptorPresenter implements FormControlDescriptorPresenter {

    private static final EntityNameControlDescriptor DEFAULT_DESCRIPTOR = EntityNameControlDescriptor.getDefault();

    @Nonnull
    private final EntityNameControlDescriptorView view;

    @Nonnull
    private final EntityCriteriaPresenter entityCriteriaPresenter;


    @Inject
    public EntityNameControlDescriptorPresenter(@Nonnull EntityNameControlDescriptorView view,
                                                @Nonnull EntityCriteriaPresenter entityCriteriaPresenter) {
        this.view = checkNotNull(view);
        this.entityCriteriaPresenter = checkNotNull(entityCriteriaPresenter);
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        return EntityNameControlDescriptor.get(
                view.getPlaceholder(),
                entityCriteriaPresenter.getCriteria().orElse(null)
        );
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        if(!(formControlDescriptor instanceof EntityNameControlDescriptor)) {
            return;
        }
        EntityNameControlDescriptor entityNameFieldDescriptor = (EntityNameControlDescriptor) formControlDescriptor;
        view.clear();
        view.setPlaceholder(entityNameFieldDescriptor.getPlaceholder());
        entityNameFieldDescriptor.getMatchCriteria()
                                 .ifPresent(entityCriteriaPresenter::setCriteria);
    }

    @Override
    public void clear() {
        setFormFieldDescriptor(DEFAULT_DESCRIPTOR);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        entityCriteriaPresenter.start(view.getCriteriaViewContainer());
    }
}
