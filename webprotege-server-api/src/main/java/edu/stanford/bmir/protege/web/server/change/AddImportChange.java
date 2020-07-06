package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.change.AddImportData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-27
 */
@AutoValue
public abstract class AddImportChange implements OntologyImportChange {

    public static AddImportChange of(@Nonnull OWLOntologyID ontologyID,
                                     @Nonnull OWLImportsDeclaration importsDeclaration) {
        return new AutoValue_AddImportChange(ontologyID, importsDeclaration);
    }

    @Nonnull
    @Override
    public OWLOntologyChangeRecord toOwlOntologyChangeRecord() {
        return new OWLOntologyChangeRecord(getOntologyId(), new AddImportData(getImportsDeclaration()));
    }

    @Nonnull
    @Override
    public AddImportChange replaceOntologyId(@Nonnull OWLOntologyID ontologyId) {
        if(getOntologyId().equals(ontologyId)) {
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
        return RemoveImportChange.of(getOntologyId(), getImportsDeclaration());
    }
}
