package edu.stanford.bmir.protege.web.client.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.client.change.ChangeListView;
import edu.stanford.bmir.protege.web.client.change.ChangeListViewImpl;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSettingsEditor;
import edu.stanford.bmir.protege.web.client.crud.EntityCrudKitSettingsEditorImpl;
import edu.stanford.bmir.protege.web.client.crud.obo.UserIdRangeEditor;
import edu.stanford.bmir.protege.web.client.crud.obo.UserIdRangeEditorImpl;
import edu.stanford.bmir.protege.web.client.form.FormView;
import edu.stanford.bmir.protege.web.client.form.FormViewImpl;
import edu.stanford.bmir.protege.web.client.individualslist.IndividualsListView;
import edu.stanford.bmir.protege.web.client.issues.*;
import edu.stanford.bmir.protege.web.client.issues.DiscussionThreadView;
import edu.stanford.bmir.protege.web.client.issues.DiscussionThreadViewImpl;
import edu.stanford.bmir.protege.web.client.lang.LangCodesProvider;
import edu.stanford.bmir.protege.web.client.lang.LanguageCodes;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionCheckerImpl;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveLinkManager;
import edu.stanford.bmir.protege.web.client.perspective.PerspectiveLinkManagerImpl;
import edu.stanford.bmir.protege.web.client.portlet.PortletFactory;
import edu.stanford.bmir.protege.web.client.portlet.PortletFactoryGenerated;
import edu.stanford.bmir.protege.web.client.project.*;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsView;
import edu.stanford.bmir.protege.web.client.projectsettings.ProjectSettingsViewImpl;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsView;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsViewImpl;
import edu.stanford.bmir.protege.web.client.ui.editor.EditorManagerSelector;
import edu.stanford.bmir.protege.web.client.ui.editor.EntityManagerSelectorImpl;
import edu.stanford.bmir.protege.web.client.ui.frame.ManchesterSyntaxFrameEditor;
import edu.stanford.bmir.protege.web.client.ui.frame.ManchesterSyntaxFrameEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.individuals.IndividualsListViewImpl;
import edu.stanford.bmir.protege.web.client.ui.notes.*;
import edu.stanford.bmir.protege.web.client.ui.obo.OBOTermCrossProductEditor;
import edu.stanford.bmir.protege.web.client.ui.obo.OBOTermCrossProductEditorImpl;
import edu.stanford.bmir.protege.web.client.ui.ontology.annotations.AnnotationsView;
import edu.stanford.bmir.protege.web.client.ui.ontology.annotations.AnnotationsViewImpl;
import edu.stanford.bmir.protege.web.client.watches.WatchTypeSelectorView;
import edu.stanford.bmir.protege.web.client.watches.WatchTypeSelectorViewImpl;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.LanguageCode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
@Module
public class ClientProjectModule {

    private final ProjectId projectId;

    public ClientProjectModule(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @Provides
    ProjectId provideProjectId() {
        return projectId;
    }


    @Provides
    OBOTermCrossProductEditor provideOboTermCrossProductEditor(OBOTermCrossProductEditorImpl editor) {
        return editor;
    }

    @Provides
    AnnotationsView provideAnnotationsView(AnnotationsViewImpl view) {
        return view;
    }

    @Provides
    edu.stanford.bmir.protege.web.client.ui.notes.DiscussionThreadView provideDiscussionThreadView(edu.stanford.bmir.protege.web.client.ui.notes.DiscussionThreadViewImpl view) {
        return view;
    }

    @Provides
    NoteView provideNoteView(NoteViewImpl noteView) {
        return noteView;
    }

    @Provides
    NoteContainerView provideNoteContainerView(NoteContainerViewImpl view) {
        return view;
    }

    @Provides
    NoteHeaderView provideNoteHeaderView(NoteHeaderViewImpl view) {
        return view;
    }

    @Provides
    NoteActionView provideNoteActionView(NoteActionViewImpl view) {
        return view;
    }

    @Provides
    ReplyToNoteHandler provideReplyToNoteHandler(ReplyToNoteHandlerImpl handler) {
        return handler;
    }

    @Provides
    DeleteNoteHandler provideDeleteNoteHandler(DeleteNoteHandlerImpl handler) {
        return handler;
    }

    @Provides
    ManchesterSyntaxFrameEditor provideManchesterSyntaxFrameEditor(ManchesterSyntaxFrameEditorImpl editor) {
        return editor;
    }

    @Provides
    ShowProjectDetailsHandler provideShowProjectDetailsHandler(ShowProjectDetailsHandlerImpl handler) {
        return handler;
    }

    @Provides
    UploadAndMergeHandler provideUploadAndMergeHandler(UploadAndMergeHandlerImpl handler) {
        return handler;
    }

    @Provides
    @ProjectSingleton
    ShowFreshEntitySettingsHandler provideShowFreshEntitySettingsHandler(ShowFreshEntitySettingsHandlerImpl handler) {
        return handler;
    }

    @Provides
    LoggedInUserProjectPermissionChecker provideLoggedInUserProjectPermissionChecker(LoggedInUserProjectPermissionCheckerImpl checker) {
        return checker;
    }

    @Provides
    EditorManagerSelector provideEditorManagerSelector(EntityManagerSelectorImpl selector) {
        return selector;
    }

    @Provides
    ChangeListView provideChangeListView(ChangeListViewImpl view) {
        return view;
    }

    @Provides
    SharingSettingsView provideSharingSettingsView(SharingSettingsViewImpl view) {
        return view;
    }

    @Provides
    ProjectSettingsView provideProjectSettingsView(ProjectSettingsViewImpl view) {
        return view;
    }

    @Provides
    IndividualsListView provideIndividualsListView(IndividualsListViewImpl view) {
        return view;
    }

    @Provides
    WatchTypeSelectorView provideWatchTypeSelectorView(WatchTypeSelectorViewImpl view) {
        return view;
    }

    @Provides
    FormView provideFormView(FormViewImpl view) {
        return view;
    }

    @Provides
    @LanguageCodes
    List<LanguageCode> provideLanguageCodes(LangCodesProvider provider) {
        return provider.get();
    }

    @Provides
    EntityCrudKitSettingsEditor provideEntityCrudKitSettingsEditor(EntityCrudKitSettingsEditorImpl editor) {
        return editor;
    }

    @Provides
    UserIdRangeEditor provideUserIdRangeEditor(UserIdRangeEditorImpl editor) {
        return editor;
    }


    @Provides
    PerspectiveLinkManager providePerspectiveLinkManager(PerspectiveLinkManagerImpl linkManager) {
        return linkManager;
    }


    @Provides
    @ProjectSingleton
    PortletFactory providePortletFactory(PortletFactoryGenerated portletFactoryGenerated) {
        return portletFactoryGenerated;
    }

    @Provides
    DiscussionThreadListView provideEntityDiscussionThreadListView(DiscussionThreadListViewImpl view) {
        return view;
    }

    @Provides
    DiscussionThreadView provideEntityDiscussionThreadView(DiscussionThreadViewImpl view) {
        return view;
    }

//    @Provides
//    CommentView provideCommentView() {
//        return new CommentView2Impl();
//    }
}
