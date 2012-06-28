package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import com.gwtext.client.widgets.Panel;

import edu.stanford.bmir.protege.web.client.model.SystemEventManager;
import edu.stanford.bmir.protege.web.client.model.event.LoginEvent;
import edu.stanford.bmir.protege.web.client.model.event.PermissionEvent;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListener;
import edu.stanford.bmir.protege.web.client.model.listener.SystemListenerAdapter;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class MyWebProtegeTab extends Panel {

    //TODO: make MyWebProtege unspecial!

	protected OntologiesPortlet ontologiesPortlet;
	protected SystemListener systemListener;


	public MyWebProtegeTab() {
		initializeUI();
	}

	public String getLabel() {
		return "My WebProt\u00E9g\u00E9";
	}

	public OntologiesPortlet getOntologiesPortlet() {
		return ontologiesPortlet;
	}

	public void initializeUI() {
//	    setAutoScroll(true);
		ontologiesPortlet = new OntologiesPortlet();
		add(ontologiesPortlet);

		SystemEventManager.getSystemEventManager().addSystemListener(getSystemListener());
	}

    protected SystemListener getSystemListener() {
        if (systemListener == null) {
            this.systemListener = new SystemListenerAdapter() {
                @Override
                public void onLogin(LoginEvent loginEvent) {
                    ontologiesPortlet.onLogin(loginEvent.getUser());
                }

                @Override
                public void onLogout(LoginEvent loginEvent) {
                    ontologiesPortlet.onLogout(loginEvent.getUser());
                }

                @Override
                public void onPermissionsChanged(PermissionEvent permissionEvent) {
                    ontologiesPortlet.onPermissionsChanged(permissionEvent.getPermissions());
                }
            };
        }
        return systemListener;
    }

}
