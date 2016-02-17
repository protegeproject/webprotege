package edu.stanford.bmir.protege.web.client.ui.ontology.individuals;

import com.google.common.base.Optional;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.client.action.CreateHandler;
import edu.stanford.bmir.protege.web.client.action.DeleteHandler;
import edu.stanford.bmir.protege.web.client.action.NullCreateHandler;
import edu.stanford.bmir.protege.web.client.action.NullDeleteHandler;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entitieslist.EntitiesList;
import edu.stanford.bmir.protege.web.client.entitieslist.EntitiesListImpl;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListView;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListViewPresenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractWebProtegeAsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.ui.portlet.PortletActionHandler;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class IndividualsListPortlet extends AbstractOWLEntityPortlet implements IndividualsListView {

    private static final String PRECONFIGURED_CLASS = "showOnlyClass";

    @Override
    public void handleActivated() {
        super.handleActivated();
        updateButtonStates();
    }

    private DeleteHandler deleteHandler = new NullDeleteHandler();

    private CreateHandler createHandler = new NullCreateHandler();


    private final PortletAction createAction = new PortletAction("Create", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            createHandler.handleCreate();
        }
    });

    private final PortletAction deleteAction = new PortletAction("Delete", new PortletActionHandler() {
        @Override
        public void handleActionInvoked(PortletAction action, ClickEvent event) {
            deleteHandler.handleDelete();
        }
    });

    private final IndividualsListViewPresenter presenter;

    /*
     * Retrieved from the project configuration. If it is set,
     * then the individuals list will always display the instances
     * of the preconfigured class.
     */
    // TODO: This needs fixing
    private Optional<OWLClass> preconfiguredClass = Optional.absent();

    private EntitiesList<OWLNamedIndividualData> individualsList;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public IndividualsListPortlet(SelectionModel selectionModel,
                                  EventBus eventBus,
                                  DispatchServiceManager dispatchServiceManager,
                                  LoggedInUserProvider loggedInUserProvider,
                                  ProjectId projectId,
                                  LoggedInUserProjectPermissionChecker permissionChecker) {
        super(selectionModel, eventBus, projectId, loggedInUserProvider);
        this.permissionChecker = permissionChecker;
        setTitle("Individuals");
        individualsList = new EntitiesListImpl<>();
        ScrollPanel sp = new ScrollPanel(individualsList.asWidget());
        getContentHolder().setWidget(sp);
        individualsList.addSelectionHandler(new SelectionHandler<OWLNamedIndividualData>() {
            @Override
            public void onSelection(com.google.gwt.event.logical.shared.SelectionEvent<OWLNamedIndividualData> event) {
                getSelectionModel().setSelection(event.getSelectedItem());
            }
        });
        if (preconfiguredClass != null && preconfiguredClass.isPresent()) {
            // TODO:
//            presenter.setType(preconfiguredClass.get());
        }
        deleteHandler = new NullDeleteHandler();
        createHandler = new NullCreateHandler();
        addPortletAction(createAction);
        addPortletAction(deleteAction);
        presenter = new IndividualsListViewPresenter(getProjectId(), this, dispatchServiceManager);
    }

    @Override
    protected void handleBeforeSetEntity(Optional<OWLEntityData> existingEntity) {
        super.handleBeforeSetEntity(existingEntity);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> entityData) {
        Optional<OWLClass> selectedClass;
        if(preconfiguredClass.isPresent()) {
            selectedClass = preconfiguredClass;
        }
        else if(getSelectionModel().getLastSelectedClassData().isPresent()) {
            selectedClass = Optional.of(getSelectionModel().getLastSelectedClassData().get().getEntity());
        }
        else {
            selectedClass = Optional.absent();
        }


        if(selectedClass.isPresent()) {
            presenter.setType(selectedClass.get());
            updateTitle(entityData);
        }
        else {
            presenter.clearType();
        }
    }

    private void updateTitle(Optional<OWLEntityData> entityData) {
        if(entityData.isPresent()) {
            setTitle("Individuals for " + entityData.get().getBrowserText());
        }
        else {
            setTitle("Individuals (nothing selected)");
        }
    }

    @Override
    public void onPermissionsChanged() {
        updateButtonStates();
    }

    public void updateButtonStates() {
        createAction.setEnabled(false);
        deleteAction.setEnabled(false);
        permissionChecker.hasWritePermission(new DispatchServiceCallback<Boolean>() {
            @Override
            public void handleSuccess(Boolean result) {
                createAction.setEnabled(true);
                deleteAction.setEnabled(true);
            }
        });
    }

    @Override
    public void setListData(List<OWLNamedIndividualData> individuals) {
        individualsList.setListData(individuals);
    }

    @Override
    public void addListData(Collection<OWLNamedIndividualData> individuals) {
        individualsList.addAll(individuals);
    }

    @Override
    public void removeListData(Collection<OWLNamedIndividualData> individuals) {
        individualsList.removeAll(individuals);
    }

    @Override
    public Collection<OWLNamedIndividualData> getSelectedIndividuals() {
        return individualsList.getSelectedEntity().asSet();
    }

    @Override
    public Optional<OWLNamedIndividualData> getSelectedIndividual() {
        return individualsList.getSelectedEntity();
    }

    @Override
    public void setSelectedIndividual(OWLNamedIndividualData individual) {
        individualsList.setSelectedEntity(individual);
    }

    @Override
    public void setCreateHandler(CreateHandler createHandler) {
        this.createHandler = checkNotNull(createHandler);
    }

    @Override
    public void setDeleteHandler(DeleteHandler handler) {
        this.deleteHandler = checkNotNull(handler);
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<OWLNamedIndividualData> handler) {
        return individualsList.addSelectionHandler(handler);
    }
}
