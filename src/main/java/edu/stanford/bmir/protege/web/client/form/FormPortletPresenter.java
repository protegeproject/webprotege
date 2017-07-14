package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.GetFormDescriptorAction;
import edu.stanford.bmir.protege.web.shared.form.SetFormDataAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

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
                                @Nonnull DispatchServiceManager dispatchServiceManager) {
        super(selectionModel, projectId);
        this.projectId = projectId;
        this.formPresenter = formPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        formPresenter.start(portletUi);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        GWT.log("[FormPortletPresenter] handleAfterSetEntity " + entityData);
        if (entityData.isPresent()) {
            setSubject(entityData.get());
        }
        else {
            formPresenter.clear();
        }
    }


    private void setSubject(@Nonnull final OWLEntity entity) {
        checkNotNull(entity);
        FormData formData = formPresenter.getFormData();
        currentSubject.ifPresent(subject -> {
            dispatchServiceManager.execute(new SetFormDataAction(projectId,
                                                                 new FormId("MyForm"),
                                                                 subject,
                                                                 formData),
                                           result -> {});
        });
        currentSubject = Optional.of(entity);
        dispatchServiceManager.execute(new GetFormDescriptorAction(projectId, new FormId("MyForm"), entity),
                                       result -> formPresenter.displayForm(result.getFormDescriptor(), result.getFormData()));
    }

}
