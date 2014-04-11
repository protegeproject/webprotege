package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/01/2013
 */
public class DefaultPrimitiveDataParser implements PrimitiveDataParser {


    private EntityDataLookupHandler entityDataLookupHandler;

    @Inject
    public DefaultPrimitiveDataParser(EntityDataLookupHandler entityDataLookupHandler) {
        this.entityDataLookupHandler = entityDataLookupHandler;
    }

    /**
     * Parses the specified content and optional language tag into an {@link OWLLiteralData} object.
     * @param trimmedContent The trimmed content.  Not {@code null}.
     * @param language The language as an optional.  Not {@code null}.
     * @return The specified content and optional language parsed into an {@link OWLLiteralData} object.  Not {@code
     *         null}.
     */
    private OWLLiteralData parseLiteralData(String trimmedContent, Optional<String> language) {
        OWLLiteral literal = DataFactory.parseLiteral(trimmedContent, language);
        return new OWLLiteralData(literal);
    }

    @Override
    public void parsePrimitiveData(String text, Optional<String> language, Set<PrimitiveType> allowedTypes, PrimitiveDataParserCallback callback) {
        final String trimmedContent = checkNotNull(text).trim();
        parsePrimitiveDataFromTrimmedContent(trimmedContent, checkNotNull(language), checkNotNull(allowedTypes), checkNotNull(callback));
    }

    private void parsePrimitiveDataFromTrimmedContent(final String trimmedContent, final Optional<String> lang, Set<PrimitiveType> allowedTypes, final PrimitiveDataParserCallback callback) {
        if (trimmedContent.isEmpty()) {
            callback.onSuccess(Optional.<OWLPrimitiveData>absent());
            return;
        }

        if (lang.isPresent()) {
            if (allowedTypes.contains(PrimitiveType.LITERAL)) {
                OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
                callback.onSuccess(Optional.<OWLPrimitiveData>of(literalData));
            }
            else {
                // TODO: Literal not expected
                callback.parsingFailure();
            }
            return;
        }
        Set<EntityType<?>> allowedEntityTypes = Sets.newHashSet();
        for(PrimitiveType primitiveType : allowedTypes) {
            if(primitiveType.isEntityType()) {
                allowedEntityTypes.add(primitiveType.getEntityType());
            }
        }
        parseEntityDataIRIOrLiteral(trimmedContent, lang, allowedEntityTypes, allowedTypes, callback);
    }

    private void parseEntityDataIRIOrLiteral(final String trimmedContent, final Optional<String> lang, final Set<EntityType<?>> allowedEntityTypes, final Set<PrimitiveType> allowedTypes, final PrimitiveDataParserCallback callback) {
        entityDataLookupHandler.lookupEntity(trimmedContent, allowedEntityTypes, new AsyncCallback<Optional<OWLEntityData>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.parsingFailure();
            }

            @Override
            public void onSuccess(Optional<OWLEntityData> result) {
                handleEntityDataParsingResult(result, callback, trimmedContent, lang, allowedTypes);
            }
        });
    }

    private void handleEntityDataParsingResult(Optional<OWLEntityData> result, PrimitiveDataParserCallback callback, String trimmedContent, Optional<String> lang, Set<PrimitiveType> allowedTypes) {
        if (result.isPresent()) {
            callback.onSuccess(Optional.<OWLPrimitiveData>of(result.get()));
        }
        else if (allowedTypes.contains(PrimitiveType.IRI) && isAbsoluteIRI(trimmedContent)) {
            IRIData iriData = new IRIData(IRI.create(trimmedContent));
            callback.onSuccess(Optional.<OWLPrimitiveData>of(iriData));
        }
        else if (allowedTypes.contains(PrimitiveType.LITERAL)) {
            OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
            callback.onSuccess(Optional.<OWLPrimitiveData>of(literalData));
        }
        else {
            callback.parsingFailure();
        }
    }

    public native boolean isAbsoluteIRI(String url) /*-{
        var pattern = /(\w+):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        return pattern.test(url);
    }-*/;












}
