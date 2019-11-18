package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.NumberFieldDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.NumberFieldRange;
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
    public FormFieldDescriptor getFormFieldDescriptor() {
        return new NumberFieldDescriptor(
                view.getFormat(),
                rangePresenter.getNumberFieldRange(),
                NumberFieldType.PLAIN,
                view.getLength(),
                view.getPlaceholder()
        );
    }

    @Override
    public void setFormFieldDescriptor(@Nonnull FormFieldDescriptor formFieldDescriptor) {
        if(!(formFieldDescriptor instanceof NumberFieldDescriptor)) {
            return;
        }
        NumberFieldDescriptor numberFieldDescriptor = (NumberFieldDescriptor) formFieldDescriptor;
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
