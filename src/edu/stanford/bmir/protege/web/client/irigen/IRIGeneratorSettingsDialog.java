package edu.stanford.bmir.protege.web.client.irigen;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.irigen.IRIGeneratorSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class IRIGeneratorSettingsDialog extends WebProtegeDialog<IRIGeneratorSettings> {

    public IRIGeneratorSettingsDialog() {
        super(new IRIGeneratorSettingsDialogController());
    }


}
