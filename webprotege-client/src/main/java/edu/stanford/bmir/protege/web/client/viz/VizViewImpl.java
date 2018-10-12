package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class VizViewImpl extends Composite implements VizView {

    private static final double DEFAULT_RANK_SEP = 0.75;

    interface VizViewImplUiBinder extends UiBinder<HTMLPanel, VizViewImpl> {

    }

    private static VizViewImplUiBinder ourUiBinder = GWT.create(VizViewImplUiBinder.class);

    @UiField
    HTML imageContainer;

    @UiField
    FocusPanel focusPanel;

    @UiField
    ListBox ranksepListBox;

    @UiField
    Button downloadButton;

    private SettingsChangedHandler settingsChangedHandler = () -> {};

    @Inject
    public VizViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        ranksepListBox.setSelectedIndex(3);
        ranksepListBox.addChangeHandler(event -> settingsChangedHandler.handleSettingsChanged());
    }

    @Override
    public void setRendering(@Nonnull String rendering) {
        imageContainer.getElement().setInnerHTML(checkNotNull(rendering));
    }

    @Override
    public double getRankSpacing() {
        String value = ranksepListBox.getSelectedValue();
        if(value.isEmpty()) {
            return DEFAULT_RANK_SEP;
        }
        try {
            return DEFAULT_RANK_SEP * Double.parseDouble(value);
        } catch (NumberFormatException e){
            return DEFAULT_RANK_SEP;
        }
    }

    @Override
    public void setSettingsChangedHandler(@Nonnull SettingsChangedHandler handler) {
        this.settingsChangedHandler = checkNotNull(handler);
    }

    @UiHandler("downloadButton")
    public void downloadButtonClick(ClickEvent event) {
        GWT.log(imageContainer.getElement().getInnerHTML());
        DownloadSvg saver = new DownloadSvg();
        saver.save(imageContainer.getElement(), "entity-graph.png");
    }
}