package edu.stanford.bmir.protege.web.client.sharing;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class SharingSettingsPresenter {

    private final Button view;

    private final SharingSettingsDialogController controller;

    @Inject
    public SharingSettingsPresenter(SharingSettingsDialogController controller) {
        this.controller = controller;
        view = new Button("Share");
        view.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showSharingSettings();
            }
        });
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    private void showSharingSettings() {
        WebProtegeDialog<ProjectSharingSettings> dlg = new WebProtegeDialog<>(controller);
        dlg.show();
    }
}
