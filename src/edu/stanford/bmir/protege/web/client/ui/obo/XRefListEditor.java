package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.common.base.Optional;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueList;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueListEditorImpl;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.obo.*;
import edu.stanford.bmir.protege.web.client.ui.library.button.DeleteButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class XRefListEditor extends ValueListEditorImpl<OBOXRef> {

    public XRefListEditor() {
        super(new ValueEditorFactory<OBOXRef>() {
            @Override
            public ValueEditor<OBOXRef> createEditor() {
                return new XRefEditorImpl();
            }
        });
    }
}
