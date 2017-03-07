package edu.stanford.bmir.protege.web.client.projectfeed;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.entitylabel.EntityLabel;
import edu.stanford.bmir.protege.web.client.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.NotePostedEvent;
import edu.stanford.bmir.protege.web.shared.issues.Comment;
import edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import edu.stanford.bmir.protege.web.shared.notes.NoteHeader;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class CommentPostedEventView extends Composite implements ProjectFeedItemDisplay {

    interface CommentPostedEventPanelUiBinder extends UiBinder<HTMLPanel, CommentPostedEventView> {

    }

    private static CommentPostedEventPanelUiBinder ourUiBinder = GWT.create(CommentPostedEventPanelUiBinder.class);

    @UiField
    protected SimplePanel userIconHolder;

    @UiField
    protected InlineLabel userNameLabel;

    @UiField
    protected EntityLabel entityLabel;

    @UiField
    protected ElapsedTimeLabel timeLabel;

    @UiField
    protected HTML bodyLabel;

    @Inject
    public CommentPostedEventView(@Nonnull SelectionModel selectionModel) {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        entityLabel.setSelectionModel(selectionModel);
    }


    public void setValue(CommentPostedEvent event) {
        Comment comment = event.getComment();
        UserId createdBy = comment.getCreatedBy();
        userNameLabel.setText(createdBy.getUserName());
        userIconHolder.setWidget(UserIcon.get(createdBy));
        final java.util.Optional<OWLEntityData> targetAsEntityData = event.getEntity();
        if(targetAsEntityData.isPresent()) {
            entityLabel.setEntity(targetAsEntityData.get());
            entityLabel.setVisible(true);
        }
        else {
            entityLabel.setVisible(false);
        }
        timeLabel.setBaseTime(comment.getCreatedAt());
        final SafeHtmlBuilder builder = new SafeHtmlBuilder();
        bodyLabel.setHTML(builder.appendHtmlConstant(comment.getRenderedBody()).toSafeHtml());
    }

    @Override
    public void updateElapsedTimeDisplay() {
        timeLabel.update();
    }

    @Override
    public UserId getUserId() {
        return UserId.getUserId(userNameLabel.getText());
    }
}