package edu.stanford.bmir.protege.web.client.watches;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKDialogController;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class WatchTypeDialogController extends WebProtegeOKDialogController<WatchTypeSelection> {

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

    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.empty();
    }

    @Override
    public WatchTypeSelection getData() {
        return view.getSelectedType();
    }

    public void setSelectedType(WatchTypeSelection watchTypeSelection) {
        view.setSelectedType(watchTypeSelection);
    }
}
