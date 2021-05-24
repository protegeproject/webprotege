package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.form.FormDataByFormId;
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
                      @Nonnull FormDataByFormId edited) {
        for(FormId pristineFormId : pristine.keySet()) {
            if(!edited.contains(pristineFormId)) {
                throw new RuntimeException("Pristine form not found in edited forms: " + pristineFormId);
            }
        }
        for(FormId editedFormId : edited.getFormIds()) {
            if(!pristine.containsKey(editedFormId)) {
                throw new RuntimeException("Edited form not found in pristine forms: " + editedFormId);
            }
        }
    }
}
