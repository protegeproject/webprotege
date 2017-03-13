package edu.stanford.bmir.protege.web.client.change;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.download.ProjectRevisionDownloader;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.TimeUtil;
import edu.stanford.bmir.protege.web.shared.change.*;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.REVERT_CHANGES;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_CHANGES;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class ChangeListViewPresenter {


    private final ChangeListView view;

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProjectPermissionChecker permissionChecker;

    private boolean revertChangesVisible = false;

    private boolean downloadVisible = false;

    private Optional<ProjectId> projectId = Optional.empty();

    private HasBusy hasBusy = busy -> {};

    @Inject
    public ChangeListViewPresenter(ChangeListView view,
                                   DispatchServiceManager dispatchServiceManager,
                                   LoggedInUserProjectPermissionChecker permissionChecker) {
        this.view = view;
        this.permissionChecker = permissionChecker;
        this.dispatchServiceManager = dispatchServiceManager;
    }

    public void setRevertChangesVisible(boolean revertChangesVisible) {
        this.revertChangesVisible = revertChangesVisible;
    }

    public void setDownloadVisible(boolean downloadVisible) {
        this.downloadVisible = downloadVisible;
    }

    public ChangeListView getView() {
        return view;
    }

    public void setHasBusy(@Nonnull HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    public void setChangesForProject(ProjectId projectId) {
        this.projectId = Optional.of(projectId);
        view.clear();
        dispatchServiceManager.execute(new GetProjectChangesAction(projectId, com.google.common.base.Optional.absent()),
                                       hasBusy,
                                       result -> fillView(result.getChanges(),
                                                          SubjectDisplay.DISPLAY_SUBJECT,
                                                          revertChangesVisible,
                                                          downloadVisible));
    }

    public void setChangesForEntity(ProjectId projectId, OWLEntity entity) {
        this.projectId = Optional.of(projectId);
        view.clear();
        dispatchServiceManager.execute(new GetProjectChangesAction(projectId, com.google.common.base.Optional.of(entity)),
                                       hasBusy,
                                       result -> fillView(result.getChanges(),
                                                          SubjectDisplay.DO_NOT_DISPLAY_SUBJECT,
                                                          revertChangesVisible,
                                                          downloadVisible));
    }

    public void setChangesForWatches(ProjectId projectId, UserId userId) {
        this.projectId = Optional.of(projectId);
        view.clear();
        dispatchServiceManager.execute(new GetWatchedEntityChangesAction(projectId, userId),
                                       result -> fillView(result.getChanges(),
                                                      SubjectDisplay.DISPLAY_SUBJECT,
                                                      revertChangesVisible,
                                                      downloadVisible));
    }

    public void clear() {
        view.clear();
    }

    private void fillView(ImmutableList<ProjectChange> changes,
                          SubjectDisplay subjectDisplay,
                          boolean revertChangesVisible,
                          boolean downloadVisible) {
        view.clear();
        permissionChecker.hasPermission(VIEW_CHANGES,
                                        viewChanges -> {
                                            if(viewChanges) {
                                                insertChangesIntoView(changes,
                                                                      subjectDisplay,
                                                                      revertChangesVisible,
                                                                      downloadVisible);
                                            }
                                        });
    }

    private void insertChangesIntoView(ImmutableList<ProjectChange> changes,
                                       SubjectDisplay subjectDisplay,
                                       boolean revertChangesVisible, boolean downloadVisible) {
        List<ProjectChange> projectChanges = new ArrayList<>(changes);
        Collections.sort(projectChanges, Ordering.compound(Arrays.asList(
                Ordering.from(new ProjectChangeTimestampComparator()).reverse())));
        long previousTimeStamp = 0;
        for (final ProjectChange projectChange : projectChanges) {
            long changeTimeStamp = projectChange.getTimestamp();
            if (!TimeUtil.isSameCalendarDay(previousTimeStamp, changeTimeStamp)) {
                previousTimeStamp = changeTimeStamp;
                Date date = new Date(changeTimeStamp);
                view.addSeparator("\u25C9   Changes on " + DateTimeFormat.getFormat("EEE, d MMM yyyy").format(date));
            }

            ChangeDetailsView view = new ChangeDetailsViewImpl();
            if (subjectDisplay == SubjectDisplay.DISPLAY_SUBJECT) {
                List<OWLEntityData> subjects = new ArrayList<>(projectChange.getSubjects());
                Collections.sort(subjects, (o1, o2) -> o1.compareToIgnoreCase(o2));
                view.setSubjects(subjects);
            }
            view.setRevision(projectChange.getRevisionNumber());
            view.setAuthor(projectChange.getAuthor());
            view.setHighLevelDescription(projectChange.getSummary());
            view.setRevertRevisionVisible(false);
            if(revertChangesVisible) {
                permissionChecker.hasPermission(REVERT_CHANGES,
                                                canRevertChanges -> view.setRevertRevisionVisible(canRevertChanges));
            }
            view.setRevertRevisionHandler(revisionNumber -> ChangeListViewPresenter.this.handleRevertRevision(projectChange));
            view.setDownloadRevisionHandler(revisionNumber -> {
                ProjectRevisionDownloader downloader = new ProjectRevisionDownloader(
                        projectId.get(),
                        revisionNumber,
                        DownloadFormatExtension.owl);
                downloader.download();
            });
            view.setDownloadRevisionVisible(downloadVisible);
            Page<DiffElement<String, SafeHtml>> page = projectChange.getDiff();
            List<DiffElement<String, SafeHtml>> pageElements = page.getPageElements();
            if (page.getPageCount() == 1) {
                view.setDiff(pageElements);
            }
            view.setChangeCount(projectChange.getChangeCount());
            view.setTimestamp(changeTimeStamp);
            this.view.addChangeDetailsView(view);
        }
    }

    private void handleRevertRevision(final ProjectChange projectChange) {
        startRevertChangesWorkflow(projectChange);
    }

    private void startRevertChangesWorkflow(final ProjectChange projectChange) {
        String subMessage = "Are you sure that you want to revert the changes in Revision "
                + projectChange.getRevisionNumber()
                                                                                                           .getValue() + "?";
        MessageBox.showYesNoConfirmBox("Revert changes?",
                                       subMessage,
                                       () -> revertChanges(projectChange));
    }

    private void revertChanges(ProjectChange projectChange) {
        GWT.log("Reverting revision " + projectChange.getRevisionNumber().getValue());
        if (!projectId.isPresent()) {
            return;
        }
        final RevisionNumber revisionNumber = projectChange.getRevisionNumber();
        dispatchServiceManager.execute(new RevertRevisionAction(projectId.get(), revisionNumber),
                                       new DispatchServiceCallback<RevertRevisionResult>() {
                                           @Override
                                           public void handleSuccess(RevertRevisionResult revertRevisionResult) {
                                               MessageBox.showMessage("Changes in revision " + revisionNumber.getValue() + " have been reverted");
                                           }
                                       });
    }

}
