package edu.stanford.bmir.protege.web.client.watches;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import javax.inject.Inject;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKDialogController;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class WatchTypeDialogController extends WebProtegeOKDialogController<WatchType> {

    private final WatchTypeSelectorView view;

    @Inject
    public WatchTypeDialogController(WatchTypeSelectorView view) {
        super("Select the type of watch");
        this.view = view;
    }

    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return Optional.absent();
    }

    @Override
    public WatchType getData() {
        return view.getSelectedType();
    }

    public void setSelectedType(WatchType watchType) {
        view.setSelectedType(watchType);
    }
}
