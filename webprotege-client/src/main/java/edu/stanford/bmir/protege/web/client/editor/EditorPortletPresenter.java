package edu.stanford.bmir.protege.web.client.editor;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.tag.TagListPresenter;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.client.viz.VizPanePresenter;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchService;
import edu.stanford.bmir.protege.web.shared.event.ClassFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.NamedIndividualFrameChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
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

    private final ImmutableList<EditorPanePresenter> panePresenters;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final DispatchServiceManager dispatch;

    private final Set<EntityType<?>> displayedTypes = new HashSet<>();

    @Inject
    public EditorPortletPresenter(
            @Nonnull ProjectId projectId,
            @Nonnull SelectionModel selectionModel,
            @Nonnull EditorPortletView view,
            @Nonnull TagListPresenter tagListPresenter,
            @Nonnull EditorPaneSimpleEditorPresenter editorPresenter,
            @Nonnull DisplayNameRenderer displayNameRenderer,
            @Nonnull EditorPaneEntityChangesPresenter changesPresenter,
            @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
            @Nonnull VizPanePresenter vizPresenter,
            @Nonnull Provider<ForbiddenView> forbiddenViewProvider,
            @Nonnull DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer);
        this.view = checkNotNull(view);
        this.tagListPresenter = checkNotNull(tagListPresenter);
        this.permissionChecker = permissionChecker;
        this.dispatch = checkNotNull(dispatch);
        panePresenters = ImmutableList.of(
                checkNotNull(editorPresenter),
                checkNotNull(vizPresenter),
                checkNotNull(changesPresenter)
        );
        displayedTypes.addAll(Arrays.asList(
                CLASS,
                OBJECT_PROPERTY,
                DATA_PROPERTY,
                ANNOTATION_PROPERTY,
                NAMED_INDIVIDUAL,
                DATATYPE
        ));
        view.setEditorPaneChangedHandler(this::handleEditorPaneChanged);
    }

    private void handleEditorPaneChanged() {
        try {
            dispatch.beginBatch();
            handleAfterSetEntity(getSelectedEntity());
        } finally {
            dispatch.executeCurrentBatch();
        }
    }

    public void setDisplayedTypes(EntityType<?> ... entityTypes) {
        displayedTypes.clear();
        displayedTypes.addAll(Arrays.asList(entityTypes));
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(view);
        startPanePresenters(portletUi, eventBus);
        eventBus.addProjectEventHandler(getProjectId(),
                                        ClassFrameChangedEvent.CLASS_FRAME_CHANGED,
                                        this::handleClassFrameChangedEvent);
        eventBus.addProjectEventHandler(getProjectId(),
                                        NamedIndividualFrameChangedEvent.NAMED_INDIVIDUAL_CHANGED,
                                        this::handleIndividualFrameChangedEvent);
        tagListPresenter.start(view.getTagListViewContainer(), eventBus);
        int editorIndex = getEditorIndex(portletUi);
        view.setVisibleIndex(editorIndex);
        setDisplaySelectedEntityNameAsSubtitle(true);
        handleAfterSetEntity(getSelectedEntity());
    }

    private int getEditorIndex(PortletUi portletUi) {
        String editor = portletUi.getNodeProperty("editor", null);
        GWT.log("[EditorPortletPresenter] Editor: " + editor);
        if(editor != null) {
            for(int i = 0; i < panePresenters.size(); i++) {
                EditorPanePresenter panePresenter = panePresenters.get(i);
                if(panePresenter.getCaption().equals(editor)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void startPanePresenters(PortletUi portletUi, WebProtegeEventBus eventBus) {
        for(EditorPanePresenter panePresenter : panePresenters) {
            startPanePresenter(portletUi, eventBus, panePresenter);
        }
    }

    private void startPanePresenter(PortletUi portletUi, WebProtegeEventBus eventBus, EditorPanePresenter panePresenter) {
        permissionChecker.hasPermission(panePresenter.getRequiredAction(), permission -> {
            if(permission) {
                panePresenter.setHasBusy(portletUi);
                panePresenter.setEntityDisplay(this);
                AcceptsOneWidget container = view.addPane(panePresenter.getCaption(),
                                                          panePresenter.getAdditionalStyles());
                panePresenter.start(container, eventBus);
            }
        });

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
            entity.ifPresent(e -> {
                for(EditorPanePresenter panePresenter : panePresenters) {
                    if (view.isPaneVisible(panePresenter.getCaption())) {
                        panePresenter.setEntity(e);
                        setNodeProperty("editor", panePresenter.getCaption());
                    }
                }
            });
            tagListPresenter.setEntity(entity.get());
        }
    }

    private void handleClassFrameChangedEvent(ClassFrameChangedEvent event) {
        if(displayedTypes.contains(CLASS) && getSelectedEntity().equals(Optional.of(event.getEntity()))) {
            reloadEditorIfNotActive();
        }
    }

    private void handleIndividualFrameChangedEvent(NamedIndividualFrameChangedEvent event) {
        if(displayedTypes.contains(NAMED_INDIVIDUAL) && getSelectedEntity().equals(Optional.of(event.getEntity()))) {
            reloadEditorIfNotActive();
        }
    }

    private void reloadEditorIfNotActive() {
        for(EditorPanePresenter presenter : panePresenters) {
            if(!presenter.isActive()) {
                getSelectedEntity().ifPresent(presenter::setEntity);
            }
        }
    }


    private boolean isDisplayedType(Optional<OWLEntity> entity) {
        return entity.map(e -> displayedTypes.contains(e.getEntityType())).orElse(false);
    }

    @Override
    public void dispose() {
        for(EditorPanePresenter panePresenter : panePresenters) {
            panePresenter.dispose();
        }
        super.dispose();
    }

    private boolean isActive(IsWidget widget) {
        return ElementalUtil.isWidgetOrDescendantWidgetActive(widget);
    }
}
