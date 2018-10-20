package edu.stanford.bmir.protege.web.client.change;

import com.google.common.collect.Ordering;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.download.ProjectRevisionDownloader;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.client.viz.Viz;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.REVERT_CHANGES;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 26/02/15
 */
public class ChangeListPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeListView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final Messages messages;

    private boolean revertChangesVisible = false;

    private boolean downloadVisible = false;

    private HasBusy hasBusy = busy -> {
    };

    private Optional<GetProjectChangesAction> lastAction = Optional.empty();

    private HasPagination.PageNumberChangedHandler pageNumberChangedHandler = pageNumber -> {
    };

    @Nonnull
    private MessageBox messageBox;

    private EntityDisplay entityDisplay;

    @Inject
    public ChangeListPresenter(@Nonnull ProjectId projectId,
                               @Nonnull ChangeListView view,
                               @Nonnull DispatchServiceManager dispatch,
                               @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                               @Nonnull Messages messages,
                               @Nonnull MessageBox messageBox) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.dispatch = checkNotNull(dispatch);
        this.messages = checkNotNull(messages);
        this.messageBox = checkNotNull(messageBox);
        this.view.setPageNumberChangedHandler(pageNumber -> pageNumberChangedHandler.handlePageNumberChanged(pageNumber));
    }

    public void setRevertChangesVisible(boolean revertChangesVisible) {
        this.revertChangesVisible = revertChangesVisible;
    }

    public void setDownloadVisible(boolean downloadVisible) {
        this.downloadVisible = downloadVisible;
    }

    @Nonnull
    public ChangeListView getView() {
        return view;
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void displayChangesForProject() {
        this.pageNumberChangedHandler = pageNumber -> displayChangesForProject();
        view.clear();
        PageRequest pageRequest = PageRequest.requestPage(view.getPageNumber());
        GetProjectChangesAction action = new GetProjectChangesAction(projectId, Optional.empty(), pageRequest);
        lastAction = Optional.of(action);
        dispatch.execute(action,
                         hasBusy,
                         this::fillView);
    }

    public void displayChangesForEntity(@Nonnull OWLEntity entity) {
        checkNotNull(entity);
        this.pageNumberChangedHandler = pageNumber -> displayChangesForEntity(entity);
        view.clear();
        PageRequest pageRequest = PageRequest.requestPage(view.getPageNumber());
        GetProjectChangesAction action = new GetProjectChangesAction(projectId, Optional.of(entity), pageRequest);
        dispatch.execute(action,
                         hasBusy,
                         this::fillView);
    }

    public void displayChangesForWatches(@Nonnull UserId userId) {
        checkNotNull(userId);
        this.pageNumberChangedHandler = pageNumber -> displayChangesForWatches(userId);
        view.clear();
        GetWatchedEntityChangesAction action = new GetWatchedEntityChangesAction(projectId, userId);
        dispatch.execute(action,
                         hasBusy,
                         this::fillView);
    }

    public void clear() {
        view.clear();
    }

    private void fillView(HasProjectChanges result) {
        Page<ProjectChange> changes = result.getProjectChanges();
        view.clear();
        permissionChecker.hasPermission(VIEW_CHANGES,
                                        viewChanges -> {
                                            if (viewChanges) {
                                                insertChangesIntoView(changes);
                                            }
                                        });
    }

    private void insertChangesIntoView(Page<ProjectChange> changes) {
        List<ProjectChange> projectChanges = new ArrayList<>(changes.getPageElements());
        Collections.sort(projectChanges, Ordering.compound(Collections.singletonList(
                Ordering.from(new ProjectChangeTimestampComparator()).reverse())));
        long previousTimeStamp = 0;
        view.setPageCount(changes.getPageCount());
        view.setPageNumber(changes.getPageNumber());
        for (final ProjectChange projectChange : projectChanges) {
            long changeTimeStamp = projectChange.getTimestamp();
            if (!TimeUtil.isSameCalendarDay(previousTimeStamp, changeTimeStamp)) {
                previousTimeStamp = changeTimeStamp;
                Date date = new Date(changeTimeStamp);
                view.addSeparator("\u25C9   " + messages.change_changesOn() + " " + DateTimeFormat
                        .getFormat("EEE, d MMM yyyy")
                        .format(date));
            }

            ChangeDetailsView view = new ChangeDetailsViewImpl();
            view.setRevision(projectChange.getRevisionNumber());
            view.setAuthor(projectChange.getAuthor());
            view.setHighLevelDescription(projectChange.getSummary());
            view.setRevertRevisionVisible(false);
            if (revertChangesVisible) {
                permissionChecker.hasPermission(REVERT_CHANGES,
                                                view::setRevertRevisionVisible);
            }
            view.setRevertRevisionHandler(revisionNumber -> ChangeListPresenter.this.handleRevertRevision(
                    projectChange));
            view.setDownloadRevisionHandler(revisionNumber -> {
                ProjectRevisionDownloader downloader = new ProjectRevisionDownloader(
                        projectId,
                        revisionNumber,
                        DownloadFormatExtension.owl);
                downloader.download();
            });
            view.setDownloadRevisionVisible(downloadVisible);
            Page<DiffElement<String, SafeHtml>> page = projectChange.getDiff();
            List<DiffElement<String, SafeHtml>> pageElements = page.getPageElements();
            view.setDiff(pageElements, (int) page.getTotalElements());
            view.setChangeCount(projectChange.getChangeCount());
            view.setTimestamp(changeTimeStamp);
            this.view.addChangeDetailsView(view);
        }
    }

    private void handleRevertRevision(final ProjectChange projectChange) {
        startRevertChangesWorkflow(projectChange);
    }

    private void startRevertChangesWorkflow(final ProjectChange projectChange) {
        String subMessage = messages.change_revertChangesInRevisionQuestion();
        messageBox.showConfirmBox(
                messages.change_revertChangesQuestion(),
                subMessage,
                CANCEL,
                DialogButton.get(messages.change_revert()),
                () -> revertChanges(projectChange),
                CANCEL);
    }

    private void revertChanges(ProjectChange projectChange) {
        final RevisionNumber revisionNumber = projectChange.getRevisionNumber();
        dispatch.execute(new RevertRevisionAction(projectId, revisionNumber),
                         this::handleChangedReverted);
    }

    private void handleChangedReverted(@Nonnull RevertRevisionResult result) {
        RevisionNumber revisionNumber = result.getRevisionNumber();
        messageBox.showMessage(messages.change_revertChangesInRevisionSuccessful(revisionNumber.getValue()));
        lastAction.ifPresent(action -> displayChangesForProject());
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }
}
