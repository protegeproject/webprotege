package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.progress.HasBusy;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-21
 */
public class FormsManagerObjectPresenter implements ObjectPresenter<FormDescriptor> {

    private final FormsManagerObjectView view;

    private Optional<FormDescriptor> value = Optional.empty();

    private final LanguageMapCurrentLocaleMapper localeMapper;

    private Consumer<String> headerLabelChangedHandler = (label) -> {};

    @Nonnull
    private final PlaceController placeController;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final FormsMessages formsMessages;

    @Nonnull
    private final FormsManagerService formsManagerService;

    @Nonnull
    private HasBusy busyIndicator = (busy) -> {};

    @Inject
    public FormsManagerObjectPresenter(@Nonnull FormsManagerObjectView view,
                                       @Nonnull LanguageMapCurrentLocaleMapper localeMapper,
                                       @Nonnull PlaceController placeController,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull FormsMessages formsMessages,
                                       @Nonnull FormsManagerService formsManagerService) {
        this.view = checkNotNull(view);
        this.localeMapper = checkNotNull(localeMapper);
        this.placeController = checkNotNull(placeController);
        this.projectId = checkNotNull(projectId);
        this.formsMessages = checkNotNull(formsMessages);
        this.formsManagerService = formsManagerService;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        view.setShowFormDetailsHandler(this::handleShowFormDetails);
        view.setLanguageMapChangedHandler(this::handleLanguageMapChanged);
    }

    private void handleShowFormDetails() {
        value.ifPresent(descriptor -> {
            placeController.goTo(EditFormPlace.get(projectId,
                                                   descriptor.getFormId(),
                                                   placeController.getWhere()));
        });
    }

    @Override
    public void setValue(@Nonnull FormDescriptor value) {
        this.value = Optional.of(value);
        view.setLanguageMap(value.getLabel());
    }

    @Nonnull
    @Override
    public Optional<FormDescriptor> getValue() {
        return value;
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return localeMapper.getValueForCurrentLocale(view.getLanguageMap());
    }

    private void handleLanguageMapChanged() {
        headerLabelChangedHandler.accept(getHeaderLabel());
        value.ifPresent(this::updateCurrentFormDescriptorWithLanguageMap);
    }

    private void updateCurrentFormDescriptorWithLanguageMap(FormDescriptor currentFormDescriptor) {
        LanguageMap newLabel = view.getLanguageMap();
        FormDescriptor updatedFormDescriptor = currentFormDescriptor.withLabel(newLabel);
        formsManagerService.updateForm(updatedFormDescriptor,
                                       busyIndicator,
                                       () -> {});
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        this.headerLabelChangedHandler = checkNotNull(headerLabelHandler);
    }
}
