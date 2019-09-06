package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.util.IRIShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-21
 */
public class IriShortFormAdapter implements IRIShortFormProvider {

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitesByIri;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Inject
    public IriShortFormAdapter(@Nonnull EntitiesInProjectSignatureByIriIndex entitesByIri,
                               @Nonnull DictionaryManager dictionaryManager) {
        this.entitesByIri = entitesByIri;
        this.dictionaryManager = dictionaryManager;
    }

    @Nonnull
    @Override
    public String getShortForm(@Nonnull IRI iri) {
        return entitesByIri.getEntitiesInSignature(iri)
                .sorted()
                .map(dictionaryManager::getShortForm)
                .findFirst()
                .orElse(iri.toQuotedString());
    }
}
