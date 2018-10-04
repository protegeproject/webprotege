package edu.stanford.bmir.protege.web.client.project;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.app.PermissionScreener;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchErrorMessageDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallbackWithProgressDisplay;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.ProgressDisplay;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.place.ProjectPrefixDeclarationsPlace;
import edu.stanford.bmir.protege.web.shared.project.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.NO;
import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.YES;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_PROJECT_PREFIXES;
import static java.util.Comparator.comparing;
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

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final Messages messages;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final DispatchErrorMessageDisplay errorDisplay;

    @Nonnull
    private final ProgressDisplay progressDisplay;

    @Inject
    public ProjectPrefixDeclarationsPresenter(@Nonnull ProjectId projectId,
                                              @Nonnull ProjectPrefixDeclarationsView view,
                                              @Nonnull PermissionScreener permissionScreener,
                                              @Nonnull DispatchServiceManager dispatchServiceManager,
                                              @Nonnull PlaceController placeController,
                                              @Nonnull SettingsPresenter settingsPresenter,
                                              @Nonnull Messages messages, MessageBox messageBox, @Nonnull DispatchErrorMessageDisplay errorDisplay, @Nonnull ProgressDisplay progressDisplay) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.permissionScreener = checkNotNull(permissionScreener);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.placeController = checkNotNull(placeController);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.messages = checkNotNull(messages);
        this.messageBox = checkNotNull(messageBox);
        this.errorDisplay = checkNotNull(errorDisplay);
        this.progressDisplay = checkNotNull(progressDisplay);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull EventBus eventBus) {
        permissionScreener.checkPermission(EDIT_PROJECT_PREFIXES.getActionId(),
                                           container,
                                           () -> displayProjectPrefixes(container));
    }

    private void displayProjectPrefixes(@Nonnull AcceptsOneWidget container) {
        view.clear();
        settingsPresenter.start(container);
        settingsPresenter.setSettingsTitle(messages.prefixes_project_title());
        settingsPresenter.addSection(messages.prefixes_title()).setWidget(view);
        settingsPresenter.setApplySettingsHandler(this::handleApplyChanges);
        settingsPresenter.setCancelSettingsHandler(this::cancelChanges);
        settingsPresenter.setNextPlace(getNextPlace());
        dispatchServiceManager.execute(new GetProjectPrefixDeclarationsAction(projectId),
                                       new DispatchServiceCallbackWithProgressDisplay<GetProjectPrefixDeclarationsResult>(errorDisplay, progressDisplay) {
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
                                               List<PrefixDeclaration> prefixDeclarations = result.getPrefixDeclarations();
                                               prefixDeclarations.sort(comparing(PrefixDeclaration::getPrefixName)
                                                                               .thenComparing(PrefixDeclaration::getPrefix));
                                               view.setPrefixDeclarations(prefixDeclarations);
                                           }
                                       });
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
            messageBox.showConfirmBox("Suspicious prefix declarations.  Apply changes?",
                                      msg,
                                      NO, YES,
                                      this::applyChanges,
                                      NO);

        }
        else {
            applyChanges();
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
                messageBox.showAlert("Duplicate prefix name bindings",
                                     "Prefix name " + existing.getPrefixName() + " is bound to more than one prefix.");
                return true;
            }
        }
        return false;
    }

    private void applyChanges() {
        dispatchServiceManager.execute(new SetProjectPrefixDeclarationsAction(projectId, view.getPrefixDeclarations()),
                                       result -> handleChangesApplied());
    }

    private void cancelChanges() {
    }

    /**
     * Gets the next place
     * @return The next place to go to.  Possibly empty inidicating the next place is not specified.
     */
    private Optional<Place> getNextPlace() {
        Place place = placeController.getWhere();
        if(!(place instanceof ProjectPrefixDeclarationsPlace)) {
            return Optional.empty();
        }
        ProjectPrefixDeclarationsPlace prefixesPlace = (ProjectPrefixDeclarationsPlace) place;
        return prefixesPlace.getNextPlace();
    }

    private void handleChangesApplied() {
        Optional<Place> nextPlace = getNextPlace();
        nextPlace.ifPresent(placeController::goTo);
        if(!nextPlace.isPresent()) {
            messageBox.showMessage("Changes Applied", "Your changes have been applied.");
        }
    }

}
