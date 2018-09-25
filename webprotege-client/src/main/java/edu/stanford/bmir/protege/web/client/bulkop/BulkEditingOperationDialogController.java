package edu.stanford.bmir.protege.web.client.bulkop;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogController;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class BulkEditingOperationDialogController extends WebProtegeDialogController<Void> {

    @Nonnull
    private IsWidget view;

    public BulkEditingOperationDialogController(String title,
                                                List<DialogButton> buttons,
                                                DialogButton defaultButton,
                                                DialogButton escapeButton,
                                                @Nonnull IsWidget view) {
        super(title, buttons, defaultButton, escapeButton);
        this.view = view;
    }

    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    @Nonnull
    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(view)
                .filter(v -> v instanceof HasRequestFocus)
                .map(v -> (HasRequestFocus) v);
    }

    @Override
    public Void getData() {
        return null;
    }
}
