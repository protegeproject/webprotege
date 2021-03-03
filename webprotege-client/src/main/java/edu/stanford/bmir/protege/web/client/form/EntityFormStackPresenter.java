package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.entity.DeprecateEntityModal;
import edu.stanford.bmir.protege.web.client.lang.LangTagFilterPresenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.form.*;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.Map.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public class EntityFormStackPresenter {

    private Optional<OWLEntity> currentEntity = Optional.empty();

    private PristineFormDataManager pristineDataManager = new PristineFormDataManager();

    private HasBusy hasBusy = (busy) -> {
    };

    private Optional<FormLanguageFilterStash> formLanguageFilterStash = Optional.empty();

    private FormMode mode = FormMode.READ_ONLY_MODE;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormStackView view;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final FormStackPresenter formStackPresenter;

    @Nonnull
    private final LoggedInUserProjectPermissionChecker permissionChecker;

    @Nonnull
    private final LangTagFilterPresenter langTagFilterPresenter;

    @Nonnull
    private final DeprecateEntityModal deprecateEntityModal;

    @Nonnull
    private EntityDisplay entityDisplay = entityData -> {
    };

    @Inject
    public EntityFormStackPresenter(@Nonnull ProjectId projectId,
                                    @Nonnull EntityFormStackView view,
                                    @Nonnull DispatchServiceManager dispatch,
                                    @Nonnull FormStackPresenter formStackPresenter,
                                    @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                    @Nonnull LangTagFilterPresenter langTagFilterPresenter,
                                    @Nonnull DeprecateEntityModal deprecateEntityModal) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.formStackPresenter = checkNotNull(formStackPresenter);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.langTagFilterPresenter = checkNotNull(langTagFilterPresenter);
        this.deprecateEntityModal = deprecateEntityModal;
    }

    public void setEntityDisplay(@Nonnull EntityDisplay entityDisplay) {
        this.entityDisplay = checkNotNull(entityDisplay);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        formStackPresenter.start(view.getFormStackContainer());
        formStackPresenter.setFormRegionPageChangedHandler(this::handlePageChange);
        formStackPresenter.setFormRegionOrderingChangedHandler(this::handleGridOrderByChanged);
        formStackPresenter.setFormRegionFilterChangedHandler(this::handleFormRegionFilterChanged);
        formStackPresenter.setSelectedFormChangedHandler(this::handleSelectedFormChanged);
        langTagFilterPresenter.start(view.getLangTagFilterContainer());
        langTagFilterPresenter.setLangTagFilterChangedHandler(this::handleLangTagFilterChanged);
        view.setEnterEditModeHandler(this::handleEnterEditMode);
        view.setApplyEditsHandler(this::handleApplyEdits);
        view.setDeprecateEntityHandler(this::handleDeprecateEntity);
        view.setCancelEditsHandler(this::handleCancelEdits);

        permissionChecker.hasPermission(BuiltInAction.EDIT_ONTOLOGY, view::setEditButtonVisible);
        setMode(FormMode.READ_ONLY_MODE);
    }

    private void handleSelectedFormChanged() {
        ImmutableList<FormId> selectedForms = formStackPresenter.getSelectedForms();
        ImmutableList<FormId> selectedFormsToUpdate = selectedForms.stream()
                                           .filter(formId -> !pristineDataManager.containsPristineFormData(formId))
                                           .collect(toImmutableList());
        if (selectedFormsToUpdate.isEmpty()) {
            return;
        }
        GWT.log("[EntityFormStackPresenter] Updating forms: " + selectedForms);
        updateFormsForCurrentEntity(selectedFormsToUpdate);
    }

    private void handleFormRegionFilterChanged(FormRegionFilterChangedEvent event) {
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    private void handleGridOrderByChanged() {
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    public void setHasBusy(HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    private void handleLangTagFilterChanged() {
        stashLanguagesFilter();
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    private void handlePageChange(FormRegionPageChangedEvent event) {
        updateFormsForCurrentEntity(ImmutableList.of(event.getFormId()));
    }

    public void setEntity(@Nonnull OWLEntity entity) {
        if (mode.equals(FormMode.EDIT_MODE)) {
            handleOutstandingEditsAndSwitchToEntity(entity);
        }
        else {
            switchToEntity(entity);
        }
    }

    private void handleOutstandingEditsAndSwitchToEntity(@Nonnull OWLEntity entity) {
        view.displayApplyOutstandingEditsConfirmation(() -> {
            currentEntity.ifPresent(this::applyEdits);
            switchToEntity(entity);
        }, () -> {
            handleCancelEdits();
            switchToEntity(entity);
        });
    }

    private void switchToEntity(@Nonnull OWLEntity entity) {
        this.currentEntity = Optional.of(entity);
        pristineDataManager.resetCurrentEntity(entity);
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    public void clear() {
        this.currentEntity = Optional.empty();
        pristineDataManager.clearCurrentEntity();
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    public void expandAllFields() {
        formStackPresenter.expandAllFields();
    }

    public void collapseAllFields() {
        formStackPresenter.collapseAllFields();
    }

    private void updateFormsForCurrentEntity(ImmutableList<FormId> formFilter) {
        currentEntity.ifPresent(entity -> {
            ImmutableList<FormPageRequest> pageRequests = formStackPresenter.getPageRequests();
            ImmutableSet<FormRegionOrdering> orderings = formStackPresenter.getGridControlOrderings();
            ImmutableSet<FormRegionFilter> filters = formStackPresenter.getRegionFilters();
            LangTagFilter langTagFilter = langTagFilterPresenter.getFilter();
            dispatch.execute(new GetEntityFormsAction(projectId,
                                                      entity,
                                                      formFilter,
                                                      pageRequests,
                                                      langTagFilter,
                                                      orderings,
                                                      filters), hasBusy, this::handleGetEntityFormsResult);
        });
        if (!currentEntity.isPresent()) {
            entityDisplay.setDisplayedEntity(Optional.empty());
            formStackPresenter.clearForms();
        }
    }

    private void handleGetEntityFormsResult(GetEntityFormsResult result) {
        entityDisplay.setDisplayedEntity(Optional.of(result.getEntityData()));
        view.setDeprecateButtonVisible(!result.getEntityData().isDeprecated());
        ImmutableList<FormDataDto> formData = result.getFormData();
        for (FormDataDto formDataDto : result.getFormData()) {
            pristineDataManager.updatePristineFormData(formDataDto.toFormData());
        }
        boolean replaceAllForms = result.getFilteredFormIds().isEmpty() || result.getFormData().isEmpty();
        // If we have a subset of the forms then just replace the ones that we have
        if (replaceAllForms) {
            formStackPresenter.setForms(formData, FormStackPresenter.FormUpdate.REPLACE);
        }
        else {
            formStackPresenter.setForms(formData, FormStackPresenter.FormUpdate.PATCH);
        }
    }

    private void handleEnterEditMode() {
        permissionChecker.hasPermission(BuiltInAction.EDIT_ONTOLOGY, canEdit -> setMode(FormMode.EDIT_MODE));
    }

    private void handleApplyEdits() {
        setMode(FormMode.READ_ONLY_MODE);
        currentEntity.ifPresent(this::applyEdits);
    }

    private void applyEdits(@Nonnull OWLEntity entity) {
        // TODO: Offer a commit message
        setMode(FormMode.READ_ONLY_MODE);
        permissionChecker.hasPermission(BuiltInAction.EDIT_ONTOLOGY, canEdit -> {
            if (canEdit) {
                commitEdits(entity);
            }
        });
    }

    private void handleDeprecateEntity() {
        currentEntity.ifPresent(entity -> deprecateEntityModal.showModal(entity,
                                                                         () -> view.setDeprecateButtonVisible(false),
                                                                         () -> {
                                                                         }));
    }

    private void handleCancelEdits() {
        setMode(FormMode.READ_ONLY_MODE);
        dropEdits();
    }

    private void setMode(@Nonnull FormMode mode) {
        this.mode = checkNotNull(mode);
        if (mode == FormMode.EDIT_MODE) {
            view.setEditButtonVisible(false);
            view.setApplyEditsButtonVisible(true);
            view.setCancelEditsButtonVisible(true);
        }
        else {
            view.setEditButtonVisible(true);
            view.setApplyEditsButtonVisible(false);
            view.setCancelEditsButtonVisible(false);
        }
        formStackPresenter.setEnabled(mode == FormMode.EDIT_MODE);
    }

    private void commitEdits(@Nonnull OWLEntity entity) {
        ImmutableMap<FormId, FormData> editedFormData = formStackPresenter.getForms()
                                                                          .entrySet()
                .stream()
                .filter(e -> pristineDataManager.containsPristineFormData(e.getValue().getFormId()))
                .collect(toImmutableMap(Entry::getKey, Entry::getValue));
        if(editedFormData.isEmpty()) {
            return;
        }
        ImmutableSet<FormId> editedFormIds = editedFormData.keySet();
        GWT.log("[EntityFormStackPresenter] Committing edits for " + editedFormIds);
        ImmutableMap<FormId, FormData> pristineFormData = pristineDataManager.getPristineFormData(editedFormIds);
        dispatch.execute(new SetEntityFormsDataAction(projectId, entity, pristineFormData, editedFormData),
                         // Refresh the pristine data to what was committed
                         result -> updateFormsForCurrentEntity(ImmutableList.copyOf(editedFormIds)));
    }

    private void dropEdits() {
        currentEntity.ifPresent(e -> pristineDataManager.resetCurrentEntity(e));
        // Back to pristine data for the selected forms
        updateFormsForCurrentEntity(formStackPresenter.getSelectedForms());
    }

    public void setSelectedFormIdStash(@Nonnull SelectedFormIdStash formIdStash) {
        formStackPresenter.setSelectedFormIdStash(formIdStash);
    }

    private void stashLanguagesFilter() {
        formLanguageFilterStash.ifPresent(stash -> {
            ImmutableSet<LangTag> filteringTags = langTagFilterPresenter.getFilter().getFilteringTags();
            stash.stashLanguage(filteringTags);
        });
    }

    public void setLanguageFilterStash(FormLanguageFilterStash formLanguageFilterStash) {
        this.formLanguageFilterStash = Optional.of(checkNotNull(formLanguageFilterStash));
        ImmutableSet<LangTag> stashedLangTags = ImmutableSet.copyOf(formLanguageFilterStash.getStashedLangTags());
        LangTagFilter langTagFilter = LangTagFilter.get(stashedLangTags);
        langTagFilterPresenter.setFilter(langTagFilter);
    }
}
