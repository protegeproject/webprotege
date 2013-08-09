package edu.stanford.bmir.protege.web.shared.irigen.obo;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.irigen.obo.OBOIdSuffixSettingsEditor;
import edu.stanford.bmir.protege.web.client.rpc.SharingSettingsServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.SharingSetting;
import edu.stanford.bmir.protege.web.client.rpc.data.UserSharingSetting;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsEditorKit;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class OBOSuffixSettingsEditorKit implements SuffixSettingsEditorKit<OBOSuffixSettings> {

    @Override
    public SuffixSettingsId getSchemeId() {
        return OBOSuffixSettings.Id;
    }

    @Override
    public void createDefaultSettings(final AsyncCallback<OBOSuffixSettings> callback) {
        ProjectId projectId = Application.get().getActiveProject().get();
        SharingSettingsServiceManager.getService().getProjectSharingSettings(projectId, new AsyncCallback<ProjectSharingSettings>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(ProjectSharingSettings result) {
                List<UserIdRange> list = new ArrayList<UserIdRange>();
                for(UserSharingSetting sharingSetting : result.getSharingSettings()) {
                    if(sharingSetting.getSharingSetting() == SharingSetting.EDIT) {
                        list.add(new UserIdRange(sharingSetting.getUserId(), UserIdRange.getDefaultStart(), UserIdRange.getDefaultEnd()));
                    }
                }
                callback.onSuccess(new OBOSuffixSettings(list, Optional.of("en")));
            }
        });

    }

    @Override
    public OBOIdSuffixSettingsEditor getEditor() {
        return new OBOIdSuffixSettingsEditor();
    }
}
