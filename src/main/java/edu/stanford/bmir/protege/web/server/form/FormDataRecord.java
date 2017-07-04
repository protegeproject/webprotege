package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.annotations.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2017
 */
@Entity(noClassnameStored = true)
@Indexes(
        {
                @Index(fields = {@Field("projectId"), @Field("formCollection")}, options = @IndexOptions(unique = true))
        }
)
public class FormDataRecord {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private String formCollection;

    @Nonnull
    private FormData formData;

    public FormDataRecord(@Nonnull ProjectId projectId,
                          @Nonnull String formCollection,
                          @Nonnull FormData formData) {
        this.projectId = projectId;
        this.formCollection = formCollection;
        this.formData = formData;
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public String getFormCollection() {
        return formCollection;
    }

    @Nonnull
    public FormData getFormData() {
        return formData;
    }
}
