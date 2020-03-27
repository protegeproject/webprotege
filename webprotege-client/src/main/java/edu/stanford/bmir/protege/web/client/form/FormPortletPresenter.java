package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
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
    private final FormStackPresenter formStackPresenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private Optional<OWLEntity> currentSubject = Optional.empty();

    @Nonnull
    private Optional<PortletUi> portletUi = Optional.empty();

    @Nonnull
    private final List<FormData> pristineFormsData = new ArrayList<>();

    @Inject
    public FormPortletPresenter(SelectionModel selectionModel,
                                @Nonnull ProjectId projectId,
                                @Nonnull FormStackPresenter formStackPresenter,
                                @Nonnull DispatchServiceManager dispatchServiceManager,
                                DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.projectId = projectId;
        this.formStackPresenter = formStackPresenter;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        GWT.log("[FormPortletPresenter] handleAfterSetEntity " + entityData);
        if(entityData.isPresent()) {
            setSubject(entityData.get());
            setNothingSelectedVisible(false);
        }
        else {
            setNothingSelectedVisible(true);
        }
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.portletUi = Optional.of(portletUi);
        portletUi.addAction(new PortletAction("Expand all", "wp-btn-g--expand-all", formStackPresenter::expandAllFields));
        portletUi.addAction(new PortletAction("Collapse all", "wp-btn-g--collapse-all", formStackPresenter::collapseAllFields));
        formStackPresenter.start(portletUi);
        setDisplaySelectedEntityNameAsSubtitle(true);

        eventBus.addProjectEventHandler(projectId, CLASS_FRAME_CHANGED, this::handleClassFrameChanged);
    }

    private void handleClassFrameChanged(ClassFrameChangedEvent event) {
        GWT.log("[FormPortletPresenter] handleClassFrameChanged");
        if(currentSubject.equals(Optional.of(event.getEntity()))) {
            if(!formStackPresenter.isDirty()) {
                setSubject(event.getEntity());
            }
        }
    }

    private void setSubject(@Nonnull final OWLEntity entity) {
        checkNotNull(entity);
//        if(formStackPresenter.isDirty()) {
            saveCurrentFormData();
//        }
        currentSubject = Optional.of(entity);
        dispatchServiceManager.execute(new GetEntityFormsAction(projectId, entity),
                                       this::displayFormResult);
    }

    private void saveCurrentFormData() {
        GWT.log("[FormPortletPresenter] Saving form data");
        currentSubject.ifPresent(subject -> {
            ImmutableList<FormData> editedForms = formStackPresenter.getForms();
            dispatchServiceManager.execute(new SetEntityFormsDataAction(projectId,
                                                                        subject,
                                                                        ImmutableList.copyOf(pristineFormsData),
                                                                        editedForms),
                                           this,
                                           result -> {});
        });
    }

    private void displayFormResult(GetEntityFormsResult result) {
        GWT.log("[FormPortletPresenter] Display form result: " + result);
        pristineFormsData.clear();
        pristineFormsData.addAll(result.getFormData());
        formStackPresenter.setForms(result.getFormData());
        String formLabel = "Forms";
        portletUi.ifPresent(ui -> ui.setTitle(formLabel));

    }

    private String getFormLabel(Optional<FormDescriptor> formDescriptor) {
        LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
        String formLabel = formDescriptor.map(desc -> desc.getLabel().get(localeInfo.getLocaleName()))
                                         .orElse("Form");
        if(formLabel.isEmpty()) {
            return "Form";
        }
        else {
            return formLabel;
        }

    }

}
