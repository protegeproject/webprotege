package edu.stanford.bmir.protege.web.client.ui.library.common;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.HasActive;
import edu.stanford.bmir.protege.web.shared.HasDirty;
import edu.stanford.bmir.protege.web.shared.HasWellFormed;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public interface ValueEditor<O> extends HasDirty, HasValueChangeHandlers<Optional<O>>, HasWellFormed {

    void setValue(O object);

    void clearValue();

    Optional<O> getValue();

    Widget getWidget();

}
