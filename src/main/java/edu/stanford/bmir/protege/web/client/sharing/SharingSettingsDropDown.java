package edu.stanford.bmir.protege.web.client.sharing;

import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.client.ui.library.dropdown.DropDown;
import edu.stanford.bmir.protege.web.client.ui.library.dropdown.DropDownModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/02/2012
 */
public class SharingSettingsDropDown extends DropDown<SharingPermission> {

    private static List<SharingPermission> sharingPermissionList = new ArrayList<SharingPermission>();
    
    static {
        for(SharingPermission sharingPermission : SharingPermission.values()) {
            if(!sharingPermission.equals(SharingPermission.NONE)) {
                sharingPermissionList.add(sharingPermission);
            }
        }
    }

    public SharingSettingsDropDown() {
        super(new DropDownModel<SharingPermission>() {
            
            public int getSize() {
                return sharingPermissionList.size();
            }

            public SharingPermission getItemAt(int index) {
                return sharingPermissionList.get(index);
            }

            public String getRendering(int index) {
                String name = sharingPermissionList.get(index).toString();
                return "Can " + name.toLowerCase();
            }
        });
    }
}
