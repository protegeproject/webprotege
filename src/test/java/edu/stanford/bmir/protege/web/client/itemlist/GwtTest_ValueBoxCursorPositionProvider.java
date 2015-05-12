package edu.stanford.bmir.protege.web.client.itemlist;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/05/15
 */
public class GwtTest_ValueBoxCursorPositionProvider extends GWTTestCase {

    public static final int CURSOR_POSITION = 3;

    private ValueBoxCursorPositionProvider provider;

    private ValueBoxBase<?> valueBox;

    @Override
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeJUnit";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        delayTestFinish(10000);
        valueBox = new TextBox();
        valueBox.setText("ABC");
        valueBox.setCursorPos(CURSOR_POSITION);
        provider = new ValueBoxCursorPositionProvider(valueBox);
    }

    public void test_shouldReturnValueBoxCursorPosition() {
        assertTrue(provider.getCursorPosition() == CURSOR_POSITION);
        finishTest();
    }
}
