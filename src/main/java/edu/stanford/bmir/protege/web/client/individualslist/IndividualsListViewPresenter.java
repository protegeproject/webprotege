package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.action.CreateHandler;
import edu.stanford.bmir.protege.web.client.action.DeleteHandler;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateNamedIndividualsResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.DeleteEntityResult;
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
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class IndividualsListViewPresenter {

    private final DispatchServiceManager dispatchServiceManager;

    private IndividualsListView view;

    private ProjectId projectId;

    private Optional<OWLClass> currentType = Optional.absent();

    public IndividualsListViewPresenter(ProjectId projectId, IndividualsListView view, DispatchServiceManager dispatchServiceManager) {
        this.projectId = projectId;
        this.view = view;
        this.view.setCreateHandler(new CreateHandler() {
            @Override
            public void handleCreate() {
                handleCreateIndividuals();
            }
        });
        this.view.setDeleteHandler(new DeleteHandler() {
            @Override
            public void handleDelete() {
                handleDeleteIndividuals();
            }
        });
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void clearType() {
        currentType = Optional.absent();
    }

    public void setType(OWLClass type) {
        currentType = Optional.of(type);
        updateList();
    }

    private void updateList() {
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
                dispatchServiceManager.execute(new CreateNamedIndividualsAction(projectId, currentType, browserTexts), new DispatchServiceCallback<CreateNamedIndividualsResult>() {
                    @Override
                    public void handleSuccess(CreateNamedIndividualsResult result) {
                        GWT.log("Individuals successfully added: " + result.getIndividuals());
                        Set<OWLNamedIndividualData> individuals = result.getIndividuals();
                        view.addListData(individuals);
                        if (!individuals.isEmpty()) {
                            OWLNamedIndividualData next = individuals.iterator().next();
                            GWT.log("Selecting new individual: " + next);
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

    protected void deleteIndividual(final OWLNamedIndividualData entity) {
        dispatchServiceManager.execute(new DeleteEntityAction(entity.getEntity(), projectId), new DispatchServiceCallback<DeleteEntityResult>() {
            @Override
            public void handleSuccess(DeleteEntityResult result) {
                view.removeListData(Collections.singleton(entity));
            }
        });
    }
}
