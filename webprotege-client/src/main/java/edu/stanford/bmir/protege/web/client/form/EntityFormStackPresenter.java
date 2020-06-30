package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.LangTagFilterPresenter;
import edu.stanford.bmir.protege.web.client.permissions.LoggedInUserProjectPermissionChecker;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
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

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-23
 */
public class EntityFormStackPresenter {

    private Optional<OWLEntity> currentEntity = Optional.empty();

    private final Map<FormId, FormData> pristineFormData = new HashMap<>();

    private HasBusy hasBusy = (busy) -> {};

    private Optional<FormLanguageFilterStash> formLanguageFilterStash = Optional.empty();

    enum Mode {
        EDIT_MODE,
        READ_ONLY_MODE
    }

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

    @Inject
    public EntityFormStackPresenter(@Nonnull ProjectId projectId,
                                    @Nonnull EntityFormStackView view,
                                    @Nonnull DispatchServiceManager dispatch,
                                    @Nonnull FormStackPresenter formStackPresenter,
                                    @Nonnull LoggedInUserProjectPermissionChecker permissionChecker,
                                    @Nonnull LangTagFilterPresenter langTagFilterPresenter) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatch = checkNotNull(dispatch);
        this.formStackPresenter = checkNotNull(formStackPresenter);
        this.permissionChecker = checkNotNull(permissionChecker);
        this.langTagFilterPresenter = checkNotNull(langTagFilterPresenter);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        formStackPresenter.start(view.getFormStackContainer());
        formStackPresenter.setFormRegionPageChangedHandler(this::handlePageChange);
        formStackPresenter.setFormRegionOrderingChangedHandler(this::handleGridOrderByChanged);
        formStackPresenter.setFormRegionFilterChangedHandler(this::handleFormRegionFilterChanged);
        langTagFilterPresenter.start(view.getLangTagFilterContainer());
        langTagFilterPresenter.setLangTagFilterChangedHandler(this::handleLangTagFilterChanged);
        view.setEnterEditModeHandler(this::handleEnterEditMode);
        view.setApplyEditsHandler(this::handleApplyEdits);
        view.setCancelEditsHandler(this::handleCancelEdits);

        permissionChecker.hasPermission(BuiltInAction.EDIT_ONTOLOGY,
                                        view::setEditButtonVisible);
        setMode(Mode.READ_ONLY_MODE);
    }

    private void handleFormRegionFilterChanged(FormRegionFilterChangedEvent event) {
        updateFormsForCurrentEntity(ImmutableList.of());
    }

    private void handleGridOrderByChanged() {
        updateFormsForCurrentEntity(ImmutableList.of());
    }

    public void setHasBusy(HasBusy hasBusy) {
        this.hasBusy = checkNotNull(hasBusy);
    }

    private void handleLangTagFilterChanged() {
        stashLanguagesFilter();
        updateFormsForCurrentEntity(ImmutableList.of());
    }

    private void handlePageChange(FormRegionPageChangedEvent event) {
        updateFormsForCurrentEntity(ImmutableList.of(event.getFormId()));
    }

    public void setEntity(@Nonnull OWLEntity entity) {
        this.currentEntity = Optional.of(entity);
        updateFormsForCurrentEntity(ImmutableList.of());
    }

    public void clear() {
        this.currentEntity = Optional.empty();
        updateFormsForCurrentEntity(ImmutableList.of());
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
            dispatch.execute(new GetEntityFormsAction(projectId, entity, formFilter, pageRequests, langTagFilter,
                                                      orderings, filters),
                             hasBusy,
                             this::handleGetEntityFormsResult);
        });
        if(!currentEntity.isPresent()) {
            formStackPresenter.clearForms();
        }
    }

    private void handleGetEntityFormsResult(GetEntityFormsResult result) {
        ImmutableList<FormDataDto> formData = result.getFormData();
        // If we have a subset of the forms then just replace the ones that we have
        boolean replaceAllForms = result.getFilteredFormIds().isEmpty();
        if (replaceAllForms) {
            pristineFormData.clear();
        }
        for(FormDataDto formDataDto : result.getFormData()) {
            pristineFormData.put(formDataDto.getFormId(), formDataDto.toFormData());
        }
        if (replaceAllForms) {
            formStackPresenter.setForms(formData, FormStackPresenter.FormUpdate.REPLACE);
        }
        else {
            formStackPresenter.setForms(formData, FormStackPresenter.FormUpdate.PATCH);
        }
    }

    private void handleEnterEditMode() {
        permissionChecker.hasPermission(BuiltInAction.EDIT_ONTOLOGY,
                                        canEdit -> setMode(Mode.EDIT_MODE));
    }

    private void handleApplyEdits() {
        setMode(Mode.READ_ONLY_MODE);
        // TODO: Offer a commit message
        permissionChecker.hasPermission(BuiltInAction.EDIT_ONTOLOGY,
                                        canEdit -> {
                                            if(canEdit) {
                                                commitEdits();
                                            }
                                        });
    }

    private void handleCancelEdits() {
        setMode(Mode.READ_ONLY_MODE);
        dropEdits();
    }

    private void setMode(@Nonnull Mode mode) {
        if(mode == Mode.EDIT_MODE) {
            view.setEditButtonVisible(false);
            view.setApplyEditsButtonVisible(true);
            view.setCancelEditsButtonVisible(true);
        }
        else {
            view.setEditButtonVisible(true);
            view.setApplyEditsButtonVisible(false);
            view.setCancelEditsButtonVisible(false);
        }
        formStackPresenter.setEnabled(mode == Mode.EDIT_MODE);
    }




    private void commitEdits() {
        currentEntity.ifPresent(entity -> {
            ImmutableMap<FormId, FormData> editedFormData = formStackPresenter.getForms();
            ImmutableMap<FormId, FormData> pristineFormData = ImmutableMap.copyOf(this.pristineFormData);
            dispatch.execute(new SetEntityFormsDataAction(projectId,
                                                          entity,
                                                          pristineFormData,
                                                          editedFormData),
                             result -> {});
        });
    }

    private void dropEdits() {
        updateFormsForCurrentEntity(ImmutableList.of());
    }

    public void setSelectedFormIdStash(@Nonnull SelectedFormIdStash formIdStash) {
        formStackPresenter.setSelectedFormIdStash(formIdStash);
    }

    private void stashLanguagesFilter() {
        formLanguageFilterStash.ifPresent(stash -> {
            ImmutableSet<LangTag> filteringTags = langTagFilterPresenter.getFilter()
                                                                        .getFilteringTags();
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
