package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent.CLASS_FRAME_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@Portlet(id = "portlets.form", title = "Form", tooltip = "Displays a form")
public class FormPortletPresenter extends AbstractWebProtegePortletPresenter {


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final FormPresenter formPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private Optional<OWLEntity> currentSubject = Optional.empty();

    @Inject
    public FormPortletPresenter(SelectionModel selectionModel,
                                @Nonnull ProjectId projectId,
                                @Nonnull FormPresenter formPresenter,
                                @Nonnull DispatchServiceManager dispatchServiceManager,
                                DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.projectId = projectId;
        this.formPresenter = formPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        GWT.log("[FormPortletPresenter] handleAfterSetEntity " + entityData);
        if(entityData.isPresent()) {
            setSubject(entityData.get());
        }
        else {
            formPresenter.clear();
        }
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        formPresenter.start(portletUi);
        eventBus.addProjectEventHandler(projectId, CLASS_FRAME_CHANGED, this::handleClassFrameChanged);
    }

    private void handleClassFrameChanged(ClassFrameChangedEvent event) {
        GWT.log("[FormPortletPresenter] handleClassFrameChanged");
        if(currentSubject.equals(Optional.of(event.getEntity()))) {
            if(!formPresenter.isDirty()) {
                setSubject(event.getEntity());
            }
        }
    }

    private void setSubject(@Nonnull final OWLEntity entity) {
        checkNotNull(entity);
        if(formPresenter.isDirty()) {
            saveCurrentFormData();
        }
        currentSubject = Optional.of(entity);
        dispatchServiceManager.execute(new GetEntityFormAction(projectId, entity),
                                       this::displayFormResult);
    }

    private void saveCurrentFormData() {
        currentSubject.ifPresent(subject -> {
            FormData formData = formPresenter.getFormData();
            dispatchServiceManager.execute(new SetEntityFormDataAction(projectId,
                                                                       subject,
                                                                       formData),
                                           result -> {
                                           });
        });
    }

    private void displayFormResult(GetEntityFormResult result) {
        GWT.log("[FormPortletPresenter] Display form result: " + result);
        Optional<FormDescriptor> formDescriptor = result.getFormDescriptor();
        if(formDescriptor.isPresent()) {
            formPresenter.displayForm(formDescriptor.get(), result.getFormData());
        }
        else {
            formPresenter.clear();
        }

    }

}
