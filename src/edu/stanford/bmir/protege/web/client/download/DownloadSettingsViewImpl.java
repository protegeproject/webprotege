package edu.stanford.bmir.protege.web.client.download;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/07/2013
 */
public class DownloadSettingsViewImpl extends Composite implements DownloadSettingsView {

    interface DownloadSettingsViewImplUiBinder extends UiBinder<HTMLPanel, DownloadSettingsViewImpl> {

    }

    private static DownloadSettingsViewImplUiBinder ourUiBinder = GWT.create(DownloadSettingsViewImplUiBinder.class);

    @UiField
    protected ListBox formatListBox;

    public DownloadSettingsViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        populateListBox();
    }


    private void populateListBox() {
        for(DownloadFormatExtension extension : DownloadFormatExtension.values()) {
            formatListBox.addItem(extension.getDisplayName());
        }
    }

    @Override
    public DownloadFormatExtension getDownloadFormatExtension() {
        int selIndex = formatListBox.getSelectedIndex();
        if(selIndex == 0) {
            return DownloadFormatExtension.rdf;
        }
        else {
            return DownloadFormatExtension.values()[selIndex];
        }
    }

    @Override
    public void setDownloadFormatExtension(DownloadFormatExtension extension) {
        int selIndex = extension.ordinal();
        formatListBox.setSelectedIndex(selIndex);
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return Optional.absent();
    }
}