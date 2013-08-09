package edu.stanford.bmir.protege.web.client.irigen;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.OKCancelHandler;
import edu.stanford.bmir.protege.web.shared.irigen.IRIGeneratorSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2013
 */
public class IRIGeneratorSettingsDialogController extends WebProtegeOKCancelDialogController<IRIGeneratorSettings> {

    private static final String TITLE = "New Entity Settings";

    final IRIGeneratorSettingsEditor editor = new IRIGeneratorSettingsEditorImpl();

    public IRIGeneratorSettingsDialogController() {
        super(TITLE);

    }

    public IRIGeneratorSettingsEditor getEditor() {
        return editor;
    }

    @Override
    public Widget getWidget() {
        return editor.asWidget();
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return editor.getInitialFocusable();
    }

    @Override
    public IRIGeneratorSettings getData() {
        return editor.getValue().get();
    }
}
