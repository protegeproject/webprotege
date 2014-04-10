package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/10/2012
 */
public interface WebProtegeDialogInlineValidator<T extends Widget> {

    InlineValidationResult getValidation(T widget);

    boolean shouldCheckOnKeyUp();

    boolean shouldCheckOnValueChange();
}
