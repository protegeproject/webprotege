package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.popupmenu.MenuButton;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/02/16
 */
public class PerspectiveLinkImpl extends Composite implements PerspectiveLink {

    interface PerspectiveLinkWidgetImplUiBinder extends UiBinder<HTMLPanel, PerspectiveLinkImpl> {

    }

    private static PerspectiveLinkWidgetImplUiBinder ourUiBinder = GWT.create(PerspectiveLinkWidgetImplUiBinder.class);


    @UiField
    protected Button button;

    @UiField
    protected MenuButton menuButton;


    private final PopupMenu popupMenu;

    private final PerspectiveId perspectiveId;

    @Inject
    public PerspectiveLinkImpl(PerspectiveId perspectiveId) {
        initWidget(ourUiBinder.createAndBindUi(this));
        popupMenu = new PopupMenu();
        this.perspectiveId = perspectiveId;
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    @UiHandler("menuButton")
    protected void handleMenuClicked(ClickEvent clickEvent) {
        popupMenu.showRelativeTo(menuButton);
    }

    @Override
    public PerspectiveId getPerspectiveId() {
        return perspectiveId;
    }

    @Override
    public void setStyle(@Nonnull String url){
        this.addStyleName(url);
    }

    @Override
    public void addActionHandler(@Nonnull String text, @Nonnull Runnable runnable) {
        popupMenu.addItem(text, runnable);
    }

    @Override
    public void setMenuButtonVisible(boolean visible) {
        menuButton.setVisible(visible);
    }

}