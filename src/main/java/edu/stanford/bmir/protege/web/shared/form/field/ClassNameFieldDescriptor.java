package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(ClassNameFieldDescriptor.TYPE)
public class ClassNameFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "ClassName";

    private Set<OWLClass> filteringSuperClasses = ImmutableSet.of();

    private NodeType nodeType = NodeType.LEAF;

    private String placeholder = "";

    private ClassNameFieldDescriptor() {
    }

    public ClassNameFieldDescriptor(@Nonnull Set<OWLClass> filteringSuperClasses,
                                    @Nonnull NodeType nodeType,
                                    @Nonnull String placeholder) {
        this.filteringSuperClasses = ImmutableSet.copyOf(filteringSuperClasses);
        this.nodeType = checkNotNull(nodeType);
        this.placeholder = placeholder;
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

    public String getPlaceholder() {
        return placeholder;
    }

    @Nonnull
    public final Set<OWLClass> getFilteringSuperClasses() {
        return filteringSuperClasses;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(filteringSuperClasses, nodeType, placeholder);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ClassNameFieldDescriptor)) {
            return false;
        }
        ClassNameFieldDescriptor other = (ClassNameFieldDescriptor) obj;
        return this.filteringSuperClasses.equals(other.filteringSuperClasses)
                && this.nodeType == other.nodeType
                && this.placeholder.equals(other.placeholder);
    }


    @Override
    public String toString() {
        return toStringHelper("ClassNameFieldDescriptor")
                .add("nodeType", nodeType)
                .add("filteringSuperClasses", filteringSuperClasses)
                .add("placeholder", placeholder)
                .toString();
    }
}
