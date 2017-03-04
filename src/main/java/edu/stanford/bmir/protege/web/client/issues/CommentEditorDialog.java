package edu.stanford.bmir.protege.web.client.issues;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Oct 2016
 */
public class CommentEditorDialog {

    private final CommentEditorView view;

    @Inject
    public CommentEditorDialog(CommentEditorView view) {
        this.view = view;
    }

    void show(Consumer<String> handler) {
        WebProtegeDialogController<String> controller = new WebProtegeOKCancelDialogController<String>("Comment") {
            @Override
            public Widget getWidget() {
                return view.asWidget();
            }

            @Override
            public Optional<Focusable> getInitialFocusable() {
                return view.getInitialFocusable();
            }

            @Override
            public String getData() {
                return view.getBody();
            }
        };
        controller.setDialogButtonHandler(DialogButton.OK, (data, closer) -> {
            closer.hide();
            handler.accept(data);
        });
        WebProtegeDialog<String> dlg = new WebProtegeDialog<>(controller);
        dlg.show();
    }


    public void setCommentBody(@Nonnull String body) {
        view.setBody(body);
    }
}
