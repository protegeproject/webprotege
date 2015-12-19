package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInHandler;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutHandler;
import edu.stanford.bmir.protege.web.client.permissions.PermissionChecker;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.shared.frame.SetManchesterSyntaxFrameException;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.*;
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

    private final DispatchServiceManager dispatchServiceManager;

    private ManchesterSyntaxFrameEditor editor;

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


    public ManchesterSyntaxFrameEditorPresenter(ManchesterSyntaxFrameEditor editor, ProjectId projectId, HasUserId hasUserId, PermissionChecker permissionChecker, DispatchServiceManager dispatchServiceManager) {
        this.editor = editor;
        this.hasUserId = hasUserId;
        this.permissionChecker = permissionChecker;
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
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
        editor.setAutoCompletionHandler(new ManchesterSyntaxFrameAutoCompletionHandler(dispatchServiceManager,
                                                                                       projectId, this, this));
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
        management.addProjectEventHandler(ProjectChangedEvent.TYPE, new ProjectChangedHandler() {
            @Override
            public void handleProjectChanged(ProjectChangedEvent event) {
                refreshIfPristine();
            }
        });
        updateState();
    }

    private boolean isPristine() {
        return editor.getValue().equals(pristineValue);
    }

    public void clearSubject() {
        editor.clearValue();
        currentSubject = Optional.absent();
        freshEntities.clear();
    }

    public void setSubject(final OWLEntity subject) {
        applyChangesWithoutCommitMessage();
        replaceTextWithFrameRendering(subject);
    }

    public void refresh() {
        if (currentSubject.isPresent()) {
            replaceTextWithFrameRendering(currentSubject.get());
        }
    }

    private void refreshIfPristine() {
        if(isPristine()) {
            refresh();
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
            dispatchServiceManager.execute(new CheckManchesterSyntaxFrameAction(projectId, currentSubject.get(), pristineValue.get(), newRendering, freshEntities),
                    new DispatchServiceCallback<CheckManchesterSyntaxFrameResult>() {

                        @Override
                        public void handleErrorFinally(Throwable throwable) {
                            editor.setApplyChangesViewVisible(false);
                        }

                        @Override
                        public void handleSuccess(CheckManchesterSyntaxFrameResult result) {
                            if(result.getResult() != ManchesterSyntaxFrameParseResult.ERROR) {
                                editor.clearError();
                            }
                            else {
                                editor.setError(result.getError().get());
                            }
                            if(result.getResult() == ManchesterSyntaxFrameParseResult.CHANGED) {
                                GWT.log("Structural changes encountered in edited expression");
                                editor.setApplyChangesViewVisible(result.getResult() == ManchesterSyntaxFrameParseResult.CHANGED);
                            }
                            else {
                                GWT.log("No structural changes in edited expression");
                            }

                        }
                    });
        }

    }

    private void applyChangesWithoutCommitMessage() {
        applyChanges(Optional.<String>absent(), false);
    }

    private void applyChangesWithCommitMessage(String input) {
        applyChanges(Optional.of(input), false);
    }

    private void applyChangesWithCommitMessage() {
        InputBox.showDialog("Enter commit message", new InputBoxHandler() {
            @Override
            public void handleAcceptInput(String input) {
                applyChangesWithCommitMessage(input);
            }
        });
    }

    private void applyChanges(Optional<String> commitMessage, final boolean reformatText) {
        editor.setApplyChangesViewVisible(false);
        final Optional<String> editorText = editor.getValue();
        if(!isPristine() && pristineValue.isPresent() && editorText.isPresent() && currentSubject.isPresent()) {
            String text = editorText.get();
            dispatchServiceManager.execute(new SetManchesterSyntaxFrameAction(projectId, currentSubject.get(), pristineValue.get(), text, freshEntities, commitMessage), new DispatchServiceCallback<SetManchesterSyntaxFrameResult>() {
                @Override
                public void handleSuccess(SetManchesterSyntaxFrameResult result) {
                    if(reformatText) {
                        editor.setValue(result.getFrameText());
                    }
                }

                @Override
                protected String getErrorMessageTitle() {
                    return "Could not apply changes to due to invalid syntax";
                }

                @Override
                protected String getErrorMessage(Throwable throwable) {
                    if(throwable instanceof SetManchesterSyntaxFrameException) {
                        return throwable.getMessage();
                    }
                    else {
                        return super.getErrorMessage(throwable);
                    }
                }

                @Override
                public void handleErrorFinally(Throwable throwable) {
                    editor.clearError();
                }
            });
        }
    }


    private void replaceTextWithFrameRendering(final OWLEntity subject) {
        editor.setApplyChangesViewVisible(false);
        freshEntities.clear();
        dispatchServiceManager.execute(new GetManchesterSyntaxFrameAction(projectId, subject), new DispatchServiceCallback<GetManchesterSyntaxFrameResult>() {
            @Override
            public void handleSuccess(GetManchesterSyntaxFrameResult result) {
                editor.setValue(result.getManchesterSyntax());
                pristineValue = Optional.of(result.getManchesterSyntax());
                currentSubject = Optional.of(subject);
            }
        });
    }
}
