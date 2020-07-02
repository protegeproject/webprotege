package edu.stanford.bmir.protege.web.server.form.processor;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.form.FormFrameBuilder;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.GridColumnBindingMissingException;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowData;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
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
public class GridRowDataProcessor {

    @Nonnull
    private final Provider<FormFrameBuilder> formFrameBuilderProvider;

    @Nonnull
    private final GridCellDataProcessor gridCellDataProcessor;

    @Inject
    public GridRowDataProcessor(@Nonnull Provider<FormFrameBuilder> formFrameBuilderProvider,
                                @Nonnull GridCellDataProcessor gridCellDataProcessor) {
        this.formFrameBuilderProvider = checkNotNull(formFrameBuilderProvider);
        this.gridCellDataProcessor = checkNotNull(gridCellDataProcessor);
    }


    public void processGridRowData(@Nonnull OwlBinding binding,
                                   @Nonnull FormSubjectFactoryDescriptor rowSubjectFactoryDescriptor,
                                   @Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors,
                                   @Nonnull FormFrameBuilder gridFrameBuilder,
                                   GridRowData gridRowData) {
        var rowFrameBuilder = formFrameBuilderProvider.get();

        var rowSubject = gridRowData.getSubject();
        // Set subject if it is present.  If it is not present
        // then a subject should be generated
        rowSubject.ifPresent(rowFrameBuilder::setSubject);
        rowFrameBuilder.setSubjectFactoryDescriptor(rowSubjectFactoryDescriptor);
        var rowCells = gridRowData.getCells();
        for(int i = 0; i < columnDescriptors.size(); i++) {
            var columnDescriptor = columnDescriptors.get(i);
            var gridCellData = rowCells.get(i);
            var columnId = columnDescriptor.getId();
            var cellBinding = columnDescriptor.getOwlBinding().orElseThrow(() -> new GridColumnBindingMissingException(columnId));
            gridCellDataProcessor.processGridCellData(rowFrameBuilder, cellBinding, gridCellData);
        }
        gridFrameBuilder.add(binding, rowFrameBuilder);
    }
}
