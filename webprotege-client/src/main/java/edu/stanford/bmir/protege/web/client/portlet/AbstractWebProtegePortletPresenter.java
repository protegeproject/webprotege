package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettingsChangedEvent;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent.ON_BROWSER_TEXT_CHANGED;


public abstract class AbstractWebProtegePortletPresenter implements WebProtegePortletPresenter, EntityDisplay {

    private final SelectionModel selectionModel;

    private final ProjectId projectId;

    private final HandlerRegistration selectionModelHandlerRegistration;

    private final DisplayNameRenderer displayNameRenderer;

    private Optional<PortletUi> portletUi = Optional.empty();

    private boolean trackSelection = true;

    private boolean displaySelectedEntityNameAsSubtitle = false;

    private Optional<OWLEntityData> displayedEntityData = Optional.empty();

    @Nullable
    private Place lastPlace = null;

    public AbstractWebProtegePortletPresenter(@Nonnull SelectionModel selectionModel,
                                              @Nonnull ProjectId projectId,
                                              @Nonnull DisplayNameRenderer displayNameRenderer) {

        this.selectionModel = checkNotNull(selectionModel);
        this.projectId = checkNotNull(projectId);
        this.displayNameRenderer = displayNameRenderer;
        selectionModelHandlerRegistration = selectionModel.addSelectionChangedHandler(e -> {
                                                                                          if (trackSelection) {
                                                                                              handleBeforeSetEntity(e.getPreviousSelection());
                                                                                              handleAfterSetEntity(e.getLastSelection());
                                                                                          }
                                                                                      }
        );
    }

    /**
     * Stops this presenter from listening to selection changes.
     */
    public void setTrackSelection(boolean trackSelection) {
        this.trackSelection = trackSelection;
    }

    public void setDisplaySelectedEntityNameAsSubtitle(boolean displaySelectedEntityNameAsSubtitle) {
        if (displaySelectedEntityNameAsSubtitle != this.displaySelectedEntityNameAsSubtitle) {
            this.displaySelectedEntityNameAsSubtitle = displaySelectedEntityNameAsSubtitle;
            updateViewTitle();
        }
    }

    @Override
    public final void start(PortletUi portletUi, WebProtegeEventBus eventBus) {
        this.portletUi = Optional.of(portletUi);
        startPortlet(portletUi, eventBus);
        eventBus.addProjectEventHandler(projectId,
                                        ON_BROWSER_TEXT_CHANGED,
                                        this::handleBrowserTextChanged);
        eventBus.addProjectEventHandler(projectId,
                                        DisplayNameSettingsChangedEvent.ON_DISPLAY_LANGUAGE_CHANGED,
                                        this::handlePrefLangChanged);
        eventBus.addApplicationEventHandler(PlaceChangeEvent.TYPE, this::handlePlaceChanged);
    }

    private void handlePlaceChanged(PlaceChangeEvent placeChangeEvent) {
        if(!(lastPlace instanceof ProjectViewPlace)) {
            handlePlaceChangeFromNonProjectViewPlace();
        }
        lastPlace = placeChangeEvent.getNewPlace();
    }

    private void handleBrowserTextChanged(@Nonnull BrowserTextChangedEvent event) {
        getSelectedEntity().ifPresent(selEntity -> {
            if (selEntity.equals(event.getEntity())) {
                setDisplayedEntity(Optional.of(DataFactory.getOWLEntityData(event.getEntity(),
                                                                            event.getShortForms())));
            }
        });
    }

    protected void handlePlaceChangeFromNonProjectViewPlace() {

    }

    private void handlePrefLangChanged(DisplayNameSettingsChangedEvent event) {
        displayedEntityData.ifPresent(sel -> {
            updateViewTitle();
        });
    }

    public abstract void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus);

    public SelectionModel getSelectionModel() {
        return selectionModel;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public void setDisplayedEntity(@Nonnull Optional<OWLEntityData> entityData) {
        displayedEntityData = checkNotNull(entityData);
        updateViewTitle();
    }

    private void updateViewTitle() {
        portletUi.ifPresent(ui -> {
            if (displaySelectedEntityNameAsSubtitle && displayedEntityData.isPresent()) {
                displayedEntityData.ifPresent(entityData -> {
                    String subTitle = displayNameRenderer.getBrowserText(entityData);
                    ui.setSubtitle(subTitle);
                });
            }
            else {
                ui.setSubtitle("");
            }
        });
    }

    protected void setForbiddenVisible(boolean forbiddenVisible) {
        portletUi.ifPresent(ui -> ui.setForbiddenVisible(forbiddenVisible));
    }

    protected void setNothingSelectedVisible(boolean nothingSelectedVisible) {
        portletUi.ifPresent(ui -> ui.setNothingSelectedVisible(nothingSelectedVisible));
    }

    protected void handleBeforeSetEntity(Optional<? extends OWLEntity> existingEntity) {
    }

    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
    }

    @Override
    public void setBusy(boolean busy) {
        portletUi.ifPresent(ui -> ui.setBusy(busy));
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Optional<OWLEntity> getSelectedEntity() {
        return getSelectionModel().getSelection();
    }


    @Override
    public String toString() {
        return toStringHelper("EntityPortlet")
                .addValue(getClass().getName())
                .toString();
    }

    @Override
    public void dispose() {
        selectionModelHandlerRegistration.removeHandler();
        portletUi.ifPresent(ui -> ui.dispose());
    }

    public void setNodeProperty(@Nonnull String property, @Nonnull String value) {
        portletUi.ifPresent(ui -> ui.setNodeProperty(property, value));
    }

    public String getNodeProperty(@Nonnull String property, String defaultValue) {
        return portletUi.map(ui -> ui.getNodeProperty(property, defaultValue)).orElse(defaultValue);
    }
}
