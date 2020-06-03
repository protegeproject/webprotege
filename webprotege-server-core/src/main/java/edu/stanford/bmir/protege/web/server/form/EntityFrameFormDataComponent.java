package edu.stanford.bmir.protege.web.server.form;

import dagger.Subcomponent;

import javax.annotation.Nonnull;

@Subcomponent(modules = {EntityFrameFormDataModule.class})
@FormDataBuilderSession
public interface EntityFrameFormDataComponent {

    EntityFrameFormDataDtoBuilder createFormDataBuilder();

}
