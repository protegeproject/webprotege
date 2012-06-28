package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.junit.client.GWTTestCase;
import edu.stanford.bmir.protege.web.client.ICat;
import edu.stanford.bmir.protege.web.client.WebProtege;

/**
 * @author Jack Elliott <jacke@stanford.edu>
 */
public class ManagerInitializationTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "edu.stanford.bmir.protege.web.WebProtegeTest";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        AdminServiceManager.instance = null;
        ApplicationPropertiesServiceManager.instance= null;
        AuthenticateServiceManager.instance= null;
        ChAOServiceManager.instance= null;
        HierarchyServiceManager.instance= null;
        ICDServiceManager.instance= null;
        NotificationServiceManager.instance= null;
        OntologyServiceManager.instance= null;
        OpenIdServiceManager.instance= null;
        ProjectConfigurationServiceManager.instance= null;
    }

    public void testWebProtegeInitServletMagagers() throws Exception {
        WebProtege  webProtege = new WebProtege();
        webProtege.onModuleLoad();
        assertNotNull(AdminServiceManager.instance);
        assertNotNull(ApplicationPropertiesServiceManager.instance);
        assertNotNull(AuthenticateServiceManager.instance);
        assertNotNull(ChAOServiceManager.instance);
        assertNotNull(HierarchyServiceManager.instance);
        assertNotNull(ICDServiceManager.instance);
        assertNotNull(NotificationServiceManager.instance);
        assertNotNull(OntologyServiceManager.instance);
        assertNotNull(OpenIdServiceManager.instance);
        assertNotNull(ProjectConfigurationServiceManager.instance);
    }

    public void testICatInitServletMagagers() throws Exception {
        final ICat iCat = new ICat();
        iCat.onModuleLoad();
        assertNotNull(AdminServiceManager.instance);
        assertNotNull(ApplicationPropertiesServiceManager.instance);
        assertNotNull(AuthenticateServiceManager.instance);
        assertNotNull(ChAOServiceManager.instance);
        assertNotNull(HierarchyServiceManager.instance);
        assertNotNull(ICDServiceManager.instance);
        assertNotNull(NotificationServiceManager.instance);
        assertNotNull(OntologyServiceManager.instance);
        assertNotNull(OpenIdServiceManager.instance);
        assertNotNull(ProjectConfigurationServiceManager.instance);
    }
}