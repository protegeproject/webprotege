package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import edu.stanford.bmir.protege.web.client.rpc.data.SharingSetting;
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
public class SharingSettingsDropDown extends DropDown<SharingSetting> {

    private static List<SharingSetting> sharingSettingList = new ArrayList<SharingSetting>();
    
    static {
        for(SharingSetting sharingSetting : SharingSetting.values()) {
            if(!sharingSetting.equals(SharingSetting.NONE)) {
                sharingSettingList.add(sharingSetting);
            }
        }
    }

    public SharingSettingsDropDown() {
        super(new DropDownModel<SharingSetting>() {
            
            public int getSize() {
                return sharingSettingList.size();
            }

            public SharingSetting getItemAt(int index) {
                return sharingSettingList.get(index);
            }

            public String getRendering(int index) {
                String name = sharingSettingList.get(index).toString();
                return "Can " + name.toLowerCase();
            }
        });
    }
}
