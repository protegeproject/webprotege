package edu.stanford.bmir.protege.web.client.form;

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
public class FormControlFilterMapper {

    @Nonnull
    private final TextControlFilterPresenterFactory textControlFilterPresenterFactory;

    @Nonnull
    private final NumberControlFilterPresenterFactory numberControlFilterPresenterFactory;

    @Nonnull
    private final EntityNameControlFilterPresenterFactory entityNameControlFilterPresenterFactory;

    @Nonnull
    private final SingleChoiceControlFilterPresenterFactory singleChoiceControlFilterPresenterFactory;

    @Inject
    public FormControlFilterMapper(@Nonnull TextControlFilterPresenterFactory textControlFilterPresenterFactory, @Nonnull NumberControlFilterPresenterFactory numberControlFilterPresenterFactory, @Nonnull EntityNameControlFilterPresenterFactory entityNameControlFilterPresenterFactory, @Nonnull SingleChoiceControlFilterPresenterFactory singleChoiceControlFilterPresenterFactory) {
        this.textControlFilterPresenterFactory = checkNotNull(textControlFilterPresenterFactory);
        this.numberControlFilterPresenterFactory = checkNotNull(numberControlFilterPresenterFactory);
        this.entityNameControlFilterPresenterFactory = checkNotNull(entityNameControlFilterPresenterFactory);
        this.singleChoiceControlFilterPresenterFactory = checkNotNull(singleChoiceControlFilterPresenterFactory);
    }

    @Nonnull
    public Optional<FormControlFilterPresenter> getPresenter(@Nonnull FormControlDescriptorDto descriptor) {
        FormControlFilterPresenter presenter = descriptor.accept(new FormControlDescriptorDtoVisitor<FormControlFilterPresenter>() {
            @Override
            public FormControlFilterPresenter visit(TextControlDescriptorDto textControlDescriptorDto) {
                return textControlFilterPresenterFactory.create(textControlDescriptorDto);
            }

            @Override
            public FormControlFilterPresenter visit(SingleChoiceControlDescriptorDto singleChoiceControlDescriptorDto) {
                return singleChoiceControlFilterPresenterFactory.create(singleChoiceControlDescriptorDto);
            }

            @Override
            public FormControlFilterPresenter visit(MultiChoiceControlDescriptorDto multiChoiceControlDescriptorDto) {
                return null;
            }

            @Override
            public FormControlFilterPresenter visit(NumberControlDescriptorDto numberControlDescriptorDto) {
                return numberControlFilterPresenterFactory.create(numberControlDescriptorDto);
            }

            @Override
            public FormControlFilterPresenter visit(ImageControlDescriptorDto imageControlDescriptorDto) {
                return null;
            }

            @Override
            public FormControlFilterPresenter visit(GridControlDescriptorDto gridControlDescriptorDto) {
                return null;
            }

            @Override
            public FormControlFilterPresenter visit(SubFormControlDescriptorDto subFormControlDescriptorDto) {
                return null;
            }

            @Override
            public FormControlFilterPresenter visit(EntityNameControlDescriptorDto entityNameControlDescriptorDto) {
                return entityNameControlFilterPresenterFactory.create(entityNameControlDescriptorDto);
            }
        });
        return Optional.ofNullable(presenter);
    }

}
