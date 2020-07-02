package edu.stanford.bmir.protege.web.server.form.data;

import edu.stanford.bmir.protege.web.shared.form.data.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class FormControlDataDtoComparator implements Comparator<FormControlDataDto> {

    private final Provider<GridControlDataDtoComparator> gridControlDataDtoComparatorProvider;

    @Nullable
    private GridControlDataDtoComparator gridControlDataDtoComparator = null;

    @Nonnull
    private final EntityNameControlDataDtoComparator entityNameControlDataComparator;

    @Nonnull
    private final ImageControlDataDtoComparator imageControlDataDtoComparator;

    @Nonnull
    private final MultiChoiceControlDataDtoComparator multiChoiceControlDataDtoComparator;

    @Nonnull
    private final SingleChoiceControlDataDtoComparator singleChoiceControlDataDtoComparator;



    @Nullable
    private FormDataDtoComparator formDataDtoComparator;

    @Nonnull
    private final Provider<FormDataDtoComparator> formDataDtoComparatorProvider;

    @Nonnull
    private final NumberControlDataDtoComparator numberControlDataDtoComparator;

    @Nonnull
    private final TextControlDataDtoComparator textControlDataDtoComparator;

    @Inject
    public FormControlDataDtoComparator(@Nonnull Provider<GridControlDataDtoComparator> gridControlDataDtoComparatorProvider,
                                        @Nonnull EntityNameControlDataDtoComparator entityNameControlDataComparator,
                                        @Nonnull ImageControlDataDtoComparator imageControlDataDtoComparator,
                                        @Nonnull MultiChoiceControlDataDtoComparator multiChoiceControlDataDtoComparator,
                                        @Nonnull SingleChoiceControlDataDtoComparator singleChoiceControlDataDtoComparator,
                                        @Nonnull Provider<FormDataDtoComparator> formDataDtoComparatorProvider,
                                        @Nonnull NumberControlDataDtoComparator numberControlDataDtoComparator,
                                        @Nonnull TextControlDataDtoComparator textControlDataDtoComparator) {
        this.gridControlDataDtoComparatorProvider = checkNotNull(gridControlDataDtoComparatorProvider);
        this.entityNameControlDataComparator = entityNameControlDataComparator;
        this.imageControlDataDtoComparator = imageControlDataDtoComparator;
        this.multiChoiceControlDataDtoComparator = multiChoiceControlDataDtoComparator;
        this.singleChoiceControlDataDtoComparator = singleChoiceControlDataDtoComparator;
        this.formDataDtoComparatorProvider = formDataDtoComparatorProvider;
        this.numberControlDataDtoComparator = numberControlDataDtoComparator;
        this.textControlDataDtoComparator = textControlDataDtoComparator;
    }


    @Override
    public int compare(FormControlDataDto d1, FormControlDataDto d2) {
        return d1.accept(new FormControlDataDtoVisitorEx<>() {
            @Override
            public Integer visit(@Nonnull EntityNameControlDataDto entityNameControlData) {
                return entityNameControlDataComparator.compare(entityNameControlData, with(d2));
            }

            @Override
            public Integer visit(@Nonnull FormDataDto d1) {
                return getFormDataDtoComparator().compare(d1, with(d2));
            }

            @Override
            public Integer visit(@Nonnull GridControlDataDto d1) {
                return getGridControlDataDtoComparator().compare(d1, with(d2));
            }

            @Override
            public Integer visit(@Nonnull ImageControlDataDto d1) {
                return imageControlDataDtoComparator.compare(d1, with(d2));
            }

            @Override
            public Integer visit(@Nonnull MultiChoiceControlDataDto d1) {
                return multiChoiceControlDataDtoComparator.compare(d1, with(d2));
            }

            @Override
            public Integer visit(@Nonnull SingleChoiceControlDataDto d1) {
                return singleChoiceControlDataDtoComparator.compare(d1, with(d2));
            }

            @Override
            public Integer visit(@Nonnull NumberControlDataDto d1) {
                return numberControlDataDtoComparator.compare(d1, with(d2));
            }

            @Override
            public Integer visit(@Nonnull TextControlDataDto d1) {
                return textControlDataDtoComparator.compare(d1, with(d2));
            }
        });
    }

    private FormDataDtoComparator getFormDataDtoComparator() {
        if(formDataDtoComparator == null) {
            formDataDtoComparator = formDataDtoComparatorProvider.get();
        }
        return formDataDtoComparator;
    }


    @SuppressWarnings("unchecked")
    private static <T> T with(Object o) {
        // Okay for class cast exception here
        return (T) o;
    }

    private Comparator<GridControlDataDto> getGridControlDataDtoComparator() {
        if(gridControlDataDtoComparator == null) {
            gridControlDataDtoComparator = gridControlDataDtoComparatorProvider.get();
        }
        return gridControlDataDtoComparator;
    }
}
