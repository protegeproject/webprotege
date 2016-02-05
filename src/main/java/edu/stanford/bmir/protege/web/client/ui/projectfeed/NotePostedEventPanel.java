package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.client.ui.library.entitylabel.EntityLabel;
import edu.stanford.bmir.protege.web.client.ui.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.NotePostedEvent;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.notes.NoteHeader;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class NotePostedEventPanel extends Composite implements ProjectFeedItemDisplay {

    interface NotePostedEventPanelUiBinder extends UiBinder<HTMLPanel, NotePostedEventPanel> {

    }

    private static NotePostedEventPanelUiBinder ourUiBinder = GWT.create(NotePostedEventPanelUiBinder.class);


    @UiField
    protected InlineLabel userNameLabel;

    @UiField
    protected EntityLabel entityLabel;

    @UiField
    protected ElapsedTimeLabel timeLabel;

    @UiField
    protected HTML bodyLabel;

    private SelectionModel selectionModel;

    @Inject
    public NotePostedEventPanel(SelectionModel selectionModel) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        this.selectionModel = selectionModel;
        entityLabel.setSelectionModel(selectionModel);
    }


    public void setValue(NotePostedEvent event) {
        final NoteHeader noteHeader = event.getNoteDetails().getNoteHeader();
        userNameLabel.setText(noteHeader.getAuthor().getUserName());
        final Optional<OWLEntityData> targetAsEntityData = event.getTargetAsEntityData();
        if(targetAsEntityData.isPresent()) {
            entityLabel.setEntity(targetAsEntityData.get());
            entityLabel.setVisible(true);
        }
        else {
            entityLabel.setVisible(false);
        }
        timeLabel.setBaseTime(noteHeader.getTimestamp());
        final SafeHtmlBuilder builder = new SafeHtmlBuilder();
        final NoteContent noteContent = event.getNoteDetails().getNoteContent();
        final Optional<String> subject = noteContent.getSubject();
        if (subject.isPresent()) {
            builder.appendHtmlConstant("<span style=\"font-weight: bold; padding-right: 4px;\">" + subject.or("") + ":</span>");
        }
        bodyLabel.setHTML(builder.appendHtmlConstant(noteContent.getBody().or("")).toSafeHtml());
    }

    @Override
    public void updateElapsedTimeDisplay() {
        timeLabel.update();
    }
}