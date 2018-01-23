package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.client.entity.CreateEntitiesDialogController;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.HasPortletActions;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.DELETE;
import static edu.stanford.bmir.protege.web.client.ui.NumberFormatter.format;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_INDIVIDUAL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.DELETE_INDIVIDUAL;
import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 12/09/2013
 */
@SuppressWarnings("Convert2MethodRef")
public class IndividualsListPresenter {

    private static final int SEARCH_DELAY = 700;

    private static final int PAGE_SIZE = 500;

    private final DispatchServiceManager dispatchServiceManager;

    private final Messages messages;

    private final IndividualsListView view;

    @Nonnull
    private final ProjectId projectId;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final UIAction createAction;

    private final UIAction deleteAction;

    private Optional<OWLClass> currentType = Optional.empty();

    private EntityDisplay entityDisplay = entityData -> { };

    private final Timer searchStringDelayTimer = new Timer() {
        @Override
        public void run() {
            updateList();
        }
    };

    @Nonnull
    private final CreateEntitiesDialogController controller;

    @Inject
    public IndividualsListPresenter(IndividualsListView view,
                                    @Nonnull ProjectId projectId,
                                    final SelectionModel selectionModel,
                                    DispatchServiceManager dispatchServiceManager,
                                    LoggedInUserProjectPermissionChecker permissionChecker,
                                    Messages messages,
                                    @Nonnull CreateEntitiesDialogController controller) {
        this.projectId = projectId;
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
        this.messages = messages;
        this.controller = controller;
        view.addSelectionHandler(event -> {
            OWLNamedIndividualData selectedItem = event.getSelectedItem();
            if (selectedItem != null) {
                selectionModel.setSelection(selectedItem.getEntity());
            }
        });
        view.setSearchStringChangedHandler(() -> {
            searchStringDelayTimer.cancel();
            searchStringDelayTimer.schedule(SEARCH_DELAY);
        });
        view.setPageNumberChangedHandler(pageNumber -> updateList());
        createAction = new PortletAction(messages.create(),
                                         this::handleCreateIndividuals);
        deleteAction = new PortletAction(messages.delete(),
                                         this::handleDeleteIndividuals);
    }

    public void start(AcceptsOneWidget container) {
        GWT.log("[IndividualsListPresenter] Started Individuals List");
        container.setWidget(view.asWidget());
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    public void installActions(HasPortletActions hasPortletActions) {
        hasPortletActions.addAction(createAction);
        hasPortletActions.addAction(deleteAction);
        updateButtonStates();
    }

    public void clearType() {
        currentType = Optional.empty();
    }

    public void setType(OWLClass type) {
        if (currentType.equals(Optional.of(type))) {
            return;
        }
        currentType = Optional.of(type);
        view.setPageNumber(1);
        updateList();
    }

    private void updateList() {
        GetIndividualsAction action = new GetIndividualsAction(projectId,
                currentType.orElse(DataFactory.getOWLThing()),
                view.getSearchString(),
                Optional.of(PageRequest.requestPageWithSize(view.getPageNumber(),
                        PAGE_SIZE)));
        dispatchServiceManager.execute(action, view, result -> {
            view.setListData(result.getIndividuals());
            view.setStatusMessageVisible(true);
            int displayedIndividuals = result.getIndividuals().size();
            int totalIndividuals = result.getTotalIndividuals();
            Page<OWLNamedIndividualData> paginatedResult = result.getPaginatedResult();
            view.setPageCount(paginatedResult.getPageCount());
            view.setPageNumber(paginatedResult.getPageNumber());
            updateStatusLabel(displayedIndividuals, totalIndividuals);
            entityDisplay.setDisplayedEntity(Optional.of(result.getType()));
        });
    }

    private void updateStatusLabel(int displayedIndividuals, int totalIndividuals) {
        String suffix;
        if (totalIndividuals == 1) {
            suffix = " instance";
        }
        else {
            suffix = " instances";
        }
        if (displayedIndividuals == totalIndividuals) {
            view.setStatusMessage(format(displayedIndividuals) + suffix);
        }
        else {
            view.setStatusMessage(format(displayedIndividuals) + " of " + format(totalIndividuals) + suffix);
        }
    }

    private void handleCreateIndividuals() {
        controller.clear();
        controller.setEntityType(EntityType.NAMED_INDIVIDUAL);
        controller.setCreateEntityHandler(createFromText -> {
            dispatchServiceManager.execute(new CreateNamedIndividualsAction(projectId, currentType, createFromText),
                                           result -> {
                                               Set<OWLNamedIndividualData> individuals = result.getIndividuals();
                                               view.addListData(individuals);
                                               if (!individuals.isEmpty()) {
                                                   OWLNamedIndividualData next = individuals.iterator().next();
                                                   view.setSelectedIndividual(next);
                                               }
                                           });
        });
        WebProtegeDialog.showDialog(controller);
    }

    private void handleDeleteIndividuals() {
        Collection<OWLNamedIndividualData> sel = view.getSelectedIndividuals();
        if (sel.isEmpty()) {
            return;
        }
        String subMessage;
        String title;
        if (sel.size() == 1) {
            title = messages.delete_entity_title("individual");
            subMessage = messages.delete_entity_msg("individual", sel.iterator().next().getBrowserText());
        }
        else {
            title = messages.delete_entity_title("individuals");
            subMessage = "Are you sure you want to delete " + sel.size() + " individuals?";
        }
        MessageBox.showConfirmBox(title,
                subMessage,
                CANCEL, DELETE,
                this::deleteSelectedIndividuals,
                CANCEL);
    }


    private void deleteSelectedIndividuals() {
        Collection<OWLNamedIndividualData> selection = view.getSelectedIndividuals();
        Set<OWLEntity> entities = view.getSelectedIndividuals().stream()
                .map(OWLNamedIndividualData::getEntity)
                .collect(toSet());
        dispatchServiceManager.execute(new DeleteEntitiesAction(projectId, entities),
                view,
                result -> updateList());
    }

    private void updateButtonStates() {
        createAction.setEnabled(false);
        deleteAction.setEnabled(false);
        permissionChecker.hasPermission(CREATE_INDIVIDUAL, enabled -> createAction.setEnabled(enabled));
        permissionChecker.hasPermission(DELETE_INDIVIDUAL, enabled -> deleteAction.setEnabled(enabled));
    }

}
