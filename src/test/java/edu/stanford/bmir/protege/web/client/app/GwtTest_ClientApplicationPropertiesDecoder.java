package edu.stanford.bmir.protege.web.client.app;

import com.google.gwt.junit.client.GWTTestCase;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/01/15
 */
public class GwtTest_ClientApplicationPropertiesDecoder extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeJUnit";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        delayTestFinish(10000);
        nativeSetup();
    }


    public static native void nativeSetup()/*-{
        $wnd['clientApplicationProperties'] = {'application.name' : "AppName" };
    }-*/;

    public void test_decode() {
        ClientApplicationProperties properties = ClientObjectReader.create("clientApplicationProperties",
                new ClientApplicationPropertiesDecoder()).read();

        String appName = properties.getPropertyValue(WebProtegePropertyName.APPLICATION_NAME).get();
        assertTrue("AppName".equals(appName));
        finishTest();
    }

}
