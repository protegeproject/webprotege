package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-27
 */
public interface OntologyImportChange extends OntologyChange {

    @Nonnull
    OWLImportsDeclaration getImportsDeclaration();

    @Nonnull
    @Override
    default OntologyChange replaceIris(@Nonnull OWLObjectDuplicator duplicator) {
        return this;
    }

    @Nonnull
    @Override
    default Set<OWLEntity> getSignature() {
        return Collections.emptySet();
    }
}
