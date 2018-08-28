package edu.stanford.bmir.protege.web.server.crud.uuid;

import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.*;
import edu.stanford.bmir.protege.web.server.util.IdUtil;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;

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

    private EntityCrudKitPrefixSettings prefixSettings;

    private UUIDSuffixSettings suffixSettings;

    public UUIDEntityCrudKitHandler() {
        this(new EntityCrudKitPrefixSettings(), new UUIDSuffixSettings());
    }

    public UUIDEntityCrudKitHandler(EntityCrudKitPrefixSettings prefixSettings, UUIDSuffixSettings uuidSuffixKitSettings) {
        this.prefixSettings = prefixSettings;
        this.suffixSettings = uuidSuffixKitSettings;
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
        OWLDataFactory dataFactory = context.getDataFactory();
        final OWLOntology targetOntology = context.getTargetOntology();
        String suppliedName = shortForm.getShortForm();
        Optional<IRI> parsedIRI = new IRIParser().parseIRI(suppliedName);
        final IRI entityIRI;
        final OWLLiteral labellingLiteral;
        if(parsedIRI.isPresent()) {
            entityIRI = parsedIRI.get();
            labellingLiteral = getLabellingLiteral(entityIRI.toString(), langTag, context);
        }
        else {
            entityIRI = getIRI(prefixSettings.getIRIPrefix(), suppliedName, targetOntology, context.getPrefixedNameExpander());
            labellingLiteral = getLabellingLiteral(suppliedName, langTag, context);
        }
        final E entity =  dataFactory.getOWLEntity(entityType, entityIRI);
        builder.addAxiom(targetOntology, dataFactory.getOWLDeclarationAxiom(entity));
        DictionaryLanguage language = context.getDictionaryLanguage();
        IRI annotationPropertyIri = language.getAnnotationPropertyIri();
        if (annotationPropertyIri != null) {
            OWLAnnotationAssertionAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLAnnotationProperty(annotationPropertyIri), entity.getIRI(), labellingLiteral);
            builder.addAxiom(targetOntology, ax);
        }
        return entity;
    }

    private static IRI getIRI(String prefix, String suppliedName, OWLOntology ontology, PrefixedNameExpander prefixedNameExpander) {
        Optional<IRI> expandedPrefixName = prefixedNameExpander.getExpandedPrefixName(suppliedName);
        return expandedPrefixName.orElseGet(() -> createIRI(prefix, ontology));
    }


    private static IRI createIRI(String base, OWLOntology ontology) {
        while (true) {
            String base62Fragment = IdUtil.getBase62UUID();
            StringBuilder sb = new StringBuilder();
            sb.append(base);
            sb.append(START_CHAR);
            sb.append(base62Fragment);
            IRI iri = IRI.create(sb.toString());
            if(!ontology.containsEntityInSignature(iri)) {
                return iri;
            }
        }
    }


    private static OWLLiteral getLabellingLiteral(String suppliedName, Optional<String> langTag, EntityCrudContext context) {
        OWLDataFactory dataFactory = context.getDataFactory();
        DictionaryLanguage dictionaryLanguage = context.getDictionaryLanguage();
        return dataFactory.getOWLLiteral(suppliedName, langTag.orElse(dictionaryLanguage.getLang()));
    }

}
