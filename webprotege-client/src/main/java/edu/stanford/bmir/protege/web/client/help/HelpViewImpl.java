package edu.stanford.bmir.protege.web.client.help;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.about.AboutBox;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class HelpViewImpl extends Composite implements HelpView {

    interface HelpViewImplUiBinder extends UiBinder<HTMLPanel, HelpViewImpl> {

    }

    private static HelpViewImplUiBinder ourUiBinder = GWT.create(HelpViewImplUiBinder.class);

    private static final Messages MESSAGES = GWT.create(Messages.class);

    @UiField
    protected Button helpButton;

    private final PopupMenu popupMenu = new PopupMenu();

    @Inject
    public HelpViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        popupMenu.addItem(MESSAGES.about(), () -> {
            AboutBox box = new AboutBox();
            box.show();
        });
    }

    @UiHandler("helpButton")
    protected void handleHelpButtonClicked(ClickEvent clickEvent) {
        popupMenu.showRelativeTo(helpButton);
    }
}