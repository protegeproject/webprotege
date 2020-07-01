package edu.stanford.bmir.protege.web.client.form;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.SingleChoiceControlDescriptorDto;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.NumericValueCriteria;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-18
 */
public class SingleChoiceControlFilterPresenter implements FormControlFilterPresenter {

    @Nonnull
    private final SingleChoiceControlFilterView view;

    @Nonnull
    private final SingleChoiceControlDescriptorDto descriptor;

    @Nonnull
    private final FormControlFactory formControlFactory;

    @Nonnull
    private Optional<FormControl> formControl = Optional.empty();

    @AutoFactory
    public SingleChoiceControlFilterPresenter(@Provided @Nonnull SingleChoiceControlFilterView view,
                                              @Nonnull SingleChoiceControlDescriptorDto descriptor,
                                              @Provided @Nonnull FormControlFactory formControlFactory) {
        this.view = checkNotNull(view);
        this.descriptor = checkNotNull(descriptor);
        this.formControlFactory = checkNotNull(formControlFactory);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        FormControlDataEditorFactory dataEditorFactory = formControlFactory.getDataEditorFactory(descriptor);
        FormControl formControl = dataEditorFactory.createFormControl();
        this.formControl = Optional.of(formControl);
        view.getContainer().setWidget(formControl);
    }

    @Nonnull
    @Override
    public Optional<PrimitiveFormControlDataMatchCriteria> getFilter() {
        return formControl.flatMap(control -> control.getValue().flatMap(this::getPrimitiveFormControlDataMatchCriteria));
    }

    @Nonnull
    private Optional<PrimitiveFormControlDataMatchCriteria> getPrimitiveFormControlDataMatchCriteria(
            FormControlData data) {
        PrimitiveFormControlDataMatchCriteria criteria = data.accept(new FormControlDataVisitorEx<PrimitiveFormControlDataMatchCriteria>() {
            @Override
            public PrimitiveFormControlDataMatchCriteria visit(@Nonnull EntityNameControlData entityNameControlData) {
                return getEntityCriteria(entityNameControlData);
            }

            @Override
            public PrimitiveFormControlDataMatchCriteria visit(@Nonnull FormData formData) {
                return null;
            }

            @Override
            public PrimitiveFormControlDataMatchCriteria visit(@Nonnull GridControlData gridControlData) {
                return null;
            }

            @Override
            public PrimitiveFormControlDataMatchCriteria visit(@Nonnull ImageControlData imageControlData) {
                return null;
            }

            @Override
            public PrimitiveFormControlDataMatchCriteria visit(@Nonnull MultiChoiceControlData multiChoiceControlData) {
                return null;
            }

            @Override
            public PrimitiveFormControlDataMatchCriteria visit(@Nonnull SingleChoiceControlData singleChoiceControlData) {
                return singleChoiceControlData.getChoice()
                                              .flatMap(PrimitiveFormControlData::asEntity)
                        .map(EntityIsCriteria::get)
                        .map(EntityFormControlDataMatchCriteria::get)
                        .orElse(null);
            }

            @Override
            public PrimitiveFormControlDataMatchCriteria visit(@Nonnull NumberControlData numberControlData) {
                return null;
            }

            @Override
            public PrimitiveFormControlDataMatchCriteria visit(@Nonnull TextControlData textControlData) {
                return null;
            }
        });
        return Optional.ofNullable(criteria);
    }

    @Nullable
    private EntityFormControlDataMatchCriteria getEntityCriteria(@Nonnull EntityNameControlData entityNameControlData) {
        return entityNameControlData.getEntity()
                .map(EntityIsCriteria::get)
                .map(EntityFormControlDataMatchCriteria::get)
                                    .orElse(null);
    }

    @Override
    public void setFilter(@Nonnull FormRegionFilter filter) {

    }

    @Override
    public void clear() {
        formControl.ifPresent(FormControl::clearValue);
    }
}
