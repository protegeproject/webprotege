package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.change.AddImportData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-27
 */
@AutoValue
public abstract class AddImportChange implements OntologyImportChange {

    public static AddImportChange of(@Nonnull OntologyDocumentId documentId,
                                     @Nonnull OWLImportsDeclaration importsDeclaration) {
        return new AutoValue_AddImportChange(documentId, importsDeclaration);
    }

    @Nonnull
    @Override
    public AddImportChange replaceOntologyId(@Nonnull OntologyDocumentId ontologyId) {
        if(getOntologyDocumentId().equals(ontologyId)) {
            return this;
        }
        else {
            return AddImportChange.of(ontologyId, getImportsDeclaration());
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
    public RemoveImportChange getInverseChange() {
        return RemoveImportChange.of(getOntologyDocumentId(), getImportsDeclaration());
    }
}
