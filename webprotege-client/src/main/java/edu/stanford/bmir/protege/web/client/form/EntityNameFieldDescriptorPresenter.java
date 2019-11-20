package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
public class EntityNameFieldDescriptorPresenter implements FormFieldDescriptorPresenter {

    private static final EntityNameFieldDescriptor DEFAULT_DESCRIPTOR = EntityNameFieldDescriptor.getDefault();

    @Nonnull
    private final EntityNameFieldDescriptorView view;

    @Nonnull
    private final EntityCriteriaPresenter entityCriteriaPresenter;


    @Inject
    public EntityNameFieldDescriptorPresenter(@Nonnull EntityNameFieldDescriptorView view,
                                              @Nonnull EntityCriteriaPresenter entityCriteriaPresenter) {
        this.view = checkNotNull(view);
        this.entityCriteriaPresenter = checkNotNull(entityCriteriaPresenter);
    }

    @Nonnull
    @Override
    public FormFieldDescriptor getFormFieldDescriptor() {
        return EntityNameFieldDescriptor.get(
                view.getPlaceholder(),
                entityCriteriaPresenter.getCriteria().orElse(null)
        );
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormFieldDescriptor formFieldDescriptor) {
        if(!(formFieldDescriptor instanceof EntityNameFieldDescriptor)) {
            return;
        }
        EntityNameFieldDescriptor entityNameFieldDescriptor = (EntityNameFieldDescriptor) formFieldDescriptor;
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
