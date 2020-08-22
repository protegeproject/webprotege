package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-04
 */
@AutoValue
public abstract class FormDescriptorRecord implements Comparable<FormDescriptorRecord> {

    private static final Comparator<FormDescriptorRecord> comparingByOrdinal = Comparator.comparing(FormDescriptorRecord::getOrdinal);

    public static final String PROJECT_ID = "projectId";

    public static final String FORM_DESCRIPTOR = "formDescriptor";

    public static final String ORDINAL = "ordinal";


    @JsonCreator
    public static FormDescriptorRecord get(@JsonProperty(PROJECT_ID) @Nonnull ProjectId projectId,
                                           @JsonProperty(FORM_DESCRIPTOR) FormDescriptor formDescriptor,
                                           @JsonProperty(ORDINAL) Integer ordinal) {
        return new AutoValue_FormDescriptorRecord(projectId,
                                                  formDescriptor == null ? FormDescriptor.empty(FormId.generate()) : formDescriptor,
                                                  ordinal == null ? 0 : ordinal);
    }

    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty(FORM_DESCRIPTOR)
    @Nonnull
    public abstract FormDescriptor getFormDescriptor();

    @JsonProperty(ORDINAL)
    @Nonnull
    public abstract Integer getOrdinal();

    @Override
    public int compareTo(@Nonnull FormDescriptorRecord o) {
        return comparingByOrdinal
                .compare(this, o);
    }
}
