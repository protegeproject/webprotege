package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.shared.form.FormData;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;

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

    private Map<OWLEntity, FormData> map = new HashMap<>();

    @Inject
    public FormDataRepository() {
    }

    public void store(OWLEntity entity, FormData formData) {
        map.put(entity, formData);
    }

    public FormData get(OWLEntity entity) {
        FormData formData = map.get(entity);
        if(formData == null) {
            return new FormData(Collections.emptyMap());
        }
        else {
            return formData;
        }
    }
}
