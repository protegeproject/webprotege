package edu.stanford.bmir.protege.web.client.sharing;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public interface SharingSettingsView extends IsWidget {

    interface ApplyChangesHandler {
        void handleApplyChanges();
    }

    interface CancelHandler {
        void handleCancel();
    }

    void setProjectTitle(@Nonnull String projectTitle);

    void setLinkSharingPermission(Optional<SharingPermission> sharingPermission);

    Optional<SharingPermission> getLinkSharingPermission();

    void setSharingSettings(List<SharingSetting> sharingSettings);

    List<SharingSetting> getSharingSettings();

    void setApplyChangesHandler(ApplyChangesHandler handler);

    void setCancelHandler(CancelHandler handler);
}
