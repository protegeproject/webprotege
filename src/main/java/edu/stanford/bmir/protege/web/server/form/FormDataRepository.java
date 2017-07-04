package edu.stanford.bmir.protege.web.server.form;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2017
 */
@ProjectSingleton
public class FormDataRepository {

    @Nonnull
    private final Datastore datastore;

    @Inject
    public FormDataRepository(@Nonnull Datastore datastore) {
        this.datastore = datastore;
    }

    public void store(ProjectId projectId, OWLEntity entity, FormData formData) {
        Query<FormDataRecord> query = datastore.createQuery(FormDataRecord.class)
                                               .field("projectId").equal(projectId)
                                               .field("formCollection").equal(entity.toString());
        UpdateOperations<FormDataRecord> update = datastore.createUpdateOperations(FormDataRecord.class);
        update.set("formData", formData);
        datastore.update(query, update, new UpdateOptions().upsert(true));
//        datastore.save(new FormDataRecord(projectId,
//                                          entity.toString(),
//                                          formData));
    }

    public FormData get(ProjectId projectId,
                        OWLEntity entity) {

        FormDataRecord record = datastore.find(FormDataRecord.class)
                 .field("projectId").equal(projectId)
                 .field("formCollection").equal(entity.toString())
                .get();
        if(record == null) {
            return FormData.empty();
        }
        else {
            return record.getFormData();
        }

    }
}
