package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsWithPermissionAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsWithPermissionResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-13
 */
public class CopyFormsFromProjectPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CopyFormsFromProjectView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final BusyView busyView;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final FormsMessages formsMessages;

    private Optional<AcceptsOneWidget> container = Optional.empty();

    @Inject
    public CopyFormsFromProjectPresenter(@Nonnull ProjectId projectId,
                                         @Nonnull CopyFormsFromProjectView view,
                                         @Nonnull DispatchServiceManager dispatch,
                                         @Nonnull BusyView busyView,
                                         @Nonnull MessageBox messageBox,
                                         @Nonnull FormsMessages formsMessages) {
        this.projectId = projectId;
        this.view = view;
        this.dispatch = dispatch;
        this.busyView = busyView;
        this.messageBox = messageBox;
        this.formsMessages = formsMessages;
    }

    public void copySelectedForms(@Nonnull FormsCopiedHandler formsCopiedHandler) {
        view.getSelectedProjectId()
            .ifPresent(projectIdToCopyFrom -> {
                dispatch.execute(new CopyFormDescriptorsFromProjectAction(projectId,
                                                                          projectIdToCopyFrom,
                                                                          getSelectedFormIds()),
                                 result -> handleFormsCopied(formsCopiedHandler, result));
            });

    }

    @Nonnull
    public ImmutableList<FormId> getSelectedFormIds() {
        return getSelectedForms().stream().map(FormDescriptor::getFormId).collect(toImmutableList());
    }

    @Nonnull
    public ImmutableList<FormDescriptor> getSelectedForms() {
        return view.getSelectedFormIds()
                   .stream()
                   .collect(toImmutableList());
    }

    private void handleFormsCopied(@Nonnull FormsCopiedHandler formsCopiedHandler,
                                   @Nonnull CopyFormDescriptorsFromProjectResult result) {
        Set<FormId> copiedFormDescriptors = result.getCopiedFormDescriptors()
                .stream()
                .map(FormDescriptor::getFormId)
                .collect(Collectors.toSet());
        String currentLocalName = LocaleInfo.getCurrentLocale().getLocaleName();
        String uncopied = getSelectedForms()
                             .stream()
                             .filter(formDescriptor -> !copiedFormDescriptors.contains(formDescriptor.getFormId()))
                             .map(formDescriptor -> formDescriptor.getLabel().get(currentLocalName))
                             .collect(joining(", "));
        if(!uncopied.isEmpty()) {
            messageBox.showAlert(formsMessages.formsNotCopied_title(),
                                 formsMessages.formsNotCopied_message(uncopied));
        }
        else {
            reload();
            formsCopiedHandler.handleFormsCopied();
        }
    }

    public Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        this.container = Optional.of(container);
        container.setWidget(busyView);
        view.setSelectedProjectChangedHandler(this::updateFormsList);
        reload();
    }

    public void reload() {
        this.container.ifPresent(c -> {
            dispatch.execute(new GetAvailableProjectsWithPermissionAction(BuiltInAction.VIEW_PROJECT.getActionId()),
                             busy -> {
                                 if(busy) {
                                     c.setWidget(busyView);
                                 }
                                 else {
                                     c.setWidget(view);
                                 }
                             },
                             this::handleAvailableProjects);
        });
    }

    private void handleAvailableProjects(GetAvailableProjectsWithPermissionResult result) {
        view.setProjects(result.getProjects()
                               .stream()
                               .filter(ProjectDetails::isNotInTrash)
                               .sorted()
                               .collect(Collectors.toList()));
    }

    private void updateFormsList() {
        view.getSelectedProjectId()
            .ifPresent(this::updateFormsListForProject);
    }

    private void updateFormsListForProject(ProjectId projectId) {
        dispatch.execute(new GetProjectFormDescriptorsAction(projectId),
                         this::updateFormsList);
    }

    private void updateFormsList(GetProjectFormDescriptorsResult result) {
        ImmutableList<FormDescriptor> formDescriptors = result.getFormDescriptors();
        view.setFormDescriptors(formDescriptors);
    }

    interface FormsCopiedHandler {
        void handleFormsCopied();
    }
}
