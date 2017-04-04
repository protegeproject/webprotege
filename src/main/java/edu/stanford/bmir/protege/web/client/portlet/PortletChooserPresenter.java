package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.PortletId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class PortletChooserPresenter {

    public interface PortletSelectedHandler {
        void handlePortletSelected(PortletId portletId);
    }


    private final PortletChooserView view;

    private final PortletFactory portletFactory;

    @Inject
    public PortletChooserPresenter(PortletChooserView view, PortletFactory portletFactory) {
        this.view = view;
        this.portletFactory = portletFactory;
    }

    public void show(final PortletSelectedHandler handler) {
        view.setAvailablePortlets(getPortletDescriptorList());
        WebProtegeOKCancelDialogController<Optional<PortletId>> controller = new WebProtegeOKCancelDialogController<Optional<PortletId>>("Choose view") {
            @Override
            public Widget getWidget() {
                return view.asWidget();
            }

            @Nonnull
            @Override
            public java.util.Optional<HasRequestFocus> getInitialFocusable() {
                return java.util.Optional.empty();
            }

            @Override
            public Optional<PortletId> getData() {
                return view.getSelectedPortletId();
            }
        };
        WebProtegeDialog<Optional<PortletId>> dlg = new WebProtegeDialog<>(controller);
        dlg.show();
        controller.setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            closer.hide();
            if(data.isPresent()) {
                handler.handlePortletSelected(data.get());
            }
        });
    }

    private List<PortletDescriptor> getPortletDescriptorList() {
        List<PortletDescriptor> availablePortletDescriptors = portletFactory.getAvailablePortletDescriptors();
        Collections.sort(availablePortletDescriptors, (d1, d2) ->
                d1.getTitle().compareToIgnoreCase(d2.getTitle())
        );
        return availablePortletDescriptors;
    }
}
