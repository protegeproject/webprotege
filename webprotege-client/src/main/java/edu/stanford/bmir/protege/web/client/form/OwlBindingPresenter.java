package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlClassBinding;
import edu.stanford.bmir.protege.web.shared.form.field.OwlPropertyBinding;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class OwlBindingPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OwlBindingView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public OwlBindingPresenter(@Nonnull ProjectId projectId,
                               @Nonnull OwlBindingView view,
                               @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    public void clear() {
        view.clear();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setBinding(@Nonnull OwlBinding binding) {
        view.clear();
        binding.getOwlProperty()
               .ifPresent(property -> {
                   dispatchServiceManager.execute(new GetEntityRenderingAction(projectId,
                                                                               property),
                                                  result -> view.setProperty(result.getEntityData()));
               });
        if(binding instanceof OwlClassBinding) {
            view.setOwlClassBinding(true);
        }
    }

    @Nonnull
    public Optional<OwlBinding> getBinding() {
        if(view.isOwlClassBinding()) {
            return Optional.of(OwlClassBinding.get());
        }
        return view.getEntity().map(OWLEntityData::getEntity)
            .map(entity -> OwlPropertyBinding.get((OWLProperty) entity));
    }
}
