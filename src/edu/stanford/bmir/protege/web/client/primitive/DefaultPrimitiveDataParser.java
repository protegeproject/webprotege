package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupServiceAsync;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupServiceResult;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequest;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequestType;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/01/2013
 */
public class DefaultPrimitiveDataParser implements PrimitiveDataParser, OWLLiteralParser {


    private EntityLookupServiceAsync lookupServiceAsync;

    public DefaultPrimitiveDataParser(EntityLookupServiceAsync lookupServiceAsync) {
        this.lookupServiceAsync = lookupServiceAsync;
    }

    @Override
    public void parsePrimitiveData(String content, Optional<String> lang, final PrimitiveDataParsingContext context, final PrimitiveDataParserCallback callback) {
        final String trimmedContent = checkNotNull(content).trim();
        parsePrimitiveDataFromTrimmedContent(trimmedContent, checkNotNull(lang), checkNotNull(context), checkNotNull(callback));
    }


    @Override
    public OWLLiteral parseLiteral(String text, Optional<String> language) {
        return DataFactory.parseLiteral(checkNotNull(text), checkNotNull(language));
    }



    private void handleSuccess(OWLPrimitiveData parsedData, PrimitiveDataParserCallback parserCallback) {
        parserCallback.onSuccess(Optional.of(parsedData));
    }

    private void parsePrimitiveDataFromTrimmedContent(final String trimmedContent, final Optional<String> lang, final PrimitiveDataParsingContext context, final PrimitiveDataParserCallback callback) {
        if (trimmedContent.isEmpty()) {
            callback.onSuccess(Optional.<OWLPrimitiveData>absent());
            return;
        }

        if (lang.isPresent()) {
            if (context.isLiteralAllowed()) {
                OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
                handleSuccess(literalData, callback);
            }
            else {
                // TODO: Literal not expected
                callback.parsingFailure();
            }
            return;
        }

        if (context.isRegisteredFreshEntity(trimmedContent)) {
            Optional<OWLEntity> registeredFreshEntity = context.getRegisteredFreshEntity(trimmedContent);
            OWLPrimitiveData parsedData = DataFactory.getOWLEntityData(registeredFreshEntity.get(), trimmedContent);
            handleSuccess(parsedData, callback);
            return;
        }

        final Set<EntityType<?>> allowedEntityTypes = context.getAllowedEntityTypes();
        final ProjectId projectId = context.getProjectId();
        final EntityLookupRequest entityLookupRequest = new EntityLookupRequest(trimmedContent, EntityLookupRequestType.EXACT_MATCH_IGNORE_CASE, 5, allowedEntityTypes);
        lookupServiceAsync.lookupEntities(projectId, entityLookupRequest, new AsyncCallback<List<EntityLookupServiceResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO: Handle lookup failure
                callback.parsingFailure();
            }

            @Override
            public void onSuccess(List<EntityLookupServiceResult> result) {
                OWLEntityData entityData = getMatchingEntity(result, trimmedContent, context);
                if (entityData != null) {
                    handleSuccess(entityData, callback);
                    return;
                }
                if (context.isIRIAllowed() && isAbsoluteIRI(trimmedContent)) {
                    IRIData iriData = new IRIData(IRI.create(trimmedContent));
                    handleSuccess(iriData, callback);
                    return;
                }

                if (context.isLiteralAllowed()) {
                    OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
                    handleSuccess(literalData, callback);
                    return;
                }

                // TODO: Not matched allowed type
                callback.parsingFailure();
            }
        });
    }

    public native boolean isAbsoluteIRI(String url) /*-{
        var pattern = /(\w+):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        return pattern.test(url);
    }-*/;


    /**
     * Given a lookup result, gets the entity data which matches the current text in the editor.
     * @param result
     * @param text
     * @return
     */
    private OWLEntityData getMatchingEntity(List<EntityLookupServiceResult> result, String text, PrimitiveDataParsingContext context) {
        if (result.isEmpty()) {
            return null;
        }
        if (!context.isEntitiesAllowed()) {
            return null;
        }
        EntityLookupServiceResult lookupResult = result.get(0);
        final OWLEntityData lookedUpEntityData = lookupResult.getOWLEntityData();
        EntityType<?> entityType = lookedUpEntityData.getEntity().getEntityType();
        if (lookedUpEntityData.getBrowserText().equalsIgnoreCase(text) && context.isTypeAllowed(PrimitiveType.get(entityType))) {
            return lookedUpEntityData;
        }
        else {
            return null;
        }
    }


    /**
     * Parses the specified content and optional language tag into an {@link OWLLiteralData} object.
     * @param trimmedContent The trimmed content.  Not {@code null}.
     * @param language The language as an optional.  Not {@code null}.
     * @return The specified content and optional language parsed into an {@link OWLLiteralData} object.  Not {@code
     *         null}.
     */
    private OWLLiteralData parseLiteralData(String trimmedContent, Optional<String> language) {
        OWLLiteral literal = parseLiteral(trimmedContent, language);
        return new OWLLiteralData(literal);
    }





}
