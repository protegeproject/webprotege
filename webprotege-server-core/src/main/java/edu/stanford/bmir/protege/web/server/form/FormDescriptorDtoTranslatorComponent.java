package edu.stanford.bmir.protege.web.server.form;

import dagger.Subcomponent;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-29
 */

@Subcomponent(modules = {EntityFrameFormDataModule.class})
@FormDataBuilderSession
public interface FormDescriptorDtoTranslatorComponent {

    FormDescriptorDtoTranslator getFormDescriptorDtoTranslator();
}
