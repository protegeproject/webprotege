package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.shared.PortletId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class PortletChooserViewImpl extends Composite implements PortletChooserView {

    interface PortletChooserViewImplUiBinder extends UiBinder<HTMLPanel, PortletChooserViewImpl> {

    }

    private static PortletChooserViewImplUiBinder ourUiBinder = GWT.create(PortletChooserViewImplUiBinder.class);

    public PortletChooserViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    private Map<String, PortletDescriptor> displayName2DescriptorMap = new HashMap<>();

    @UiField
    protected ListBox listBox;

    @Override
    public Optional<PortletId> getSelectedPortletId() {
        String value = listBox.getSelectedValue();
        PortletDescriptor descriptor = displayName2DescriptorMap.get(value);
        if(descriptor == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(descriptor.getPortletId());
        }
    }

    @Override
    public void setAvailablePortlets(Collection<PortletDescriptor> portlets) {
        displayName2DescriptorMap.clear();
        listBox.clear();
        for(PortletDescriptor descriptor : portlets) {
            displayName2DescriptorMap.put(descriptor.getDisplayName(), descriptor);
            listBox.addItem(descriptor.getDisplayName());
        }
    }
}