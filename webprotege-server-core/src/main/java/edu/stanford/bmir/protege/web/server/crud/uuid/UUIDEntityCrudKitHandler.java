package edu.stanford.bmir.protege.web.server.crud.uuid;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.*;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
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
public class UUIDEntityCrudKitHandler implements EntityCrudKitHandler<UUIDSuffixSettings, ChangeSetEntityCrudSession> {

    /**
     * A start char for local names.  Some UUIDs might start with a number.  Unfortunately, NCNames (non-colonised names)
     * in XML cannot start with numbers.  For everything apart from properties this is o.k. but for properties it means
     * that it might not be possible to save an ontology in RDF/XML.  We therefore prefix each local name with a valid
     * NCName start char - "R".  The character "R" was chosen so as not to encode the type into the name.  I initially
     * considered C for classes, P properties etc. however with punning this would get ugly.
     */
    private static final String START_CHAR = "R";

    private final EntityCrudKitPrefixSettings prefixSettings;

    private final UUIDSuffixSettings suffixSettings;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInSignature;

    @AutoFactory
    @Inject
    public UUIDEntityCrudKitHandler(@Nonnull EntityCrudKitPrefixSettings prefixSettings,
                                    @Nonnull UUIDSuffixSettings uuidSuffixKitSettings,
                                    @Provided OWLDataFactory dataFactory,
                                    @Provided @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInSignature) {
        this.prefixSettings = checkNotNull(prefixSettings);
        this.suffixSettings = checkNotNull(uuidSuffixKitSettings);
        this.dataFactory = checkNotNull(dataFactory);
        this.entitiesInSignature = checkNotNull(entitiesInSignature);
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
    public UUIDSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    @Override
    public EntityCrudKitSettings<UUIDSuffixSettings> getSettings() {
        return new EntityCrudKitSettings<>(prefixSettings, suffixSettings);
    }

    @Override
    public <E extends OWLEntity> E create(@Nonnull ChangeSetEntityCrudSession session, @Nonnull EntityType<E> entityType, @Nonnull final EntityShortForm shortForm, @Nonnull Optional<String> langTag, @Nonnull final EntityCrudContext context, @Nonnull final OntologyChangeList.Builder<E> builder) {
        var targetOntology = context.getTargetOntology();
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
            entityIRI = getIRI(prefixSettings.getIRIPrefix(), suppliedName, prefixedNameExpander);
            labellingLiteral = getLabellingLiteral(suppliedName, langTag, dictionaryLanguage);
        }
        var entity = dataFactory.getOWLEntity(entityType, entityIRI);
        builder.addAxiom(targetOntology, dataFactory.getOWLDeclarationAxiom(entity));

        var annotationPropertyIri = dictionaryLanguage.getAnnotationPropertyIri();
        if (annotationPropertyIri != null) {
            var ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLAnnotationProperty(annotationPropertyIri), entity.getIRI(), labellingLiteral);
            builder.addAxiom(targetOntology, ax);
        }
        return entity;
    }

    private IRI getIRI(String prefix, String suppliedName, PrefixedNameExpander prefixedNameExpander) {
        var expandedPrefixName = prefixedNameExpander.getExpandedPrefixName(suppliedName);
        return expandedPrefixName.orElseGet(() -> createIRI(prefix));
    }


    private IRI createIRI(String base) {
        while (true) {
            var base62Fragment = IdUtil.getBase62UUID();
            var iri = IRI.create(base + START_CHAR + base62Fragment);
            var inSig = entitiesInSignature.getEntityInSignature(iri).limit(1).count() == 1;
            if(!inSig) {
                return iri;
            }
        }
    }


    private OWLLiteral getLabellingLiteral(String suppliedName, Optional<String> langTag, DictionaryLanguage dictionaryLanguage) {
        return dataFactory.getOWLLiteral(suppliedName, langTag.orElse(dictionaryLanguage.getLang()));
    }

}
