package edu.stanford.bmir.protege.web.client.download;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.*;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public class DownloadSettingsDialog {

    private static DownloadFormatExtension lastExtension = DownloadFormatExtension.owl;

    public static void showDialog(final DownloadFormatExtensionHandler handler) {
        final DownloadSettingsView view = new DownloadSettingsViewImpl();
        view.setDownloadFormatExtension(lastExtension);
        WebProtegeOKCancelDialogController<DownloadFormatExtension> controller = new WebProtegeOKCancelDialogController<DownloadFormatExtension>("Download project") {
            @Override
            public Widget getWidget() {
                return view.asWidget();
            }

            @Override
            public Optional<Focusable> getInitialFocusable() {
                return view.getInitialFocusable();
            }

            @Override
            public DownloadFormatExtension getData() {
                return view.getDownloadFormatExtension();
            }
        };
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<DownloadFormatExtension>() {
            @Override
            public void handleHide(DownloadFormatExtension data, WebProtegeDialogCloser closer) {
                closer.hide();
                lastExtension = data;
                handler.handleDownload(data);
            }
        });
        WebProtegeDialog<DownloadFormatExtension> dlg = new WebProtegeDialog<DownloadFormatExtension>(controller);
        dlg.show();
    }
}
