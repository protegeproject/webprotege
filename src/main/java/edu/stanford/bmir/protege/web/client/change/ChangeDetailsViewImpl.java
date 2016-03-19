package edu.stanford.bmir.protege.web.client.change;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.diff.DiffLineElementRenderer;
import edu.stanford.bmir.protege.web.client.diff.DiffSourceDocumentRenderer;
import edu.stanford.bmir.protege.web.client.diff.DiffView;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.client.ui.library.timelabel.ElapsedTimeLabel;
import edu.stanford.bmir.protege.web.client.user.UserIcon;
import edu.stanford.bmir.protege.web.server.owlapi.change.Revision;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class ChangeDetailsViewImpl extends Composite implements ChangeDetailsView {

    private DownloadRevisionHandler downloadRevisionHandler = new DownloadRevisionHandler() {
        @Override
        public void handleDownloadRevision(RevisionNumber revisionNumber) {

        }
    };

    private boolean downloadRevisionVisible = false;

    private boolean revertRevisionVisible = false;

    private Optional<RevisionNumber> revision = Optional.absent();

    interface ChangeDetailsViewImplUiBinder extends UiBinder<HTMLPanel, ChangeDetailsViewImpl> {
    }

    private static ChangeDetailsViewImplUiBinder ourUiBinder = GWT.create(ChangeDetailsViewImplUiBinder.class);

    public ChangeDetailsViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        revisionField.setVisible(false);
        tooManyChangesMessage.setVisible(true);
    }

    @UiField
    protected SimplePanel authorUserIcon;

    @UiField
    protected HTML subjectsField;

    @UiField
    protected Label revisionField;

    @UiField
    protected HasText highLevelDescriptionField;

    @UiField
    protected HasText authorField;

    @UiField
    protected ElapsedTimeLabel timestampField;

    @UiField
    protected DiffView diffView;

    @UiField
    protected HasText changeCountField;

    @UiField
    protected Label tooManyChangesMessage;


//    @UiField
//    protected Button revertButton;

    private RevertRevisionHandler revertRevisionHandler = new RevertRevisionHandler() {
        @Override
        public void handleRevertRevision(RevisionNumber revisionNumber) {
        }
    };

    @UiHandler("revisionField")
    protected void showRevisionMenu(ClickEvent event) {
        if(!downloadRevisionVisible && !revertRevisionVisible) {
            return;
        }
        if(!revision.isPresent()) {
            return;
        }
        PopupMenu popupMenu = new PopupMenu();
        if (revertRevisionVisible) {
            popupMenu.addItem(new AbstractUiAction("Revert changes in revision " + revision.get().getValue()) {
                @Override
                public void execute(ClickEvent clickEvent) {
                    revertRevisionHandler.handleRevertRevision(revision.get());
                }
            });
        }
        if(downloadRevisionVisible && revertRevisionVisible) {
            popupMenu.addSeparator();
        }
        if (downloadRevisionVisible) {
            popupMenu.addItem(new AbstractUiAction("Download revision " + revision.get().getValue()) {
                @Override
                public void execute(ClickEvent clickEvent) {
                    downloadRevisionHandler.handleDownloadRevision(revision.get());
                }
            });
        }

        popupMenu.showRelativeTo(revisionField);
    }

    @Override
    public void setSubjects(Collection<OWLEntityData> subjects) {
        StringBuilder sb = new StringBuilder();
        for(OWLEntityData entityData : subjects) {
            sb.append("<span style=\"padding-left: 5px;\">");
            sb.append(entityData.getBrowserText());
            sb.append(",");
            sb.append("</span>");
        }
        subjectsField.setHTML(sb.toString().trim());
    }

    private void updateRevisionButtonState() {
        String suffix;
        if(revertRevisionVisible || downloadRevisionVisible) {
            suffix = " \u25be";
            revisionField.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        }
        else {
            suffix = "";
            revisionField.getElement().getStyle().setCursor(Style.Cursor.DEFAULT);
        }
        if (revision.isPresent()) {
            revisionField.setVisible(true);
            RevisionNumber num = revision.get();
            revisionField.setText("R " + Integer.toString(num.getValueAsInt()) + suffix);
        }
    }


    @Override
    public void setRevertRevisionVisible(boolean visible) {
        revertRevisionVisible = visible;
        updateRevisionButtonState();
    }

    public void setRevertRevisionHandler(RevertRevisionHandler revertRevisionHandler) {
        this.revertRevisionHandler = checkNotNull(revertRevisionHandler);
    }

    @Override
    public void setDownloadRevisionVisible(boolean visible) {
        this.downloadRevisionVisible = visible;
        updateRevisionButtonState();
    }

    @Override
    public void setDownloadRevisionHandler(DownloadRevisionHandler downloadRevisionHandler) {
        this.downloadRevisionHandler = downloadRevisionHandler;
    }


    @Override
    public void setRevision(RevisionNumber revision) {
        this.revision = Optional.of(revision);
        updateRevisionButtonState();
    }

    @Override
    public void setHighLevelDescription(String description) {
        highLevelDescriptionField.setText(checkNotNull(description));
    }

    @Override
    public void setAuthor(UserId author) {
        authorUserIcon.setWidget(UserIcon.get(author));
        authorField.setText(checkNotNull(author).getUserName());
    }

    @Override
    public void setDiff(List<DiffElement<String, SafeHtml>> diff) {
        diffView.setDiff(diff, new DiffLineElementRenderer<SafeHtml>() {
            @Override
            public SafeHtml getRendering(SafeHtml lineElement) {
                return lineElement;
            }
        }, new DiffSourceDocumentRenderer<String>() {
            @Override
            public SafeHtml renderSourceDocument(String document) {
                return new SafeHtmlBuilder().appendHtmlConstant(document).toSafeHtml();
            }
        });
        tooManyChangesMessage.setVisible(false);
    }

    @Override
    public void setDetailsVisible(boolean detailsVisible) {
        diffView.asWidget().setVisible(detailsVisible);
        subjectsField.setVisible(detailsVisible);
    }

    @Override
    public void setChangeCount(int changeCount) {
        changeCountField.setText(Integer.toString(changeCount));
    }

    @Override
    public void setTimestamp(long timestamp) {
        timestampField.setBaseTime(timestamp);
    }


}