package edu.stanford.bmir.protege.web.shared.usage;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class UsageReference implements Serializable, Comparable<UsageReference> {


    private AxiomType axiomType;

    private String axiomRendering;

    private OWLEntity subject;

    private String subjectRendering;


    /**
     * For serialization purposes only
     */
    private UsageReference() {
    }

    public UsageReference(AxiomType<?> axiomType, String axiomRendering, java.util.Optional<OWLEntity> axiomSubject, Optional<String> subjectRendering) {
        this.subject = checkNotNull(axiomSubject).orElse(null);
        this.subjectRendering = subjectRendering.orElse("");
        this.axiomType = checkNotNull(axiomType);
        this.axiomRendering = checkNotNull(axiomRendering);
    }


    public String getSubjectRendering() {
        return subjectRendering;
    }

    public java.util.Optional<OWLEntity> getAxiomSubject() {
        return java.util.Optional.ofNullable(subject);
    }

    public AxiomType getAxiomType() {
        return axiomType;
    }

    public String getAxiomRendering() {
        return axiomRendering;
    }

    @Override
    public int hashCode() {
        return "UsageReference".hashCode() + Optional.ofNullable(subject).hashCode() + axiomType.hashCode() + axiomRendering.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof UsageReference)) {
            return false;
        }
        UsageReference other = (UsageReference) obj;
        return Optional.ofNullable(this.subject).equals(Optional.ofNullable(other.subject)) && this.axiomType.equals(other.axiomType) && this.axiomRendering.equals(other.axiomRendering);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UsageReference");
        sb.append("(Subject(");
        sb.append(subject);
        sb.append(") AxiomType(");
        sb.append(axiomType);
        sb.append(") AxiomRendering(");
        sb.append(axiomRendering);
        sb.append("))");
        return sb.toString();
    }

    @Override
    public int compareTo(UsageReference o) {
        if(subject == null) {
            if(o.subject != null) {
                return 1;
            }
        }
        else {
            if(o.subject == null) {
                return -1;
            }
        }
        int subjectDiff = subjectRendering.compareTo(o.subjectRendering);
        if(subjectDiff != 0) {
            return subjectDiff;
        }
        int axiomTypeDiff = axiomType.getIndex() - o.axiomType.getIndex();
        if(axiomTypeDiff != 0) {
            return axiomTypeDiff;
        }
        return axiomRendering.compareTo(o.axiomRendering);
    }
}
