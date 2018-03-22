package edu.stanford.bmir.protege.web.client.editor;

import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.tag.TagListPresenter;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
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

    private final EditorPortletView view;

    private final TagListPresenter tagListPresenter;

    private final EditorPresenter editorPresenter;

    private final Set<EntityType<?>> displayedTypes = new HashSet<>();

    @Inject
    public EditorPortletPresenter(
            @Nonnull ProjectId projectId,
            @Nonnull SelectionModel selectionModel,
            @Nonnull EditorPortletView view,
            @Nonnull TagListPresenter tagListPresenter,
            @Nonnull EditorPresenter editorPresenter) {
        super(selectionModel, projectId);
        this.view = checkNotNull(view);
        this.tagListPresenter = checkNotNull(tagListPresenter);
        this.editorPresenter = checkNotNull(editorPresenter);
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
        portletUi.setWidget(view);
        editorPresenter.updatePermissionBasedItems();
        editorPresenter.setHasBusy(portletUi);
        editorPresenter.start(view.getEditorViewContainer(), eventBus);
        eventBus.addProjectEventHandler(getProjectId(),
                                        ClassFrameChangedEvent.CLASS_FRAME_CHANGED,
                                        this::handleClassFrameChangedEvent);
        editorPresenter.setEntityDisplay(this);
        tagListPresenter.start(view.getTagListViewContainer(), eventBus);
        handleAfterSetEntity(getSelectedEntity());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        if(!entity.isPresent()  || !isDisplayedType(entity)) {
            setNothingSelectedVisible(true);
            setDisplayedEntity(Optional.empty());
            tagListPresenter.clear();
        }
        else {
            setNothingSelectedVisible(false);
            final Optional<OWLEntityContext> editorContext = getEditorContext(entity, getProjectId());
            editorPresenter.setEditorContext(editorContext);
            tagListPresenter.setEntity(entity.get());
        }
    }

    private void handleClassFrameChangedEvent(ClassFrameChangedEvent event) {
        if(displayedTypes.contains(CLASS) && getSelectedEntity().equals(Optional.of(event.getEntity()))) {
            reloadEditorIfNotActive();
        }
    }

    private void reloadEditorIfNotActive() {
        if(!editorPresenter.isActive()) {
            handleAfterSetEntity(getSelectedEntity());
        }
    }


    private boolean isDisplayedType(Optional<OWLEntity> entity) {
        return entity.map(e -> displayedTypes.contains(e.getEntityType())).orElse(false);
    }

    public static Optional<OWLEntityContext> getEditorContext(Optional<OWLEntity> sel, ProjectId projectId) {
        return sel.map(owlEntity -> new OWLEntityContext(projectId, owlEntity));
    }

    @Override
    public void dispose() {
        editorPresenter.dispose();
        super.dispose();
    }
}
