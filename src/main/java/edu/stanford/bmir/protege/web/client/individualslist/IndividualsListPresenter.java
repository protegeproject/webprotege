package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateNamedIndividualsResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityResult;
import edu.stanford.bmir.protege.web.client.inject.ActiveProjectIdProvider;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.HasPortletActions;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_INDIVIDUAL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.DELETE_INDIVIDUAL;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class IndividualsListPresenter {

    private static final int SEARCH_DELAY = 700;

    private static final int PAGE_SIZE = 500;

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getFormat("#,##0;(#,##0)");

    private final DispatchServiceManager dispatchServiceManager;

    private final IndividualsListView view;

    private final ActiveProjectIdProvider activeProjectIdProvider;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private Optional<OWLClass> currentType = Optional.empty();

    private final PortletAction createAction = new PortletAction("Create", (action, event) -> handleCreateIndividuals());

    private final PortletAction deleteAction = new PortletAction("Delete", (action, event) -> handleDeleteIndividuals());

    private final Timer searchStringDelayTimer = new Timer() {
        @Override
        public void run() {
            updateList();
        }
    };

    @Inject
    public IndividualsListPresenter(IndividualsListView view,
                                    final SelectionModel selectionModel,
                                    ActiveProjectIdProvider projectIdProvider,
                                    DispatchServiceManager dispatchServiceManager,
                                    LoggedInUserProjectPermissionChecker permissionChecker) {
        this.activeProjectIdProvider = projectIdProvider;
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
        view.addSelectionHandler(event -> {
            OWLNamedIndividualData selectedItem = event.getSelectedItem();
            if (selectedItem != null) {
                selectionModel.setSelection(selectedItem.getEntity());
            }
        });
        view.setSearchStringChangedHandler(() -> {
            searchStringDelayTimer.cancel();
            searchStringDelayTimer.schedule(SEARCH_DELAY);});
        view.setPageNumberChangedHandler(pageNumber -> updateList());

    }

    public void start(AcceptsOneWidget container) {
        GWT.log("[IndividualsListPresenter] Started Individuals List");
        container.setWidget(view.asWidget());
    }

    public void installActions(HasPortletActions hasPortletActions) {
        hasPortletActions.addPortletAction(createAction);
        hasPortletActions.addPortletAction(deleteAction);
        updateButtonStates();
    }

    public void clearType() {
        currentType = Optional.empty();
    }

    public void setType(OWLClass type) {
        if(currentType.equals(Optional.of(type))) {
            return;
        }
        currentType = Optional.of(type);
        view.setPageNumber(1);
        updateList();
    }

    private void updateList() {
        ProjectId projectId = activeProjectIdProvider.get();
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
        });
    }

    private void updateStatusLabel(int displayedIndividuals, int totalIndividuals) {
        String suffix;
        if(totalIndividuals == 1) {
            suffix = " instance";
        }
        else {
            suffix = " instances";
        }
        if(displayedIndividuals == totalIndividuals) {
            view.setStatusMessage(NUMBER_FORMAT.format(displayedIndividuals) + suffix);
        }
        else {
            view.setStatusMessage(NUMBER_FORMAT.format(displayedIndividuals) + " of " + NUMBER_FORMAT.format(
                    totalIndividuals) + suffix);
        }
    }

    private void handleCreateIndividuals() {
        WebProtegeDialog.showDialog(new CreateEntityDialogController(EntityType.NAMED_INDIVIDUAL, createEntityInfo -> {
            final Set<String> browserTexts = createEntityInfo.getBrowserTexts();
            ProjectId projectId = activeProjectIdProvider.get();
            dispatchServiceManager.execute(new CreateNamedIndividualsAction(projectId, currentType, browserTexts), new DispatchServiceCallback<CreateNamedIndividualsResult>() {
                @Override
                public void handleSuccess(CreateNamedIndividualsResult result) {
                    Set<OWLNamedIndividualData> individuals = result.getIndividuals();
                    view.addListData(individuals);
                    if (!individuals.isEmpty()) {
                        OWLNamedIndividualData next = individuals.iterator().next();
                        view.setSelectedIndividual(next);
                    }
                }
            });
        }));
    }

    private void handleDeleteIndividuals() {
        Optional<OWLNamedIndividualData> sel = view.getSelectedIndividual();
        if(!sel.isPresent()) {
            return;
        }
        String subMessage = "Are you sure you want to delete " + sel.get().getBrowserText() + "?";
        MessageBox.showYesNoConfirmBox("Delete individual?",
                                       subMessage,
                                       () -> deleteSelectedIndividuals());
    }


    private void deleteSelectedIndividuals() {
        Collection<OWLNamedIndividualData> selection = view.getSelectedIndividuals();
        if(selection.size() != 1) {
            return;
        }
        deleteIndividual(selection.iterator().next());
    }

    private void deleteIndividual(final OWLNamedIndividualData entity) {
        ProjectId projectId = activeProjectIdProvider.get();
        dispatchServiceManager.execute(new DeleteEntityAction(entity.getEntity(), projectId), new DispatchServiceCallback<DeleteEntityResult>() {
            @Override
            public void handleSuccess(DeleteEntityResult result) {
                view.removeListData(Collections.singleton(entity));
            }
        });
    }

    private void updateButtonStates() {
        createAction.setEnabled(false);
        deleteAction.setEnabled(false);
        permissionChecker.hasPermission(CREATE_INDIVIDUAL, canCreate -> createAction.setEnabled(canCreate));
        permissionChecker.hasPermission(DELETE_INDIVIDUAL, canDelete -> deleteAction.setEnabled(canDelete));
    }

}
