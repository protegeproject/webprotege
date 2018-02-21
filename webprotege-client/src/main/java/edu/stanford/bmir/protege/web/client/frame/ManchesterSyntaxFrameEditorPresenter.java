package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.events.UserLoggedInEvent;
import edu.stanford.bmir.protege.web.client.events.UserLoggedOutEvent;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.user.LoggedInUserProvider;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.ProjectChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.frame.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.bmir.protege.web.shared.permissions.PermissionsChangedEvent.ON_PERMISSIONS_CHANGED;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class ManchesterSyntaxFrameEditorPresenter implements HasSubject<OWLEntity>, HasFreshEntities {

    private final DispatchServiceManager dispatchServiceManager;

    private final LoggedInUserProvider loggedInUserProvider;

    private final ManchesterSyntaxFrameEditor editor;

    private ProjectId projectId;

    private Optional<OWLEntity> currentSubject = Optional.empty();

    private Optional<String> pristineValue = Optional.empty();

    private Timer errorCheckTimer = new Timer() {
        @Override
        public void run() {
            checkSyntax();
        }
    };

    private Set<OWLEntityData> freshEntities = new HashSet<OWLEntityData>();

    private LoggedInUserProjectPermissionChecker permissionChecker;

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


    @Inject
    public ManchesterSyntaxFrameEditorPresenter(ManchesterSyntaxFrameEditor editor, ProjectId projectId, LoggedInUserProjectPermissionChecker permissionChecker, DispatchServiceManager dispatchServiceManager, LoggedInUserProvider loggedInUserProvider) {
        this.editor = editor;
        this.permissionChecker = permissionChecker;
        this.projectId = projectId;
        this.dispatchServiceManager = dispatchServiceManager;
        this.loggedInUserProvider = loggedInUserProvider;
    }

    public ManchesterSyntaxFrameEditor getView() {
        return editor;
    }

    public void start(WebProtegeEventBus eventBus) {
        editor.addValueChangeHandler(event -> {
            errorCheckTimer.cancel();
            errorCheckTimer.schedule(500);
        });
        editor.setCreateAsEntityTypeHandler(createAsEntityTypeHandler);
        editor.setAutoCompletionHandler(new ManchesterSyntaxFrameAutoCompletionHandler(dispatchServiceManager,
                                                                                       projectId, this, this));
        editor.setApplyChangesHandler(applyChangesActionHandler);
        eventBus.addProjectEventHandler(projectId, UserLoggedInEvent.ON_USER_LOGGED_IN, event -> updateState());
        eventBus.addProjectEventHandler(projectId, UserLoggedOutEvent.ON_USER_LOGGED_OUT, event -> updateState());
        eventBus.addProjectEventHandler(projectId, ON_PERMISSIONS_CHANGED, event -> updateState());
        eventBus.addProjectEventHandler(projectId, ProjectChangedEvent.TYPE, event -> refreshIfPristine());
        updateState();
    }

    private boolean isPristine() {
        return editor.getValue().equals(pristineValue);
    }

    public void clearSubject() {
        editor.clearValue();
        currentSubject = Optional.empty();
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
        return currentSubject.orElse(null);
    }


    private void updateState() {
        setEnabled(false);
        permissionChecker.hasPermission(EDIT_ONTOLOGY,
                                        canEdit -> setEnabled(canEdit));
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
        applyChanges(Optional.empty(), false);
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
