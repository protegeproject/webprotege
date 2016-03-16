package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.shared.PortletId;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class PortletChooserPresenter {

    public static interface PortletSelectedHandler {
        void handlePortletSelected(PortletId portletId);
    }


    private final PortletChooserView view;

    @Inject
    public PortletChooserPresenter(PortletChooserView view) {
        this.view = view;
    }

    public void show(final PortletSelectedHandler handler) {
        view.setAvailablePortlets(getPortletDescriptorList());
        WebProtegeOKCancelDialogController<Optional<PortletId>> controller = new WebProtegeOKCancelDialogController<Optional<PortletId>>("Choose view") {
            @Override
            public Widget getWidget() {
                return view.asWidget();
            }

            @Override
            public Optional<Focusable> getInitialFocusable() {
                return Optional.absent();
            }

            @Override
            public Optional<PortletId> getData() {
                return view.getSelectedPortletId();
            }
        };
        WebProtegeDialog<Optional<PortletId>> dlg = new WebProtegeDialog<>(controller);
        dlg.show();
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Optional<PortletId>>() {
            @Override
            public void handleHide(Optional<PortletId> data, WebProtegeDialogCloser closer) {
                closer.hide();
                if(data.isPresent()) {
                    handler.handlePortletSelected(data.get());
                }
            }
        });
    }

    private List<PortletDescriptor> getPortletDescriptorList() {
        return UIFactory.getAvailablePortlets();
    }
}
