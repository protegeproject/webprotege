package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.SubFormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-15
 */
public class FormSubjectFactoryDescriptorPresenter {

    @Nonnull
    private final FormSubjectFactoryDescriptorView view;

    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final ProjectId projectId;

    @Inject
    public FormSubjectFactoryDescriptorPresenter(@Nonnull FormSubjectFactoryDescriptorView view,
                                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                                 @Nonnull ProjectId projectId) {
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.projectId = checkNotNull(projectId);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setDescriptor(@Nonnull FormSubjectFactoryDescriptor descriptor) {
        view.setEntityType(descriptor.getEntityType());
        descriptor.getParent().ifPresent(p -> {
            dispatchServiceManager.execute(new GetEntityRenderingAction(projectId, p),
                                           result -> view.setParentClass((OWLClassData) result.getEntityData()));
        });
    }

    @Nonnull
    public Optional<FormSubjectFactoryDescriptor> getDescriptor() {
        EntityType<?> entityType = view.getEntityType();
        OWLClass parent = view.getParentClass().map(OWLClassData::getEntity).orElse(null);
        return Optional.of(FormSubjectFactoryDescriptor.get(entityType,
                                                            parent,
                                                            Optional.empty()));
    }
}
