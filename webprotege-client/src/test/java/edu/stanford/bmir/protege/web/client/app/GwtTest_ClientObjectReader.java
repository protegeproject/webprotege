package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/01/15
 */
public class GwtTest_ClientObjectReader extends GWTTestCase {

    public static final String VARIABLE_NAME = "myVar";

    public static final String PROPERTY_NAME = "prop";
    
    public static final String VALUE = "theValue";

    @Override
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeJUnit";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        delayTestFinish(10000);
        nativeSetup(VARIABLE_NAME);
    }

    public static native void nativeSetup(String variableName)/*-{
        $wnd[variableName] = {prop : "theValue" };
    }-*/;

    public void test_read() {
        ClientObjectReader<String> reader = new ClientObjectReader<String>(VARIABLE_NAME, new ClientObjectDecoder<String>() {
            @Override
            public String decode(JSONValue json) {
                return json.isObject().get(PROPERTY_NAME).isString().stringValue();
            }
        });
        String val = reader.read();
        assertTrue(val.equals(VALUE));
        finishTest();
    }
}
