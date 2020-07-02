package edu.stanford.bmir.protege.web.server.form.processor;

import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellData;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GridCellDataProcessor {

    @Nonnull
    private final Provider<FormControlDataProcessor> formControlDataProcessorProvider;

    @Inject
    public GridCellDataProcessor(@Nonnull Provider<FormControlDataProcessor> formControlDataProcessorProvider) {
        this.formControlDataProcessorProvider = checkNotNull(formControlDataProcessorProvider);
    }

    public void processGridCellData(@Nonnull FormFrameBuilder rowFrameBuilder,
                                    @Nonnull OwlBinding cellBinding,
                                    @Nonnull GridCellData gridCellData) {
        var formControlDataProcessor = formControlDataProcessorProvider.get();
        gridCellData.getValues()
                    .forEach(cellControlData -> {
                        formControlDataProcessor.processFormControlData(cellBinding,
                                                                        cellControlData,
                                                                        rowFrameBuilder);
                    });
    }

}
