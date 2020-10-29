package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeOKCancelDialogController;

import javax.annotation.Nonnull;

public class SelectOptionDialogController extends WebProtegeOKCancelDialogController<Integer> {

    private SelectOptionView view;

    public SelectOptionDialogController(SelectOptionView view) {
        super("Select Merge Type");
        this.view = view;
    }

    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    @Nonnull
    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    @Override
    public Integer getData() {
        return view.getSelectedOption();
    }

}
