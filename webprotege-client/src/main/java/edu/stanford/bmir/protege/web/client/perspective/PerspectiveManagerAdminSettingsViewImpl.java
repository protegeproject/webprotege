package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class PerspectiveManagerAdminSettingsViewImpl extends Composite implements PerspectivesManagerAdminSettingsView {


    interface PerspectiveManagerAdminSettingsViewImplUiBinder extends UiBinder<HTMLPanel, PerspectiveManagerAdminSettingsViewImpl> {
    }

    private static PerspectiveManagerAdminSettingsViewImplUiBinder ourUiBinder = GWT.create(
            PerspectiveManagerAdminSettingsViewImplUiBinder.class);

    @UiField
    Button setAsProjectDefaultButton;

    private MakeDefaultTabsForProjectHandler makeDefaultTabsForProjectHandler = () -> {};

    @Inject
    public PerspectiveManagerAdminSettingsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        setAsProjectDefaultButton.addClickHandler(this::handleMakeDefaultForProjectButtonClicked);
    }

    private void handleMakeDefaultForProjectButtonClicked(ClickEvent event) {
        makeDefaultTabsForProjectHandler.handleMakeDefaultTabsForProject();
    }


    @Override
    public void setMakeDefaultTabsForProjectHandler(@Nonnull MakeDefaultTabsForProjectHandler handler) {
        this.makeDefaultTabsForProjectHandler = checkNotNull(handler);
    }
}