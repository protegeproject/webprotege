package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
import edu.stanford.bmir.protege.web.client.portlet.PortletActionHandler;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.YesNoHandler;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityInfo;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class IndividualsListPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private final IndividualsListView view;

    private final ActiveProjectIdProvider activeProjectIdProvider;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private Optional<OWLClass> currentType = Optional.absent();

    private final PortletAction createAction = new PortletAction("Create", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            handleCreateIndividuals();
        }
    });

    private final PortletAction deleteAction = new PortletAction("Delete", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            handleDeleteIndividuals();
        }
    });

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
        view.addSelectionHandler(new SelectionHandler<OWLNamedIndividualData>() {
            @Override
            public void onSelection(com.google.gwt.event.logical.shared.SelectionEvent<OWLNamedIndividualData> event) {
                selectionModel.setSelection(event.getSelectedItem().getEntity());
            }
        });

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
        currentType = Optional.absent();
    }

    public void setType(OWLClass type) {
        currentType = Optional.of(type);
        updateList();
    }

    private void updateList() {
        ProjectId projectId = activeProjectIdProvider.get();
        GetIndividualsAction action = new GetIndividualsAction(projectId, currentType.or(DataFactory.getOWLThing()), Optional.<PageRequest>absent());
        dispatchServiceManager.execute(action, new DispatchServiceCallback<GetIndividualsResult>() {
            @Override
            public void handleSuccess(GetIndividualsResult result) {
                view.setListData(result.getIndividuals());
            }
        });
    }

    private void handleCreateIndividuals() {
        WebProtegeDialog.showDialog(new CreateEntityDialogController(EntityType.NAMED_INDIVIDUAL, new CreateEntityDialogController.CreateEntityHandler() {
            @Override
            public void handleCreateEntity(CreateEntityInfo createEntityInfo) {
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
            }
        }));
    }

    private void handleDeleteIndividuals() {
        Optional<OWLNamedIndividualData> sel = view.getSelectedIndividual();
        if(!sel.isPresent()) {
            return;
        }
        MessageBox.showYesNoConfirmBox("Delete individual?", "Are you sure you want to delete " + sel.get().getBrowserText() + "?", new YesNoHandler() {
            @Override
            public void handleYes() {
                deleteSelectedIndividuals();
            }

            @Override
            public void handleNo() {
            }
        });
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
        permissionChecker.hasWritePermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean result) {
                createAction.setEnabled(result);
                deleteAction.setEnabled(result);
            }
        });
    }

}
