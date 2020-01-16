package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridRowData {

    public static GridRowData get(@Nullable FormSubject subject,
                                  @Nonnull ImmutableList<GridCellData> cellData) {
        return new AutoValue_GridRowData(subject, cellData);
    }


    @Nullable
    protected abstract FormSubject getSubjectInternal();

    @Nonnull
    public Optional<FormSubject> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @Nonnull
    public abstract ImmutableList<GridCellData> getCells();
}
