package edu.stanford.bmir.protege.web.client.obo;

import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class XRefListEditor extends ValueListFlexEditorImpl<OBOXRef> implements HasEnabled {

    @Inject
    public XRefListEditor() {
        super(XRefEditorImpl::new);
    }
}
