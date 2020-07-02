package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.FormControlStackRepeatingView.FormControlContainer;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest;
import edu.stanford.bmir.protege.web.shared.form.RegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

public class FormControlStackRepeatingPresenter implements FormControlStackPresenter, ValueChangeHandler<Optional<FormControlData>> {

    private final HandlerManager handlerManager = new HandlerManager(this);

    @Nonnull
    private final FormControlStackRepeatingView view;

    @Nonnull
    private final PaginatorPresenter paginatorPresenter;

    @Nonnull
    private final FormControlDataEditorFactory formControlFactory;

    private boolean enabled = true;

    @Nonnull
    private final List<FormControl> formControls = new ArrayList<>();

    private FormRegionPosition position;

    @Nonnull
    private RegionPageChangedHandler regionPageChangedHandler = () -> {};

    @Nonnull
    private FormRegionFilterChangedHandler formRegionFilterChangedHandler = event -> {};

    @AutoFactory
    @Inject
    public FormControlStackRepeatingPresenter(@Provided @Nonnull FormControlStackRepeatingView view,
                                              @Provided @Nonnull PaginatorPresenter paginatorPresenter,
                                              @Nonnull FormRegionPosition position,
                                              @Nonnull FormControlDataEditorFactory formControlFactory) {
        this.view = checkNotNull(view);
        this.position = checkNotNull(position);
        this.paginatorPresenter = checkNotNull(paginatorPresenter);
        this.formControlFactory = checkNotNull(formControlFactory);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        PaginatorView paginatorView = paginatorPresenter.getView();
        view.getPaginatorContainer().setWidget(paginatorView);
        view.setAddFormControlHandler(this::handleAddControl);
    }

    @Override
    public void clearValue() {
        formControls.clear();
        view.setPaginatorVisible(false);
        // TODO: Fire Value changed
    }

    @Override
    public void setValue(@Nonnull Page<FormControlDataDto> value) {
        // TODO: Pristine?
        // TODO: Reuse form controls
        formControls.clear();
        view.clear();
        view.setPaginatorVisible(value.getPageCount() > 1);
        paginatorPresenter.setPageNumber(value.getPageNumber());
        paginatorPresenter.setPageCount(value.getPageCount());
        paginatorPresenter.setElementCount(value.getTotalElements());
        paginatorPresenter.setPageNumberChangedHandler(page -> regionPageChangedHandler.handleRegionPageChanged());
        value.getPageElements()
             .stream()
             .map(this::createFormControl)
             .forEach(this::addFormControl);
    }

    private void handleAddControl() {
        FormControl formControl = formControlFactory.createFormControl();
        addFormControl(formControl);
        formControl.requestFocus();
        ValueChangeEvent.fire(this, getValue());
    }

    private void handleRemoveControl(FormControl formControl) {
        formControls.remove(formControl);
        ValueChangeEvent.fire(this, getValue());
    }

    private void addFormControl(FormControl formControl) {
        FormControlContainer container = view.addFormControlContainer();
        container.setWidget(formControl);
        container.setEnabled(enabled);
        formControls.add(formControl);
        container.setRemoveHandler(() -> {
            formControls.remove(formControl);
            view.removeFormControlContainer(container);
            ValueChangeEvent.fire(this, getValue());
        });
    }

    private FormControl createFormControl(FormControlDataDto dto) {
        FormControl formControl = formControlFactory.createFormControl();
        formControl.setPosition(position);
        formControl.setValue(dto);
        formControl.setEnabled(enabled);
        formControl.addValueChangeHandler(this);
        formControl.setFormRegionFilterChangedHandler(formRegionFilterChangedHandler);
        return formControl;
    }

    @Override
    public void onValueChange(ValueChangeEvent<Optional<FormControlData>> event) {
        // Just pass on to out listeners
        ValueChangeEvent.fire(this, getValue());
    }

    @Nonnull
    @Override
    public ImmutableList<FormControlData> getValue() {
        return formControls.stream()
                           .map(FormControl::getValue)
                           .filter(Optional::isPresent)
                           .map(Optional::get)
                           .collect(toImmutableList());
    }

    @Override
    public boolean isNonEmpty() {
        return formControls.size() > 0;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<FormControlData>> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        formControls.forEach(fc -> fc.setEnabled(enabled));
        view.setEnabled(enabled);
    }

    @Override
    public void requestFocus() {
        formControls.stream().findFirst().ifPresent(FormControl::requestFocus);
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
        // We only show the paginator if we have more than one page of data to display
        view.setPaginatorVisible(pageCount > 1);
    }

    @Override
    public void setPageNumber(int pageNumber) {
        paginatorPresenter.setPageNumber(pageNumber);
    }

    @Override
    public int getPageNumber() {
        return paginatorPresenter.getPageNumber();
    }

    @Override
    public void setPageNumberChangedHandler(PageNumberChangedHandler handler) {
        paginatorPresenter.setPageNumberChangedHandler(handler);
    }

    @Nonnull
    @Override
    public ImmutableList<FormRegionPageRequest> getPageRequests(@Nonnull FormSubject formSubject, @Nonnull FormRegionId formRegionId) {
        Stream<FormRegionPageRequest> controlPages = formControls.stream()
                .map(formControl -> formControl.getPageRequests(formSubject, formRegionId))
                .flatMap(ImmutableList::stream);
        PageRequest stackPageRequest = PageRequest.requestPageWithSize(paginatorPresenter.getPageNumber(), FormPageRequest.DEFAULT_PAGE_SIZE);
        Stream<FormRegionPageRequest> stackPage = Stream.of(FormRegionPageRequest.get(formSubject, formRegionId, FormPageRequest.SourceType.CONTROL_STACK, stackPageRequest));
        return Stream.concat(stackPage, controlPages).collect(toImmutableList());
    }

    @Override
    public void setRegionPageChangedHandler(RegionPageChangedHandler regionPageChangedHandler) {
        this.regionPageChangedHandler = checkNotNull(regionPageChangedHandler);
        formControls.forEach(formControl -> formControl.setRegionPageChangedHandler(regionPageChangedHandler));
    }

    @Override
    public void forEachFormControl(@Nonnull Consumer<FormControl> formControlConsumer) {
        formControls.forEach(formControlConsumer);
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {
        this.formRegionFilterChangedHandler = checkNotNull(handler);
    }
}
