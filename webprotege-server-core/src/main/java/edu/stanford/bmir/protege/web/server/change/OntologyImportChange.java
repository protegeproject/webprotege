package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;

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
    default OntologyChange replaceIris(@Nonnull IriReplacer iriReplacer) {
        return this;
    }

    @Nonnull
    @Override
    default Set<OWLEntity> getSignature() {
        return Collections.emptySet();
    }

    @Nonnull
    @Override
    default OWLImportsDeclaration getImportsDeclarationOrThrow() {
        return getImportsDeclaration();
    }
}
