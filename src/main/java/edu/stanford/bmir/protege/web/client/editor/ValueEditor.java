package edu.stanford.bmir.protege.web.client.editor;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.HasDirty;
import edu.stanford.bmir.protege.web.shared.HasWellFormed;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public interface ValueEditor<O> extends HasDirty, HasValueChangeHandlers<Optional<O>>, HasWellFormed, IsWidget {

    void setValue(O object);

    void clearValue();

    Optional<O> getValue();
}
