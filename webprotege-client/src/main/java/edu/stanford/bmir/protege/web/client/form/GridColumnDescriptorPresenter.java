package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.uuid.UuidV4Provider;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public class GridColumnDescriptorPresenter implements ObjectPresenter<GridColumnDescriptor> {

    @Nonnull
    private final GridColumnDescriptorView view;

    private final FormControlDescriptorChooserPresenter fieldDescriptorChooserPresenter;

    private final OwlBindingPresenter bindingPresenter;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Nonnull
    private Optional<GridColumnId> currentColumnId = Optional.empty();

    @Nonnull
    private final UuidV4Provider uuidV4Provider;

    private Consumer<String> headerLabelChangedHandler = label -> {};

    @Inject
    public GridColumnDescriptorPresenter(@Nonnull GridColumnDescriptorView view,
                                         @Nonnull FormControlDescriptorChooserPresenter fieldDescriptorChooserPresenter,
                                         @Nonnull OwlBindingPresenter bindingPresenter,
                                         @Nonnull LanguageMapCurrentLocaleMapper localeMapper, @Nonnull UuidV4Provider uuidV4Provider) {
        this.view = checkNotNull(view);
        this.fieldDescriptorChooserPresenter = checkNotNull(fieldDescriptorChooserPresenter);
        this.bindingPresenter = checkNotNull(bindingPresenter);
        this.localeMapper = checkNotNull(localeMapper);
        this.uuidV4Provider = checkNotNull(uuidV4Provider);
    }

    @Nonnull
    @Override
    public String getHeaderLabel() {
        return localeMapper.getValueForCurrentLocale(view.getLabel());
    }

    @Nonnull
    @Override
    public Optional<GridColumnDescriptor> getValue() {
        GridColumnId columnId = currentColumnId.orElseGet(() -> GridColumnId.get(uuidV4Provider.get()));
        return fieldDescriptorChooserPresenter.getFormFieldDescriptor()
                                              .map(fieldDescriptor -> GridColumnDescriptor.get(columnId,
                                                                                               view.getOptionality(),
                                                                                               view.getRepeatability(),
                                                                                               bindingPresenter.getBinding()
                                                                                                               .orElse(null),
                                                                                               view.getLabel(),
                                                                                               fieldDescriptor));
    }

    public void setValue(@Nonnull GridColumnDescriptor descriptor) {
        this.currentColumnId = Optional.of(descriptor.getId());
        view.setOptionality(descriptor.getOptionality());
        view.setRepeatability(descriptor.getRepeatability());
        view.setLabel(descriptor.getLabel());
        bindingPresenter.clear();
        descriptor.getOwlBinding()
                  .ifPresent(bindingPresenter::setBinding);
        fieldDescriptorChooserPresenter.setFormFieldDescriptor(descriptor.getFormControlDescriptor());
    }

    @Override
    public void setHeaderLabelChangedHandler(Consumer<String> headerLabelHandler) {
        this.headerLabelChangedHandler = checkNotNull(headerLabelHandler);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        fieldDescriptorChooserPresenter.start(view.getFieldDescriptorChooserContainer());
        bindingPresenter.start(view.getBindingViewContainer());
        view.setLabelChangedHandler(() -> headerLabelChangedHandler.accept(getHeaderLabel()));
    }
}
