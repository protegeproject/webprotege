package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.HasEventHandlerManagement;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.PermissionsChangedHandler;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class ManchesterSyntaxFrameEditorPresenter implements HasSubject<OWLEntity>, HasFreshEntities {

    private ManchesterSyntaxFrameEditor editor;

    private DispatchServiceManager dispatchService;

    private ProjectId projectId;

    private Optional<OWLEntity> currentSubject = Optional.absent();

    private Optional<String> pristineValue = Optional.absent();

    private Timer errorCheckTimer = new Timer() {
        @Override
        public void run() {
            checkSyntax();
        }
    };

    private Set<OWLEntityData> freshEntities = new HashSet<OWLEntityData>();

    private HasUserId hasUserId;

    private PermissionChecker permissionChecker;

    private CreateAsEntityTypeHandler createAsEntityTypeHandler = new CreateAsEntityTypeHandler() {
        @Override
        public void handleCreateHasEntity(String name, EntityType<?> entityType) {
            OWLEntity entity = DataFactory.getFreshOWLEntity(entityType, name);
            freshEntities.add(DataFactory.getOWLEntityData(entity, name));
            checkSyntax();
        }
    };


    private ApplyChangesActionHandler applyChangesActionHandler = new ApplyChangesActionHandler() {
        @Override
        public void handleApplyChanges() {
            applyChangesWithoutCommitMessage();
        }

        @Override
        public void handleApplyChangesWithCommitMessage() {
            applyChangesWithCommitMessage();
        }
    };




    public ManchesterSyntaxFrameEditorPresenter(ManchesterSyntaxFrameEditor editor, ProjectId projectId, HasUserId hasUserId, PermissionChecker permissionChecker, DispatchServiceManager dispatchService) {
        this.editor = editor;
        this.hasUserId = hasUserId;
        this.permissionChecker = permissionChecker;
        this.dispatchService = dispatchService;
        this.projectId = projectId;
    }

    public void attach(HasEventHandlerManagement management) {
        editor.addValueChangeHandler(new ValueChangeHandler<Optional<String>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<String>> event) {
                errorCheckTimer.cancel();
                errorCheckTimer.schedule(500);
            }
        });
        editor.setCreateAsEntityTypeHandler(createAsEntityTypeHandler);
        editor.setAutoCompletionHandler(new ManchesterSyntaxFrameAutoCompletionHandler(
                DispatchServiceManager.get(),
                projectId,
                this,
                this
        ));
        editor.setApplyChangesHandler(applyChangesActionHandler);
        management.addProjectEventHandler(UserLoggedInEvent.TYPE, new UserLoggedInHandler() {
            @Override
            public void handleUserLoggedIn(UserLoggedInEvent event) {
                updateState();
            }
        });
        management.addProjectEventHandler(UserLoggedOutEvent.TYPE, new UserLoggedOutHandler() {
            @Override
            public void handleUserLoggedOut(UserLoggedOutEvent event) {
                updateState();
            }
        });
        management.addProjectEventHandler(PermissionsChangedEvent.TYPE, new PermissionsChangedHandler() {
            @Override
            public void handlePersmissionsChanged(PermissionsChangedEvent event) {
                updateState();
            }
        });
        updateState();
    }

    private void updateState() {
        UserId userId = hasUserId.getUserId();
        boolean writePermission = permissionChecker.hasWritePermissionForProject(userId, projectId);
        setEnabled(writePermission);
    }


    private void setEnabled(boolean b) {
        editor.setEnabled(b);
    }

    private void checkSyntax() {
        errorCheckTimer.cancel();
        if(!currentSubject.isPresent()) {
            editor.clearError();
        }
        else {
            String newRendering = editor.getValue().get();
            dispatchService.execute(new CheckManchesterSyntaxFrameAction(projectId, currentSubject.get(), pristineValue.get(), newRendering, freshEntities),
                    new AsyncCallback<CheckManchesterSyntaxFrameResult>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            editor.setApplyChangesViewVisible(false);
                        }

                        @Override
                        public void onSuccess(CheckManchesterSyntaxFrameResult result) {
                            if(result.getResult() != ManchesterSyntaxFrameParseResult.ERROR) {
                                editor.clearError();
                            }
                            else {
                                editor.setError(result.getError().get());
                            }
                            editor.setApplyChangesViewVisible(result.getResult() == ManchesterSyntaxFrameParseResult.CHANGED);
                        }
                    });
        }

    }

    public void clearSubject() {
        editor.clearValue();
        currentSubject = Optional.absent();
        freshEntities.clear();
    }

    public void setSubject(final OWLEntity subject) {
        applyChangesWithoutCommitMessage();
        freshEntities.clear();
        dispatchService.execute(new GetManchesterSyntaxFrameAction(projectId, subject), new AsyncCallback<GetManchesterSyntaxFrameResult>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO:
            }

            @Override
            public void onSuccess(GetManchesterSyntaxFrameResult result) {
                editor.setValue(result.getManchesterSyntax());
                pristineValue = Optional.of(result.getManchesterSyntax());
                currentSubject = Optional.of(subject);
            }
        });
    }

    private void applyChangesWithoutCommitMessage() {
        applyChanges(Optional.<String>absent());
    }

    private void applyChangesWithCommitMessage(String input) {
        applyChanges(Optional.of(input));
    }

    private void applyChangesWithCommitMessage() {
        InputBox.showDialog("Enter commit message", new InputBoxHandler() {
            @Override
            public void handleAcceptInput(String input) {
                applyChangesWithCommitMessage(input);
            }
        });
    }

    private void applyChanges(Optional<String> commitMessage) {
        editor.setApplyChangesViewVisible(false);
        final Optional<String> editorText = editor.getValue();
        if(!editorText.equals(pristineValue) && pristineValue.isPresent() && editorText.isPresent() && currentSubject.isPresent()) {
            String text = editorText.get();
            dispatchService.execute(new SetManchesterSyntaxFrameAction(projectId, currentSubject.get(), pristineValue.get(), text, freshEntities, commitMessage), new AsyncCallback<SetManchesterSyntaxFrameResult>() {
                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.showAlert("A problem has occurred and the changes could not be applied.  Please try again.");
                }

                @Override
                public void onSuccess(SetManchesterSyntaxFrameResult result) {

                }
            });
        }
    }

    @Override
    public Set<OWLEntityData> getFreshEntities() {
        return Sets.newHashSet(freshEntities);
    }

    @Override
    public OWLEntity getSubject() {
        return currentSubject.orNull();
    }
}
