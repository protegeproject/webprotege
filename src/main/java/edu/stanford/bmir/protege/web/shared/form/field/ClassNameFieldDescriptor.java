package edu.stanford.bmir.protege.web.shared.form.field;

import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class ClassNameFieldDescriptor implements FormFieldDescriptor {

    private static final String CLASS_NAME_FIELD = "ClassNameField";

    private Set<OWLClass> filteringSuperClasses = new HashSet<>();

    private static String re;

    private ClassNameFieldDescriptor() {
    }

    public ClassNameFieldDescriptor(Set<OWLClass> filteringSuperClasses) {
        this.filteringSuperClasses.addAll(filteringSuperClasses);
    }

    public static String getFieldTypeId() {
        return CLASS_NAME_FIELD;
    }

    @Override
    public String getAssociatedFieldTypeId() {
        return CLASS_NAME_FIELD;
    }

    public final Set<OWLClass> getFilteringSuperClasses() {
        return new HashSet<>(filteringSuperClasses);
    }
}
