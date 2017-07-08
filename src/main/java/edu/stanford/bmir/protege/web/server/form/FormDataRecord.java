package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.mongodb.morphia.annotations.*;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Jul 2017
 */
@Entity(noClassnameStored = true)
@Indexes(
        {
                @Index(fields = {@Field("projectId"), @Field("formId"), @Field("formCollection")}, options = @IndexOptions(unique = true))
        }
)
public class FormDataRecord {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final FormId formId;

    @Nonnull
    private String formCollection;

    @Nonnull
    private FormData formData;

    public FormDataRecord(@Nonnull ProjectId projectId,
                          @Nonnull FormId formId,
                          @Nonnull String formCollection,
                          @Nonnull FormData formData) {
        this.projectId = projectId;
        this.formId = formId;
        this.formCollection = formCollection;
        this.formData = formData;
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public FormId getFormId() {
        return formId;
    }

    @Nonnull
    public String getFormCollection() {
        return formCollection;
    }

    @Nonnull
    public FormData getFormData() {
        return formData;
    }


    @Override
    public String toString() {
        return toStringHelper("FormDataRecord")
                .addValue(projectId)
                .addValue(formId)
                .add("collection", formCollection)
                .add("formData", formData)
                .toString();
    }
}
