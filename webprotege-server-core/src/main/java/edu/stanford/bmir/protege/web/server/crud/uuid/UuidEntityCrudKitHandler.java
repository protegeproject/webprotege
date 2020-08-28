package edu.stanford.bmir.protege.web.server.crud.uuid;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.*;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.shared.crud.*;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidFormat;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;
import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageVisitor;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class UuidEntityCrudKitHandler implements EntityCrudKitHandler<UuidSuffixSettings, ChangeSetEntityCrudSession> {

    private final EntityCrudKitPrefixSettings prefixSettings;

    private final UuidSuffixSettings suffixSettings;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInSignature;

    @Nonnull
    private final EntityIriPrefixResolver entityIriPrefixResolver;

    @AutoFactory
    @Inject
    public UuidEntityCrudKitHandler(@Nonnull EntityCrudKitPrefixSettings prefixSettings,
                                    @Nonnull UuidSuffixSettings uuidSuffixKitSettings,
                                    @Provided OWLDataFactory dataFactory,
                                    @Provided @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInSignature,
                                    @Provided @Nonnull EntityIriPrefixResolver entityIriPrefixResolver) {
        this.prefixSettings = checkNotNull(prefixSettings);
        this.suffixSettings = checkNotNull(uuidSuffixKitSettings);
        this.dataFactory = checkNotNull(dataFactory);
        this.entitiesInSignature = checkNotNull(entitiesInSignature);
        this.entityIriPrefixResolver = checkNotNull(entityIriPrefixResolver);
    }

    @Override
    public EntityCrudKitId getKitId() {
        return suffixSettings.getKitId();
    }

    @Override
    public ChangeSetEntityCrudSession createChangeSetSession() {
        return EmptyChangeSetEntityCrudSession.get();
    }

    @Override
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return prefixSettings;
    }

    @Override
    public UuidSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    @Override
    public EntityCrudKitSettings<UuidSuffixSettings> getSettings() {
        return EntityCrudKitSettings.get(prefixSettings, suffixSettings);
    }

    @Override
    public <E extends OWLEntity> E create(@Nonnull ChangeSetEntityCrudSession session,
                                          @Nonnull EntityType<E> entityType,
                                          @Nonnull final EntityShortForm shortForm,
                                          @Nonnull Optional<String> langTag,
                                          @Nonnull ImmutableList<OWLEntity> parents,
                                          @Nonnull final EntityCrudContext context,
                                          @Nonnull final OntologyChangeList.Builder<E> builder) {
        var targetOntology = context.getTargetOntologyId();
        var suppliedName = shortForm.getShortForm();
        var parsedIRI = new IRIParser().parseIRI(suppliedName);
        final IRI entityIRI;
        final OWLLiteral labellingLiteral;
        var dictionaryLanguage = context.getDictionaryLanguage();
        if(parsedIRI.isPresent()) {
            entityIRI = parsedIRI.get();
            labellingLiteral = getLabellingLiteral(entityIRI.toString(), langTag, dictionaryLanguage);
        }
        else {
            var prefixedNameExpander = context.getPrefixedNameExpander();
            var iriPrefix = entityIriPrefixResolver.getIriPrefix(prefixSettings, parents);
            entityIRI = getIRI(iriPrefix, suppliedName, prefixedNameExpander);
            labellingLiteral = getLabellingLiteral(suppliedName, langTag, dictionaryLanguage);
        }
        var entity = dataFactory.getOWLEntity(entityType, entityIRI);
        builder.add(AddAxiomChange.of(targetOntology, dataFactory.getOWLDeclarationAxiom(entity)));

        if(!suppliedName.isBlank()) {
            dictionaryLanguage.accept(new DictionaryLanguageVisitor<Object>() {
                @Override
                public Object visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
                    var annotationPropertyIri = language.getAnnotationPropertyIri();
                    var ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLAnnotationProperty(annotationPropertyIri), entity.getIRI(), labellingLiteral);
                    builder.add(AddAxiomChange.of(targetOntology, ax));
                    return null;
                }
            });

        }
        return entity;
    }

    private IRI getIRI(String prefix, String suppliedName, PrefixedNameExpander prefixedNameExpander) {
        var expandedPrefixName = prefixedNameExpander.getExpandedPrefixName(suppliedName);
        return expandedPrefixName.orElseGet(() -> createIRI(prefix));
    }


    private IRI createIRI(String base) {
        while (true) {
            var suffix = getUuid();
            var iri = IRI.create(base + suffixSettings.getIdPrefix() + suffix);
            var inSig = entitiesInSignature.getEntitiesInSignature(iri).limit(1).count() == 1;
            if(!inSig) {
                return iri;
            }
        }
    }

    private String getUuid() {
        if(suffixSettings.getUuidFormat() == UuidFormat.BASE62) {
            return IdUtil.getBase62UUID();
        }
        else {
            return IdUtil.getUUID();
        }
    }


    private OWLLiteral getLabellingLiteral(String suppliedName, Optional<String> langTag, DictionaryLanguage dictionaryLanguage) {
        return dataFactory.getOWLLiteral(suppliedName, langTag.orElse(dictionaryLanguage.getLang()));
    }

}
