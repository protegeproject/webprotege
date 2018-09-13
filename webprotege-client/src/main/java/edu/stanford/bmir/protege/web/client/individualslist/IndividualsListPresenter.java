package edu.stanford.bmir.protege.web.client.individualslist;

import com.google.common.collect.ImmutableCollection;
import com.google.gwt.core.client.GWT;
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
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.EntityType;
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
import static java.util.stream.Collectors.toSet;
import static org.semanticweb.owlapi.model.EntityType.NAMED_INDIVIDUAL;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 12/09/2013
 */
@SuppressWarnings("Convert2MethodRef")
public class IndividualsListPresenter implements EntityNodeIndex {

    private static final int SEARCH_DELAY = 700;

    private static final int PAGE_SIZE = 200;

    private final DispatchServiceManager dispatchServiceManager;

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

    private EntityDisplay entityDisplay = entityData -> { };

    private final Timer searchStringDelayTimer = new Timer() {
        @Override
        public void run() {
            updateList();
        }
    };

    @Inject
    public IndividualsListPresenter(IndividualsListView view,
                                    @Nonnull ProjectId projectId,
                                    final SelectionModel selectionModel,
                                    DispatchServiceManager dispatchServiceManager,
                                    LoggedInUserProjectPermissionChecker permissionChecker,
                                    HierarchyFieldPresenter hierarchyFieldPresenter,
                                    Messages messages,
                                    @Nonnull CreateEntityPresenter createEntityPresenter, EntityNodeUpdater entityNodeUpdater) {
        this.projectId = projectId;
        this.selectionModel = selectionModel;
        this.permissionChecker = permissionChecker;
        this.view = view;
        this.dispatchServiceManager = dispatchServiceManager;
        this.hierarchyFieldPresenter = hierarchyFieldPresenter;
        this.messages = messages;
        this.createEntityPresenter = createEntityPresenter;
        this.entityNodeUpdater = entityNodeUpdater;
        view.addSelectionHandler(event -> {
            event.getSelectedItem().stream().findFirst().ifPresent(sel -> {
                selectionModel.setSelection(sel.getEntity());
            });
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

    public void start(AcceptsOneWidget container, WebProtegeEventBus eventBus) {
        GWT.log("[IndividualsListPresenter] Started Individuals List");
        container.setWidget(view.asWidget());
        eventBus.addProjectEventHandler(projectId,
                                        DisplayNameSettingsChangedEvent.ON_DISPLAY_LANGUAGE_CHANGED,
                                        event -> setDisplayLanguage(event.getDisplayLanguage()));
        entityNodeUpdater.start(eventBus, this);
        hierarchyFieldPresenter.setEntityType(PrimitiveType.CLASS);
        hierarchyFieldPresenter.start(view.getTypeFieldContainer());
        hierarchyFieldPresenter.setEntityChangedHandler(this::handleTypeChanged);
        resetType();
    }

    private void resetType() {
        hierarchyFieldPresenter.setEntity(DataFactory.getOWLThingData());
        updateList();
    }

    public void setDisplayLanguage(@Nonnull DisplayNameSettings displayLanguage) {
        view.setDisplayLanguage(displayLanguage);
    }


    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    public void installActions(HasPortletActions hasPortletActions) {
        hasPortletActions.addAction(createAction);
        hasPortletActions.addAction(deleteAction);
        updateButtonStates();
    }

    private void updateList() {
        Optional<PageRequest> pageRequest = Optional.of(PageRequest.requestPageWithSize(view.getPageNumber(),
                                                                                        PAGE_SIZE));
        OWLClass type = currentType.orElse(DataFactory.getOWLThing());
        GetIndividualsAction action = new GetIndividualsAction(projectId,
                                                               type,
                                                               view.getSearchString(),
                                                               pageRequest);
        dispatchServiceManager.execute(action, view, result -> {
            Collection<EntityNode> curSel = view.getSelectedIndividuals();
            view.setListData(result.getIndividuals());
            view.setStatusMessageVisible(true);
            int displayedIndividuals = result.getIndividuals().size();
            int totalIndividuals = result.getTotalIndividuals();
            Page<EntityNode> paginatedResult = result.getPaginatedResult();
            view.setPageCount(paginatedResult.getPageCount());
            view.setPageNumber(paginatedResult.getPageNumber());
            updateStatusLabel(displayedIndividuals, totalIndividuals);
            entityDisplay.setDisplayedEntity(Optional.of(result.getType()));
            elementsMap.clear();
            result.getIndividuals()
                  .forEach(node -> elementsMap.put(node.getEntity(), node));
            if (!view.getSelectedIndividuals().equals(curSel)) {
                Optional<EntityNode> selectedIndividual = view.getSelectedIndividual();
                selectedIndividual.ifPresent(sel -> selectionModel.setSelection(sel.getEntity()));
                if (!selectedIndividual.isPresent()) {
                    selectionModel.clearSelection();
                }
            }
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

    private void handleTypeChanged() {
        currentType = hierarchyFieldPresenter.getEntity()
                                             .filter(ed -> ed instanceof OWLClassData)
                                             .map(ed -> (OWLClassData) ed)
                                             .map(ed -> ed.getEntity());
        updateList();
    }

    private void handleCreateIndividuals() {
        createEntityPresenter.createEntities(NAMED_INDIVIDUAL,
                                             individuals -> handleIndividualsCreated(individuals),
                                             (projectId, createFromText, langTag)
                                                     -> new CreateNamedIndividualsAction(projectId,
                                                                                         currentType.orElse(DataFactory.getOWLThing()),
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
        Collection<EntityNode> selection = view.getSelectedIndividuals();
        Set<OWLEntity> entities = view.getSelectedIndividuals().stream()
                                      .map(n -> n.getEntity())
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

    @Override
    public Optional<EntityNode> getNode(@Nonnull OWLEntity entity) {
        return Optional.ofNullable(elementsMap.get(entity));
    }

    @Override
    public void updateNode(@Nonnull EntityNode entityNode) {
        elementsMap.put(entityNode.getEntity(), entityNode);
        view.updateNode(entityNode);
    }

    public void setSelectedIndividual(@Nonnull OWLNamedIndividual individual) {
        checkNotNull(individual);

    }
}
