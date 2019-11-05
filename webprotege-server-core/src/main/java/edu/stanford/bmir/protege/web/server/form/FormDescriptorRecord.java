package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-04
 */
@AutoValue
public abstract class FormDescriptorRecord {

    @JsonCreator
    public static FormDescriptorRecord get(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                           @JsonProperty("formDescriptor") @Nonnull FormDescriptor formDescriptor) {
        return new AutoValue_FormDescriptorRecord(projectId, formDescriptor);
    }

    @JsonProperty("projectId")
    @Nonnull
    public abstract ProjectId getProjectId();

    @JsonProperty("formDescriptor")
    @Nonnull
    public abstract FormDescriptor getFormDescriptor();

}
