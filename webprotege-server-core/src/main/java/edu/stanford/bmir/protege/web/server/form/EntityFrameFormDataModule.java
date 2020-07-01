package edu.stanford.bmir.protege.web.server.form;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

@Module
public class EntityFrameFormDataModule {

    @Nonnull
    private final FormRegionOrderingIndex formRegionOrderingIndex;

    @Nonnull
    private final LangTagFilter langTagFilter;

    @Nonnull
    private final FormPageRequestIndex pageRequestIndex;

    @Nonnull
    private FormRegionFilterIndex formRegionFilterIndex;


    public EntityFrameFormDataModule(@Nonnull FormRegionOrderingIndex formRegionOrderingIndex,
                                     @Nonnull LangTagFilter langTagFilter,
                                     @Nonnull FormPageRequestIndex pageRequestIndex, @Nonnull FormRegionFilterIndex formRegionFilterIndex) {
        this.formRegionOrderingIndex = checkNotNull(formRegionOrderingIndex);
        this.langTagFilter = checkNotNull(langTagFilter);
        this.pageRequestIndex = checkNotNull(pageRequestIndex);
        this.formRegionFilterIndex = checkNotNull(formRegionFilterIndex);
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

    @Provides
    public FormRegionFilterIndex provideFormRegionFilterIndex() {
        return formRegionFilterIndex;
    }
}
