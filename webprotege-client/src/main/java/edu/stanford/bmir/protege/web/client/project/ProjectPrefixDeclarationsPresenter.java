package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.project.GetProjectPrefixDeclarationsAction;
import edu.stanford.bmir.protege.web.shared.project.GetProjectPrefixDeclarationsResult;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.NO;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.YES;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_PREFIXES;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Feb 2018
 */
public class ProjectPrefixDeclarationsPresenter implements Presenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectPrefixDeclarationsView view;

    @Nonnull
    private final PermissionScreener permissionScreener;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public ProjectPrefixDeclarationsPresenter(@Nonnull ProjectId projectId,
                                              @Nonnull ProjectPrefixDeclarationsView view,
                                              @Nonnull PermissionScreener permissionScreener,
                                              @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.permissionScreener = checkNotNull(permissionScreener);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus) {
        permissionScreener.checkPermission(EDIT_PROJECT_PREFIXES.getActionId(),
                                           container,
                                           () -> {
                                               displayProjectPrefixes(container);
                                               view.setApplyChangesHandler(this::handleApplyChanges);
                                           });
    }

    private void displayProjectPrefixes(@Nonnull AcceptsOneWidget container) {
        view.clear();
        GetProjectPrefixDeclarationsAction action = new GetProjectPrefixDeclarationsAction(projectId);
        dispatchServiceManager.execute(new GetProjectPrefixDeclarationsAction(projectId),
                                       new DispatchServiceCallbackWithProgressDisplay<GetProjectPrefixDeclarationsResult>() {
                                           @Override
                                           public String getProgressDisplayTitle() {
                                               return "Retrieving prefixes";
                                           }

                                           @Override
                                           public String getProgressDisplayMessage() {
                                               return "Retrieving prefixes.  Please wait.";
                                           }

                                           @Override
                                           public void handleSuccess(GetProjectPrefixDeclarationsResult result) {
                                               view.setPrefixDeclarations(result.getPrefixDeclarations());
                                           }
                                       });
        container.setWidget(view);
    }

    private void handleApplyChanges() {
        if (alertUserAboutDuplicatePrefixNameBindings()) {
            return;
        }
        List<PrefixDeclaration> suspiciousDeclarations = getPrefixDeclarationsWithNonCommonTerminatingCharacters();
        if (!suspiciousDeclarations.isEmpty()) {
            String msg = "Prefixes usually end with either a slash (/) or a hash (#).\n" +
                    "The following prefixes do not end in either of these characters:<br><br>";
            String prefixes = suspiciousDeclarations.stream()
                                                    .map(PrefixDeclaration::getPrefix)
                                                    .collect(joining("<br>"));
            msg += prefixes;
            msg += "<br><br>Do you want to apply the changes and save these prefixes?";
            MessageBox.showConfirmBox("Suspicious prefix declarations.  Apply changes?",
                                      msg,
                                      NO, YES,
                                      this::applyChanges,
                                      NO);

        }
    }

    /**
     * Gets a list of suspicious prefix declarations.  These are declarations for prefixes that don't end with the
     * usual terminating character e.g. slash, hash or underscore (for OBO ontologies).
     *
     * @return A list of suspicious prefix declarations.
     */
    @Nonnull
    private List<PrefixDeclaration> getPrefixDeclarationsWithNonCommonTerminatingCharacters() {
        return view.getPrefixDeclarations().stream()
                   .filter(decl -> !decl.isPrefixWithCommonTerminatingCharacter())
                   .collect(toList());
    }

    /**
     * Alerts the user about duplicate bindings and displays a message to the user if there are duplicate bindings.
     *
     * @return true if there are duplicate bindings, otherwise false.
     */
    private boolean alertUserAboutDuplicatePrefixNameBindings() {
        Map<String, PrefixDeclaration> prefixDeclarations = new HashMap<>();
        for (PrefixDeclaration decl : view.getPrefixDeclarations()) {
            PrefixDeclaration existing = prefixDeclarations.put(decl.getPrefixName(), decl);
            if (existing != null) {
                MessageBox.showAlert("Duplicate prefix name bindings",
                                     "Prefix name " + existing.getPrefixName() + " is bound to more than one prefix.");
                return true;
            }
        }
        return false;
    }

    private void applyChanges() {

    }

}
