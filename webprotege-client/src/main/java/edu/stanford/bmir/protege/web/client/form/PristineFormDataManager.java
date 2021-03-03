package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-13
 */
public class PristineFormDataManager {

    private Optional<OWLEntity> currentEntity = Optional.empty();

    private final Map<FormId, FormData> pristineFormData = new HashMap<>();

    @Inject
    public PristineFormDataManager() {
    }

    /**
     * Puts the specified {@link FormData} into this display manager, marking it
     * as displayed form data.
     */
    public void updatePristineFormData(@Nonnull FormData formData) {
        if(!currentEntity.isPresent()) {
            throw currentEntityNotSetException();
        }
        FormId formId = formData.getFormId();
        pristineFormData.put(formId, formData);
    }

    /**
     * Checks to see whether this manager is tracking form data for the form specified by the given
     * form id.
     * @return true of there is form data displayed for the
     */
    public boolean containsPristineFormData(@Nonnull FormId formId) {
        return pristineFormData.containsKey(formId);
    }

    /**
     * Gets the pristine data for the specified form ids.  This method guarantees that
     * there will be pristine data for the specified form ids.  If there is no pristine
     * data for any of the specified form ids then a {@link RuntimeException} is thrown.
     */
    @Nonnull
    public ImmutableMap<FormId, FormData> getPristineFormData(Collection<FormId> formIds) {
        ImmutableMap.Builder<FormId, FormData> result = ImmutableMap.builder();
        for(FormId formId : formIds) {
            FormData pristine = pristineFormData.get(formId);
            if(pristine == null) {
                throw new RuntimeException("Pristine data not available for " + formId);
            }
            result.put(formId, pristine);
        }
        return result.build();
    }

    /**
     * Resets the underlying entity for which this manager manages pristine form data.
     * This method should be called before any pristine form data is added to this manager.
     */
    public void resetCurrentEntity(@Nonnull OWLEntity entity) {
        pristineFormData.clear();
        this.currentEntity = Optional.of(entity);
    }

    /**
     * Clears the underlying entity for which this manager manages pristine form data.
     */
    public void clearCurrentEntity() {
        pristineFormData.clear();
        currentEntity = Optional.empty();
    }


    private static IllegalStateException currentEntityNotSetException() {
        return new IllegalStateException("Current entity is not set.  Cannot set form data if current entity is not set.");
    }

}
