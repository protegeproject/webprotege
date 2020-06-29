package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-16
 */
public class GridColumnFilterPresenter {

    @Nonnull
    private final GridColumnFilterView view;

    @Nonnull
    private final FormControlFilterMapper formControlFilterMapper;

    private Optional<FormControlFilterPresenter> presenter = Optional.empty();

    @Inject
    public GridColumnFilterPresenter(@Nonnull GridColumnFilterView view, @Nonnull FormControlFilterMapper formControlFilterMapper) {
        this.view = checkNotNull(view);
        this.formControlFilterMapper = checkNotNull(formControlFilterMapper);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setGridColumnDescriptor(@Nonnull GridColumnDescriptorDto descriptor) {
        FormControlDescriptorDto formControlDescriptor = descriptor.getFormControlDescriptor();
        Optional<FormControlFilterPresenter> presenter = formControlFilterMapper.getPresenter(formControlDescriptor);
        presenter.ifPresent(thePresenter -> {
            thePresenter.start(view.getFilterContainer());
            this.presenter = Optional.of(thePresenter);
        });
    }

    public Optional<PrimitiveFormControlDataMatchCriteria> getFilter() {
        return presenter.flatMap(FormControlFilterPresenter::getFilter);
    }
}
