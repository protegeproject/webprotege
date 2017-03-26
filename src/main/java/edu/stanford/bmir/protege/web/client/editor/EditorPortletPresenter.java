package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent.ON_USER_LOGGED_IN;
import static edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent.ON_USER_LOGGED_OUT;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;
import static org.semanticweb.owlapi.model.EntityType.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/04/2013
 */
@Portlet(id = "portlets.EntityEditor",
        title = "Entity Editor",
        tooltip = "Displays a simple property-value oriented description of the selected class, property or individual for viewing and editing.")
public class EditorPortletPresenter extends AbstractWebProtegePortletPresenter {

    private EditorPresenter editorPresenter;

    private final Widget editorView;

    private final Set<EntityType<?>> displayedTypes = new HashSet<>();

    @Inject
    public EditorPortletPresenter(
            SelectionModel selectionModel,
            final ProjectId projectId,
            EditorPresenter editorPresenter) {
        super(selectionModel, projectId);
        this.editorPresenter = editorPresenter;
        editorView = editorPresenter.getView();
        displayedTypes.addAll(Arrays.asList(
                CLASS,
                OBJECT_PROPERTY,
                DATA_PROPERTY,
                ANNOTATION_PROPERTY,
                NAMED_INDIVIDUAL,
                DATATYPE
        ));
    }

    public void setDisplayedTypes(EntityType<?> ... entityTypes) {
        displayedTypes.clear();
        displayedTypes.addAll(Arrays.asList(entityTypes));
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(editorView);
        editorPresenter.updatePermissionBasedItems();
        editorPresenter.setHasBusy(portletUi);
        eventBus.addProjectEventHandler(getProjectId(),
                                        ON_PERMISSIONS_CHANGED,
                                        event -> editorPresenter.updatePermissionBasedItems());
        eventBus.addApplicationEventHandler(ON_USER_LOGGED_IN,
                                            event -> editorPresenter.updatePermissionBasedItems());
        eventBus.addApplicationEventHandler(ON_USER_LOGGED_OUT,
                                            event -> editorPresenter.updatePermissionBasedItems());
        editorPresenter.setEntityDisplay(this);
        handleAfterSetEntity(getSelectedEntity());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        if(!entity.isPresent()  || !isDisplayedType(entity)) {
            setNothingSelectedVisible(true);
            setDisplayedEntity(Optional.empty());
        }
        else {
            setNothingSelectedVisible(false);
            final Optional<OWLEntityContext> editorContext = getEditorContext(entity, getProjectId());
            editorPresenter.setEditorContext(editorContext);
        }
    }



    private boolean isDisplayedType(Optional<OWLEntity> entity) {
        return entity.map(e -> displayedTypes.contains(e.getEntityType())).orElse(false);
    }

    public static Optional<OWLEntityContext> getEditorContext(Optional<OWLEntity> sel, ProjectId projectId) {
        if(!sel.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new OWLEntityContext(projectId, sel.get()));
    }

    @Override
    public void dispose() {
        editorPresenter.dispose();
        super.dispose();
    }
}
