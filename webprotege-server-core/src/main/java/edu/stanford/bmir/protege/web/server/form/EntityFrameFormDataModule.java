package edu.stanford.bmir.protege.web.server.form;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@Module
public class EntityFrameFormDataModule {

    @Nonnull
    private final FormRegionOrderingIndex formRegionOrderingIndex;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Nonnull
    private final FormPageRequestIndex pageRequestIndex;


    public EntityFrameFormDataModule(@Nonnull FormRegionOrderingIndex formRegionOrderingIndex,
                                     @Nonnull LangTagFilter langTagFilter,
                                     @Nonnull FormPageRequestIndex pageRequestIndex) {
        this.formRegionOrderingIndex = formRegionOrderingIndex;
        this.langTagFilter = langTagFilter;
        this.pageRequestIndex = pageRequestIndex;
    }


    @Provides
    public FormRegionOrderingIndex provideFormRegionOrderingIndex() {
        return formRegionOrderingIndex;
    }

    @Provides
    public LangTagFilter provideLangTagFilter() {
        return langTagFilter;
    }

    @Provides
    public FormPageRequestIndex providePageRequestIndex() {
        return pageRequestIndex;
    }
}
