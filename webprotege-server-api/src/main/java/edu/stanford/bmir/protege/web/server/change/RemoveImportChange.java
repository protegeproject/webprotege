package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.change.RemoveImportData;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-27
 */
@AutoValue
public abstract class RemoveImportChange implements OntologyImportChange {

    public static RemoveImportChange of(@Nonnull OntologyDocumentId ontologyId,
                                        @Nonnull OWLImportsDeclaration importsDeclaration) {
        return new AutoValue_RemoveImportChange(ontologyId, importsDeclaration);
    }

    @Nonnull
    @Override
    public RemoveImportChange replaceOntologyId(@Nonnull OntologyDocumentId ontologyId) {
        if(getOntologyDocumentId().equals(ontologyId)) {
            return this;
        }
        else {
            return RemoveImportChange.of(ontologyId, getImportsDeclaration());
        }
    }

    @Override
    public void accept(@Nonnull OntologyChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull OntologyChangeVisitorEx<R> visitorEx) {
        return visitorEx.visit(this);
    }

    @Nonnull
    @Override
    public AddImportChange getInverseChange() {
        return AddImportChange.of(getOntologyDocumentId(), getImportsDeclaration());
    }
}
