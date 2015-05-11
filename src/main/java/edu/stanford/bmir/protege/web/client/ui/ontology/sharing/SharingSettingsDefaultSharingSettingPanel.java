package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.client.ui.library.button.AnnotatedRadioButton;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/02/2012
 */
public class SharingSettingsDefaultSharingSettingPanel extends FlowPanel {


    public static final String RADIO_BUTTON_GROUP_NAME = "visibility";
    
    public static final String STYLE_NAME = "web-protege-sharing-settings-visibility-selector-panel";

    private Map<RadioButton, SharingSettingsVisibilityOption> radioButtonMap = new HashMap<RadioButton, SharingSettingsVisibilityOption>();


    private Map<SharingSettingsVisibilityOption, Widget> visibilityOptionSettingsMap = new HashMap<SharingSettingsVisibilityOption, Widget>();

    private final SharingSettingsDropDown defaultSharingSettings;


    public SharingSettingsDefaultSharingSettingPanel() {
        addStyleName(STYLE_NAME);
        for(SharingSettingsVisibilityOption visibilityOption : SharingSettingsVisibilityOption.values()) {
            RadioButton radioButton = createRadioButton(visibilityOption);
            SimplePanel radioButtonHolder = new SimplePanel();
            radioButtonHolder.add(radioButton);
            radioButtonMap.put(radioButton, visibilityOption);
                radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    public void onValueChange(ValueChangeEvent<Boolean> booleanValueChangeEvent) {
                        updateSettingsHolders();
                    }
                });
            add(radioButtonHolder);
        }
        FlowPanel publicSettingHolder = new FlowPanel();
        publicSettingHolder.addStyleName("web-protege-sharing-settings-visibility-summary-box");
        add(publicSettingHolder);
        publicSettingHolder.add(new InlineLabel("Anyone "));
        defaultSharingSettings = new SharingSettingsDropDown();
        publicSettingHolder.add(defaultSharingSettings);
        visibilityOptionSettingsMap.put(SharingSettingsVisibilityOption.PUBLIC, publicSettingHolder);

        FlowPanel privateSettingHolder = new FlowPanel();
        privateSettingHolder.addStyleName("web-protege-sharing-settings-visibility-summary-box");
        add(privateSettingHolder);
        privateSettingHolder.add(new InlineLabel("Access is restricted to the people in the list below"));
        visibilityOptionSettingsMap.put(SharingSettingsVisibilityOption.PRIVATE, privateSettingHolder);
        updateSettingsHolders();

    }

    private void updateSettingsHolders() {
        for(RadioButton radioButton : radioButtonMap.keySet()) {
            SharingSettingsVisibilityOption visibilityOption = radioButtonMap.get(radioButton);
            Widget widget = visibilityOptionSettingsMap.get(visibilityOption);
            if(widget != null) {
                widget.setVisible(radioButton.getValue());
            }
        }
    }
    
    public void setDefaultSharingSetting(SharingPermission sharingPermission) {
        if (!sharingPermission.equals(SharingPermission.NONE)) {
            defaultSharingSettings.setSelectedItem(sharingPermission);
        }
        for(RadioButton radioButton : radioButtonMap.keySet()) {
            SharingSettingsVisibilityOption visibilityOption = radioButtonMap.get(radioButton);
            if(visibilityOption.isForSharingSetting(sharingPermission)) {
                radioButton.setValue(true);
                break;
            }
        }
        updateSettingsHolders();
    }

    public SharingSettingsVisibilityOption getSelectedVisibilityOption() {
        for(RadioButton radioButton : radioButtonMap.keySet()) {
            if(radioButton.getValue().equals(true)) {
                return radioButtonMap.get(radioButton);
            }
        }
        throw new RuntimeException("Missing return value");
    }

    public SharingPermission getDefaultSharingSetting() {
        if(getSelectedVisibilityOption().equals(SharingSettingsVisibilityOption.PRIVATE)) {
            return SharingPermission.NONE;
        }
        else {
            return defaultSharingSettings.getSelectedItem();
        }
    }

    private static RadioButton createRadioButton(SharingSettingsVisibilityOption visibilityOption) {
        return new AnnotatedRadioButton(RADIO_BUTTON_GROUP_NAME, visibilityOption.getSettingTitle(), visibilityOption.getSettingDescription());
    }


    private static String getRadioButtonLabel(SharingSettingsVisibilityOption visibilityOption) {
        return "<div style=\"display: inline-block;\">" +
                "<div>" +
                "<span>" + visibilityOption.getSettingTitle() + "</span>" +
                "</div>" +
                "<div>" +
                "<span class=\"web-protege-label-description\">" + visibilityOption.getSettingDescription() + "</span>" +
                "</div>" +
                "</div>";
    }
    
}
