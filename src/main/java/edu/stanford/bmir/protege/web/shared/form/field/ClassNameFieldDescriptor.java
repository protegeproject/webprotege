package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(ClassNameFieldDescriptor.TYPE)
public class ClassNameFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "ClassName";

    private ImmutableSet<OWLClass> filteringSuperClasses;

    private NodeType nodeType;

    private ClassNameFieldDescriptor() {
    }

    public ClassNameFieldDescriptor(@Nonnull Set<OWLClass> filteringSuperClasses,
                                    @Nonnull NodeType nodeType) {
        this.filteringSuperClasses = ImmutableSet.copyOf(filteringSuperClasses);
        this.nodeType = checkNotNull(nodeType);
    }

    public static String getFieldTypeId() {
        return TYPE;
    }

    @Nonnull
    @Override
    @JsonIgnore
    public String getAssociatedType() {
        return TYPE;
    }

    @Nonnull
    public final Set<OWLClass> getFilteringSuperClasses() {
        return filteringSuperClasses;
    }

    public NodeType getNodeType() {
        return nodeType;
    }
}
