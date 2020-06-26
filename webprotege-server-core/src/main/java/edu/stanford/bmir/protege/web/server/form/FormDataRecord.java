package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.collection.CollectionId;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
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
@Entity(noClassnameStored = true, value = "FormData")
@Indexes(
        {
                @Index(fields = {@Field("projectId"), @Field("collectionId"), @Field("formId"), @Field("id")},
                       options = @IndexOptions(unique = true))
        }
)
public class FormDataRecord {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CollectionId collectionId;

    @Nonnull
    private final FormId formId;

    @Nonnull
    private final String subjectId;

    @Nonnull
    private final FormData data;


    public FormDataRecord(@Nonnull ProjectId projectId,
                          @Nonnull CollectionId collectionId,
                          @Nonnull FormId formId,
                          @Nonnull String subjectId,
                          @Nonnull FormData formData) {
        this.subjectId = subjectId;
        this.projectId = projectId;
        this.formId = formId;
        this.collectionId = collectionId;
        this.data = formData;

    }

    @Nonnull
    public String getSubjectId() {
        return subjectId;
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
    public CollectionId getCollectionId() {
        return collectionId;
    }

    @Nonnull
    public FormData getData() {
        return data;
    }

    @Override
    public String toString() {
        return toStringHelper("FormDataRecord")
                .addValue(projectId)
                .addValue(formId)
                .add("collection", collectionId)
                .add("formData", data)
                .toString();
    }
}
