package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.project.BuiltInPrefixDeclarations;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.shortform.PrefixedNameDictionaryLanguage;
import org.apache.lucene.document.Document;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
public class EntityPrefixedNameDocumentAugmenter implements EntityDocumentAugmenter {

    @Nonnull
    private final ImmutableMap<String, String> builtInPrefixDeclarationsByPrefix;

    @Nonnull
    private final DictionaryLanguageFieldWriter fieldWriter;

    @Inject
    public EntityPrefixedNameDocumentAugmenter(@Nonnull DictionaryLanguageFieldWriter fieldWriter,
                                               @Nonnull BuiltInPrefixDeclarations builtInPrefixDeclarations) {
        this.fieldWriter = checkNotNull(fieldWriter);
        builtInPrefixDeclarationsByPrefix = getPrefixDeclarationsByPrefix(builtInPrefixDeclarations);
    }

    private static ImmutableMap<String, String> getPrefixDeclarationsByPrefix(@Nonnull BuiltInPrefixDeclarations builtInPrefixDeclarations) {
        return builtInPrefixDeclarations.getPrefixDeclarations()
                                        .stream()
                                        .collect(toImmutableMap(PrefixDeclaration::getPrefix,
                                                                PrefixDeclaration::getPrefixName,
                                                                (leftPrefixDecl, rightPrefixDecl) -> rightPrefixDecl));
    }

    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
        // Prefixed name
        var entityIri = entity.getIRI();
        var entityIriPrefix = entityIri.getNamespace();
        var prefixName = builtInPrefixDeclarationsByPrefix.get(entityIriPrefix);
        if (prefixName != null) {
            var prefixedName = prefixName + entityIri.getFragment();
            fieldWriter.addFieldForDictionaryLanguage(document, PrefixedNameDictionaryLanguage.get(), prefixedName);
        }
    }
}
