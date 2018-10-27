package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.CreateEntityPresenter;
import edu.stanford.bmir.protege.web.client.entity.EntityNodeUpdater;
import edu.stanford.bmir.protege.web.client.hierarchy.HierarchyFieldPresenter;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.HasPortletActions;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsPageContainingIndividualAction;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsPageContainingIndividualResult;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.DELETE;
import static edu.stanford.bmir.protege.web.client.ui.NumberFormatter.format;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_INDIVIDUAL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.DELETE_INDIVIDUAL;
import static edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettingsChangedEvent.ON_DISPLAY_LANGUAGE_CHANGED;
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.NAMED_INDIVIDUAL;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 12/09/2013
 */
public class IndividualsListPresenter implements EntityNodeIndex {

    private static final int SEARCH_DELAY = 700;

    private static final int PAGE_SIZE = 200;

    private final DispatchServiceManager dsm;

    private final HierarchyFieldPresenter hierarchyFieldPresenter;

    private final Messages messages;

    private final IndividualsListView view;

    @Nonnull
    private final ProjectId projectId;

    private final SelectionModel selectionModel;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private final UIAction createAction;

    private final UIAction deleteAction;

    @Nonnull
    private final CreateEntityPresenter createEntityPresenter;

    private final Map<OWLEntity, EntityNode> elementsMap = new HashMap<>();

    private final EntityNodeUpdater entityNodeUpdater;

    private Optional<OWLClass> currentType = Optional.empty();

    private final Timer searchStringDelayTimer = new Timer() {
        @Override
        public void run() {
            updateList();
        }
    };

    private MessageBox messageBox;

    @Inject
    public IndividualsListPresenter(IndividualsListView view,
                                    @Nonnull ProjectId projectId,
                                    final SelectionModel selectionModel,
                                    DispatchServiceManager dispatchServiceManager,
                                    LoggedInUserProjectPermissionChecker permissionChecker,
                                    HierarchyFieldPresenter hierarchyFieldPresenter,
                                    Messages messages,
                                    @Nonnull CreateEntityPresenter createEntityPresenter, EntityNodeUpdater entityNodeUpdater, MessageBox messageBox) {
        this.projectId = projectId;
        this.selectionModel = selectionModel;
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.dsm = dispatchServiceManager;
        this.hierarchyFieldPresenter = hierarchyFieldPresenter;
        this.messages = messages;
        this.createEntityPresenter = createEntityPresenter;
        this.entityNodeUpdater = entityNodeUpdater;
        this.messageBox = messageBox;
        this.view.addSelectionHandler(this::handleSelectionChangedInView);
        this.view.setSearchStringChangedHandler(this::handleSearchStringChangedInView);
        this.view.setPageNumberChangedHandler(pageNumber -> updateList());
        this.createAction = new PortletAction(messages.create(), "wp-btn-g--create-individual wp-btn-g--create", this::handleCreateIndividuals);
        this.deleteAction = new PortletAction(messages.delete(), "wp-btn-g--delete-individual wp-btn-g--delete", this::handleDeleteIndividuals);
    }


    public void start(AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        GWT.log("[IndividualsListPresenter] Started Individuals List");
        container.setWidget(view.asWidget());
        eventBus.addProjectEventHandler(projectId,
                                        ON_DISPLAY_LANGUAGE_CHANGED,
                                        event -> setDisplayLanguage(event.getDisplayLanguage()));
        entityNodeUpdater.start(eventBus, this);
        hierarchyFieldPresenter.setEntityType(PrimitiveType.CLASS);
        hierarchyFieldPresenter.setEntityChangedHandler(this::handleTypeChanged);
        hierarchyFieldPresenter.setHierarchyId(HierarchyId.CLASS_HIERARCHY);
        hierarchyFieldPresenter.start(view.getTypeFieldContainer(), eventBus);
        view.setInstanceRetrievalTypeChangedHandler(this::handleRetrievalTypeChanged);
        if(selectionModel.getSelection().map(Entity::isOWLNamedIndividual).orElse(false)) {
            selectionModel.getSelection()
                    .filter(Entity::isOWLNamedIndividual)
                    .map(Entity::asOWLNamedIndividual)
                    .ifPresent(this::setDisplayedIndividual);
        }
        else {
            reset();
        }
    }

    public void installActions(HasPortletActions hasPortletActions) {
        hasPortletActions.addAction(createAction);
        hasPortletActions.addAction(deleteAction);
        updateButtonStates();
    }

    private void handleSearchStringChangedInView() {
        searchStringDelayTimer.cancel();
        searchStringDelayTimer.schedule(SEARCH_DELAY);
    }

    private void handleSelectionChangedInView(SelectionEvent<List<EntityNode>> event) {
        event.getSelectedItem().stream().findFirst().ifPresent(sel -> selectionModel.setSelection(sel.getEntity()));
    }

    private void handleRetrievalTypeChanged() {
        updateList();
    }

    public void reset() {
        hierarchyFieldPresenter.setEntity(DataFactory.getOWLThingData());
        updateList();
    }

    public void setDisplayedIndividual(@Nonnull OWLNamedIndividual individual) {
        checkNotNull(individual);
        if (isIndividualContainedInView(individual)) {
            setIndividualSelectedInView(individual);
        }
        else {
            dsm.execute(getPageContaining(individual), this::handlePageContainingIndividual);
        }
    }

    public void setDisplayLanguage(@Nonnull DisplayNameSettings displayLanguage) {
        view.setDisplayLanguage(displayLanguage);
    }

    private void updateList() {
        Optional<PageRequest> pageRequest = Optional.of(PageRequest.requestPageWithSize(view.getPageNumber(),
                                                                                        PAGE_SIZE));
        GetIndividualsAction action = new GetIndividualsAction(projectId,
                                                               currentType,
                                                               view.getSearchString(),
                                                               view.getRetrievalMode(),
                                                               pageRequest);
        dsm.execute(action, view, result -> {
            Page<EntityNode> page = result.getPaginatedResult();
            displayPageOfIndividuals(page);
            selectionModel.getSelection().ifPresent(curSel -> {
                if (!view.getSelectedIndividuals().equals(curSel)) {
                    Optional<EntityNode> selectedIndividual = view.getSelectedIndividual();
                    selectedIndividual.ifPresent(sel -> selectionModel.setSelection(sel.getEntity()));
                    if (!selectedIndividual.isPresent()) {
                        selectionModel.clearSelection();
                    }
                }
            });
        });
    }

    private void displayPageOfIndividuals(Page<EntityNode> page) {
        Collection<EntityNode> curSel = view.getSelectedIndividuals();
        List<EntityNode> individuals = page.getPageElements();
        elementsMap.clear();
        individuals.forEach(node -> elementsMap.put(node.getEntity(), node));
        view.setListData(individuals);
        view.setStatusMessageVisible(true);
        view.setPageCount(page.getPageCount());
        view.setPageNumber(page.getPageNumber());
        updateStatusLabel(page.getPageSize(), page.getTotalElements());

    }

    private void updateStatusLabel(int displayedIndividuals,
                                   long totalIndividuals) {
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

    private void handleTypeChanged() {
        view.setRetrievalModeEnabled(hierarchyFieldPresenter.getEntity().isPresent());
        view.setPageNumber(1);
        currentType = hierarchyFieldPresenter.getEntity()
                .filter(ed -> ed instanceof OWLClassData)
                .map(ed -> (OWLClassData) ed)
                .map(OWLClassData::getEntity);
        updateList();
    }

    private void handleCreateIndividuals() {
        createEntityPresenter.createEntities(NAMED_INDIVIDUAL,
                                             this::handleIndividualsCreated,
                                             (projectId, createFromText, langTag)
                                                     -> new CreateNamedIndividualsAction(projectId,
                                                                                         ImmutableSet.of(currentType.orElse(DataFactory.getOWLThing())),
                                                                                         createFromText,
                                                                                         langTag));
    }

    private void handleIndividualsCreated(ImmutableCollection<EntityNode> individuals) {
        view.addListData(individuals);
        individuals.forEach(node -> elementsMap.put(node.getEntity(), node));
        if (!individuals.isEmpty()) {
            EntityNode next = individuals.iterator().next();
            view.setSelectedIndividual((OWLNamedIndividualData) next.getEntityData());
            selectionModel.setSelection(next.getEntity());
        }
    }

    private void handleDeleteIndividuals() {
        Collection<EntityNode> sel = view.getSelectedIndividuals();
        if (sel.isEmpty()) {
            return;
        }
        String subMessage;
        String title;
        if (sel.size() == 1) {
            String browserText = sel.iterator().next().getBrowserText();
            title = messages.delete_entity_title(browserText);
            subMessage = messages.delete_entity_msg("individual", browserText);
        }
        else {
            title = messages.delete_entity_title("individuals");
            subMessage = "Are you sure you want to delete " + sel.size() + " individuals?";
        }
        messageBox.showConfirmBox(title,
                                  subMessage,
                                  CANCEL, DELETE,
                                  this::deleteSelectedIndividuals,
                                  CANCEL);
    }


    private void deleteSelectedIndividuals() {
        Collection<EntityNode> selection = view.getSelectedIndividuals();
        Set<OWLEntity> entities = view.getSelectedIndividuals().stream()
                .map(EntityNode::getEntity)
                .collect(toSet());
        dsm.execute(new DeleteEntitiesAction(projectId, entities),
                    view,
                                       result -> updateList());
    }

    private void updateButtonStates() {
        createAction.setEnabled(false);
        deleteAction.setEnabled(false);
        permissionChecker.hasPermission(CREATE_INDIVIDUAL, createAction::setEnabled);
        permissionChecker.hasPermission(DELETE_INDIVIDUAL, deleteAction::setEnabled);
    }

    @Override
    public Optional<EntityNode> getNode(@Nonnull OWLEntity entity) {
        return Optional.ofNullable(elementsMap.get(entity));
    }

    @Override
    public void updateNode(@Nonnull EntityNode entityNode) {
        elementsMap.put(entityNode.getEntity(), entityNode);
        view.updateNode(entityNode);
    }

    private GetIndividualsPageContainingIndividualAction getPageContaining(@Nonnull OWLNamedIndividual individual) {
        return new GetIndividualsPageContainingIndividualAction(
                projectId,
                individual,
                hierarchyFieldPresenter
                        .getEntity()
                        .map(ed -> (OWLClass) ed.getEntity()),
                view.getRetrievalMode());
    }

    private void handlePageContainingIndividual(@Nonnull GetIndividualsPageContainingIndividualResult result) {
        displayPageOfIndividuals(result.getPage());
        hierarchyFieldPresenter.setEntity(result.getActualType().getEntityData());
        view.setRetrievalMode(result.getActualMode());
        OWLNamedIndividual individual = result.getIndividual();
        setIndividualSelectedInView(individual);
    }

    private void setIndividualSelectedInView(OWLNamedIndividual individual) {
        EntityNode n = elementsMap.get(individual);
        if (n != null) {
            view.setSelectedIndividual((OWLNamedIndividualData) n.getEntityData());
        }
    }

    private boolean isIndividualContainedInView(@Nonnull OWLNamedIndividual individual) {
        return elementsMap.containsKey(individual);
    }
}
