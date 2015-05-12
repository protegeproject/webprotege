package edu.stanford.bmir.protege.web.client.itemlist;

import com.google.gwt.user.client.ui.ValueBoxBase;
import edu.stanford.bmir.protege.web.shared.itemlist.CursorPositionProvider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 */
public class ValueBoxCursorPositionProvider implements CursorPositionProvider {

    private ValueBoxBase<?> valueBox;

    public ValueBoxCursorPositionProvider(ValueBoxBase<?> valueBox) {
        this.valueBox = valueBox;
    }

    @Override
    public int getCursorPosition() {
        return valueBox.getCursorPos();
    }
}
