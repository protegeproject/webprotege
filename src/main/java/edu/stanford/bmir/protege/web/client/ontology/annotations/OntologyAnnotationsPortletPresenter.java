package edu.stanford.bmir.protege.web.client.ontology.annotations;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.OntologyFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.inject.Inject;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
@Portlet(id = "portlets.OntologyAnnotations", title = "Ontology Annotations")
public class OntologyAnnotationsPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final AnnotationsView annotationsView;

    private Optional<Set<OWLAnnotation>> lastSet = Optional.absent();

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Inject
    public OntologyAnnotationsPortletPresenter(AnnotationsView annotationsView, SelectionModel selectionModel, DispatchServiceManager dispatchServiceManager, ProjectId projectId, LoggedInUserProjectPermissionChecker permissionChecker) {
        super(selectionModel, projectId);
        this.annotationsView = annotationsView;
        this.dispatchServiceManager = dispatchServiceManager;
        this.permissionChecker = permissionChecker;
        annotationsView.addValueChangeHandler(event -> handleOntologyAnnotationsChanged());
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(new ScrollPanel(annotationsView.asWidget()));

        eventBus.addProjectEventHandler(getProjectId(),
                                        OntologyFrameChangedEvent.TYPE, event -> updateView());
        eventBus.addProjectEventHandler(getProjectId(),
                                        PermissionsChangedEvent.ON_PERMISSIONS_CHANGED,
                                        event -> updateState());
        updateState();
        updateView();

    }

    private void updateView() {
        dispatchServiceManager.execute(new GetOntologyAnnotationsAction(getProjectId()), new DispatchServiceCallback<GetOntologyAnnotationsResult>() {
            @Override
            public void handleSuccess(GetOntologyAnnotationsResult result) {
                final Set<OWLAnnotation> object = new LinkedHashSet<>(result.getAnnotations());
                if (!lastSet.isPresent() || !annotationsView.getValue().equals(Optional.of(object))) {
                    lastSet = Optional.of(object);
                    annotationsView.setValue(object);
                }
            }
        });
    }


    private void updateState() {
        annotationsView.setEnabled(false);
        permissionChecker.hasPermission(BuiltInAction.EDIT_ONTOLOGY_ANNOTATIONS,
                                        canEdit -> annotationsView.setEnabled(canEdit));
    }

    private void handleOntologyAnnotationsChanged() {
        if(!annotationsView.isDirty()) {
            return;
        }
        if(!annotationsView.isWellFormed()) {
            return;
        }
        Optional<Set<OWLAnnotation>> annotations = annotationsView.getValue();
        if (annotations.isPresent() && lastSet.isPresent()) {
            dispatchServiceManager.execute(new SetOntologyAnnotationsAction(getProjectId(), lastSet.get(), annotations.get()), new DispatchServiceCallback<SetOntologyAnnotationsResult>() {
                @Override
                public void handleSuccess(SetOntologyAnnotationsResult result) {

                }
            });
        }
    }
}
