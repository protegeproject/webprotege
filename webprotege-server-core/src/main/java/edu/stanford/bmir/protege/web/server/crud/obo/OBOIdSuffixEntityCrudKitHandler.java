package edu.stanford.bmir.protege.web.server.crud.obo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudContext;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class OBOIdSuffixEntityCrudKitHandler implements EntityCrudKitHandler<OBOIdSuffixSettings, OBOIdSession> {


    private static final IRI CREATED_BY = IRI.create("http://www.geneontology.org/formats/oboInOwl#created_by");

    private static final IRI CREATION_DATE = IRI.create("http://www.geneontology.org/formats/oboInOwl#creation_date");

    private static final DateTimeFormatter ISO_OFFSET_DATE_TIME = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private long currentId = 0;

    private EntityCrudKitPrefixSettings prefixSettings;

    private OBOIdSuffixSettings suffixSettings;

    private final Map<UserId, Long> userId2CurrentIdMap = Maps.newHashMap();

    private final Map<UserId, UserIdRange> userId2RangeEndMap;

    public OBOIdSuffixEntityCrudKitHandler(EntityCrudKitPrefixSettings prefixSettings, OBOIdSuffixSettings suffixSettings) {
        this.prefixSettings = prefixSettings;
        this.suffixSettings = suffixSettings;

        ImmutableMap.Builder<UserId, UserIdRange> builder = ImmutableMap.builder();
        for(UserIdRange range : suffixSettings.getUserIdRanges()) {
            userId2CurrentIdMap.put(range.getUserId(), range.getStart());
            builder.put(range.getUserId(), range);
        }
        userId2RangeEndMap = builder.build();
    }

    @Override
    public EntityCrudKitId getKitId() {
        return OBOIdSuffixKit.getId();
    }


    @Override
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        return prefixSettings;
    }

    @Override
    public OBOIdSuffixSettings getSuffixSettings() {
        return suffixSettings;
    }

    @Override
    public OBOIdSession createChangeSetSession() {
        return new OBOIdSession();
    }

    @Override
    public EntityCrudKitSettings<OBOIdSuffixSettings> getSettings() {
        return new EntityCrudKitSettings<>(prefixSettings, suffixSettings);
    }

    @Override
    public <E extends OWLEntity> E create(@Nonnull OBOIdSession session, @Nonnull EntityType<E> entityType, @Nonnull EntityShortForm shortForm, @Nonnull Optional<String> langTag, @Nonnull EntityCrudContext context, @Nonnull OntologyChangeList.Builder<E> builder) {
        OWLDataFactory dataFactory = context.getDataFactory();
        final OWLOntology targetOntology = context.getTargetOntology();
        final IRI iri = getNextIRI(session, targetOntology, context.getUserId());
        final E entity = dataFactory.getOWLEntity(entityType, iri);
        builder.addAxiom(targetOntology, dataFactory.getOWLDeclarationAxiom(entity));
        DictionaryLanguage language = context.getDictionaryLanguage();
        IRI annotationPropertyIri = language.getAnnotationPropertyIri();
        if (annotationPropertyIri != null) {
            final OWLLiteral labellingLiteral = getLabellingLiteral(shortForm, langTag, context);
            OWLAnnotationAssertionAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getOWLAnnotationProperty(annotationPropertyIri), entity.getIRI(), labellingLiteral);
            builder.addAxiom(targetOntology, ax);
        }
        OWLAnnotationAssertionAxiom createdByAx = dataFactory.getOWLAnnotationAssertionAxiom(
                dataFactory.getOWLAnnotationProperty(CREATED_BY),
                entity.getIRI(),
                dataFactory.getOWLLiteral(context.getUserId().getUserName())
        );
        builder.addAxiom(targetOntology, createdByAx);
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        String formattedNow = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OWLAnnotationAssertionAxiom createdAtAx = dataFactory.getOWLAnnotationAssertionAxiom(
                dataFactory.getOWLAnnotationProperty(CREATION_DATE),
                entity.getIRI(),
                dataFactory.getOWLLiteral(formattedNow)
        );
        builder.addAxiom(targetOntology, createdAtAx);
        return entity;
    }



    private synchronized IRI getNextIRI(OBOIdSession session, OWLOntology rootOntology, UserId userId) {
        StringBuilder formatStringBuilder = new StringBuilder();
        for (int i = 0; i < suffixSettings.getTotalDigits(); i++) {
            formatStringBuilder.append("0");
        }
        NumberFormat numberFormat = new DecimalFormat(formatStringBuilder.toString());
        long currentId = getCurrentId(userId);
        while (true) {
            currentId++;
            if(!session.isSessionId(currentId)) {
                String shortName = numberFormat.format(currentId);
                IRI iri = IRI.create(prefixSettings.getIRIPrefix() + shortName);
                if (!rootOntology.containsEntityInSignature(iri, Imports.INCLUDED)) {
                    session.addSessionId(currentId);
                    setCurrentId(userId, currentId);
                    return iri;
                }
            }
        }
    }

    private long getCurrentId(UserId userId) {
        Long currentIdForUser = userId2CurrentIdMap.get(userId);
        if(currentIdForUser != null) {
            return currentIdForUser;
        }
        else {
            return currentId;
        }
    }

    private void setCurrentId(UserId userId, long currentId) {
        if (userId2CurrentIdMap.containsKey(userId)) {
            UserIdRange userIdRange = userId2RangeEndMap.get(userId);
            long limitForUser = userIdRange.getEnd();
            if(currentId > limitForUser) {
                throw new CannotGenerateFreshEntityIdForUserException(userIdRange);
            }
            userId2CurrentIdMap.put(userId, currentId);
        }
        else {
            this.currentId = currentId;
        }
    }

    private static OWLLiteral getLabellingLiteral(EntityShortForm shortForm,
                                                  Optional<String> langTag,
                                                  EntityCrudContext context) {
        OWLDataFactory dataFactory = context.getDataFactory();
        DictionaryLanguage dictionaryLanguage = context.getDictionaryLanguage();
        return dataFactory.getOWLLiteral(shortForm.getShortForm(), langTag.orElse(dictionaryLanguage.getLang()));
    }
}
