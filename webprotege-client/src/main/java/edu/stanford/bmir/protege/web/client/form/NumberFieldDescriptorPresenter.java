package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.NumberFieldType;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class NumberFieldDescriptorPresenter implements FormFieldDescriptorPresenter {

    @Nonnull
    private final NumberFieldDescriptorView view;

    @Nonnull
    private final NumberFieldRangePresenter rangePresenter;

    @Inject
    public NumberFieldDescriptorPresenter(@Nonnull NumberFieldDescriptorView view,
                                          @Nonnull NumberFieldRangePresenter rangePresenter) {
        this.view = checkNotNull(view);
        this.rangePresenter = checkNotNull(rangePresenter);
    }

    @Nonnull
    @Override
    public FormControlDescriptor getFormFieldDescriptor() {
        return new NumberControlDescriptor(
                view.getFormat(),
                rangePresenter.getNumberFieldRange(),
                NumberFieldType.PLAIN,
                view.getLength(),
                view.getPlaceholder()
        );
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormControlDescriptor formControlDescriptor) {
        if(!(formControlDescriptor instanceof NumberControlDescriptor)) {
            return;
        }
        NumberControlDescriptor numberFieldDescriptor = (NumberControlDescriptor) formControlDescriptor;
        view.setFormat(numberFieldDescriptor.getFormat());
        view.setPlaceholder(numberFieldDescriptor.getPlaceholder());
        rangePresenter.setNumberFieldRange(numberFieldDescriptor.getRange());
    }

    @Override
    public void clear() {
        view.setPlaceholder(LanguageMap.empty());
        view.setFormat("##.#");
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        rangePresenter.start(view.getRangeViewContainer());
    }
}
