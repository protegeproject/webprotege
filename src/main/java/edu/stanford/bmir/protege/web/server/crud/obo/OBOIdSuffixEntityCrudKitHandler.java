package edu.stanford.bmir.protege.web.server.crud.obo;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.ImmutableMap;
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
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.Session;
import org.semanticweb.owlapi.model.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class OBOIdSuffixEntityCrudKitHandler implements EntityCrudKitHandler<OBOIdSuffixSettings, OBOIdSession> {


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
        return OBOIdSuffixKit.get().getKitId();
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
        return new EntityCrudKitSettings<OBOIdSuffixSettings>(prefixSettings, suffixSettings);
    }

    @Override
    public <E extends OWLEntity> E create(OBOIdSession session, EntityType<E> entityType, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> builder) {
        OWLDataFactory dataFactory = context.getDataFactory();
        final OWLOntology targetOntology = context.getTargetOntology();
        final IRI iri = getNextIRI(session, targetOntology, context.getUserId());
        final E entity = dataFactory.getOWLEntity(entityType, iri);
        builder.addAxiom(targetOntology, dataFactory.getOWLDeclarationAxiom(entity));
        final OWLLiteral labellingLiteral = getLabellingLiteral(shortForm, context);
        OWLAnnotationAssertionAxiom ax = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getRDFSLabel(), entity.getIRI(), labellingLiteral);
        builder.addAxiom(targetOntology, ax);
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
                if (!rootOntology.containsEntityInSignature(iri, true)) {
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

    @Override
    public <E extends OWLEntity> void update(OBOIdSession session, E entity, EntityShortForm shortForm, EntityCrudContext context, OntologyChangeList.Builder<E> changeListBuilder) {
        final OWLDataFactory df = context.getDataFactory();
        OWLLiteral browserTextLiteral = getLabellingLiteral(shortForm, context);
        OntologyChangeList.Builder<E> builder = new OntologyChangeList.Builder<E>();

        OWLAxiom freshAx = df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), entity.getIRI(), browserTextLiteral);
        final OWLOntology targetOntology = context.getTargetOntology();
        for (OWLOntology ont : targetOntology.getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(entity.getIRI())) {
                if (ax.getProperty().isLabel()) {
                    builder.removeAxiom(ont, ax);
                    builder.addAxiom(ont, freshAx);
                }
            }
        }
        if (builder.isEmpty()) {
            builder.addAxiom(targetOntology, freshAx);
        }
    }

    @Override
    public <E extends OWLEntity> String getShortForm(E entity, EntityCrudContext context) {
        for (OWLOntology ontology : context.getTargetOntology().getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(entity.getIRI())) {
                if (ax.getProperty().isLabel() && ax.getValue() instanceof OWLLiteral) {
                    OWLLiteral literal = (OWLLiteral) ax.getValue();
                    return literal.getLiteral();
                }
            }
        }
        return entity.getIRI().toString();
    }

    private static OWLLiteral getLabellingLiteral(EntityShortForm shortForm, EntityCrudContext context) {
        OWLDataFactory dataFactory = context.getDataFactory();
        return dataFactory.getOWLLiteral(shortForm.getShortForm(), context.getTargetLanguage().or(""));
    }
}
