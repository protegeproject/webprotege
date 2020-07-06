package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;

import javax.annotation.Nonnull;

@Subcomponent(modules = {EntityFrameFormDataModule.class})
@FormDataBuilderSession
public interface EntityFrameFormDataComponent {

    @Nonnull
    EntityFrameFormDataDtoBuilder formDataBuilder();

//    @Subcomponent.Builder
//    interface Builder {
//
//        @BindsInstance
//        Builder withFormRegionOrdering(@Nonnull ImmutableList<FormRegionOrdering> formRegionOrderings);
//
//        @BindsInstance
//        Builder withLanguageTagFilter(@Nonnull LangTagFilter filter);
//
//        @BindsInstance
//        Builder withFormPageRequestIndex(@Nonnull FormPageRequestIndex pageRequestIndex);
//
//        @Nonnull
//        EntityFrameFormDataComponent build();
//    }
}
