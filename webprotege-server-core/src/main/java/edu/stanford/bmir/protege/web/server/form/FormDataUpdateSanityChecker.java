package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-29
 */
public class FormDataUpdateSanityChecker {

    public static void check(@Nonnull ImmutableMap<FormId, FormData> pristine,
                      @Nonnull ImmutableMap<FormId, FormData> edited) {
        for(FormId pristineFormId : pristine.keySet()) {
            if(!edited.containsKey(pristineFormId)) {
                throw new RuntimeException("Pristine form not found in edited forms: " + pristineFormId);
            }
        }
        for(FormId editedFormId : edited.keySet()) {
            if(!pristine.containsKey(editedFormId)) {
                throw new RuntimeException("Edited form not found in pristine forms: " + editedFormId);
            }
            var pristineData = pristine.get(editedFormId);
            var editedData = edited.get(editedFormId);
            if(!pristineData.getFormDescriptor().equals(editedData.getFormDescriptor())) {
                throw new RuntimeException("Pristine and Edited form descriptors are not equal.  FormId: " + edited);
            }
        }
    }
}
