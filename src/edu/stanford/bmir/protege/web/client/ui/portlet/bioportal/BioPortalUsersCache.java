package edu.stanford.bmir.protege.web.client.ui.portlet.bioportal;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.data.XmlReader;

import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioportalProposalsManager;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;


public class BioPortalUsersCache {

    private static Map<String, String> id2name = new HashMap<String, String>();

    private static String webProtegeBpUserId = null;

    private static boolean computationInProgress = false;

    //linked BP user; one per WebProtege instance
    private static String currentBpUser = null;


    public static void initBioPortalUsersMap(final AbstractAsyncHandler<Void> callback, boolean forceInit) {

        if (computationInProgress) {
            return;
        }

        if (!forceInit && id2name.size() > 0) {
            return;
        }

        computationInProgress = true;
        XmlReader reader = new XmlReader("userBean", new RecordDef(new FieldDef[]
                                                                                { new StringFieldDef(BioPortalNoteConstants.ID),
                                                                                   new StringFieldDef(BioPortalNoteConstants.USERNAME)
                                                                                 }));

         final Store store = new Store(reader);
         BioportalProposalsManager.getBioportalProposalsManager().getBioPortalUsers(null, new AbstractAsyncHandler<String>() {
            @Override
            public void handleFailure(Throwable caught) {
                GWT.log("Could not retrieve BP users");
                computationInProgress = false;
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }
            @Override
            public void handleSuccess(String usersXml) {
                computationInProgress = false;

               store.loadXmlData(usersXml, false);
               Record[] records = store.getRecords();
               for (int i = 0; i < records.length; i++) {
                   Record rec = store.getRecordAt(i);
                   String name = rec.getAsString(BioPortalNoteConstants.USERNAME);
                   String id = rec.getAsString(BioPortalNoteConstants.ID);
                   id2name.put(id, name);

                   if (name.equals(BioPortalConstants.WP_BP_USERNAME)) {
                       webProtegeBpUserId = id;
                   }
               }

               if (callback != null) {
                   callback.onSuccess(null);
               }
            }
        });
    }

    public static String getBioPortalUserName(String id) {
        String name = id2name.get(id);
        return name == null ? id : name;
    }


    public static String getWebProtegeBpUserId() {
        return webProtegeBpUserId;
    }


    public static String getCurrentBpUser() {
        //FIXME: this should be updated with a listener when the user logs in and out
        //right now, return always the WebProtege user, because we don't have the linking of BP and WP users
        return currentBpUser == null ? getWebProtegeBpUserId() : currentBpUser;
    }


    public static void setCurrentBpUser(String currentBpUser) {
        BioPortalUsersCache.currentBpUser = currentBpUser;
    }

    public static void clearCache() {
        id2name.clear();
        webProtegeBpUserId = null;
    }

}
