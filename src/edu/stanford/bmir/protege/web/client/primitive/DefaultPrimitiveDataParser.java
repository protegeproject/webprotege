package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/01/2013
 */
public class DefaultPrimitiveDataParser implements PrimitiveDataParser, OWLLiteralParser {


    private EntityDataLookupHandler entityDataLookupHandler;

    public DefaultPrimitiveDataParser(EntityDataLookupHandler entityDataLookupHandler) {
        this.entityDataLookupHandler = entityDataLookupHandler;
    }


    @Override
    public OWLLiteral parseLiteral(String text, Optional<String> language) {
        return DataFactory.parseLiteral(checkNotNull(text), checkNotNull(language));
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


    @Override
    public void parsePrimitiveData(String content, Optional<String> lang, final PrimitiveDataParsingContext context, final PrimitiveDataParserCallback callback) {
        final String trimmedContent = checkNotNull(content).trim();
        parsePrimitiveDataFromTrimmedContent(trimmedContent, checkNotNull(lang), checkNotNull(context), checkNotNull(callback));
    }





    private void parsePrimitiveDataFromTrimmedContent(final String trimmedContent, final Optional<String> lang, final PrimitiveDataParsingContext context, final PrimitiveDataParserCallback callback) {
        if (trimmedContent.isEmpty()) {
            callback.onSuccess(Optional.<OWLPrimitiveData>absent());
            return;
        }

        if (lang.isPresent()) {
            if (context.isLiteralAllowed()) {
                OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
                callback.onSuccess(Optional.<OWLPrimitiveData>of(literalData));
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
            callback.onSuccess(Optional.<OWLPrimitiveData>of(parsedData));
            return;
        }
        parseEntityDataIRIOrLiteral(trimmedContent, lang, context, callback);
    }

    private void parseEntityDataIRIOrLiteral(final String trimmedContent, final Optional<String> lang, final PrimitiveDataParsingContext context, final PrimitiveDataParserCallback callback) {
        entityDataLookupHandler.lookupEntity(trimmedContent, context, new AsyncCallback<Optional<OWLEntityData>>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.parsingFailure();
            }

            @Override
            public void onSuccess(Optional<OWLEntityData> result) {
                handleEntityDataParsingResult(result, callback, context, trimmedContent, lang);
            }
        });
    }

    private void handleEntityDataParsingResult(Optional<OWLEntityData> result, PrimitiveDataParserCallback callback, PrimitiveDataParsingContext context, String trimmedContent, Optional<String> lang) {
        if (result.isPresent()) {
            callback.onSuccess(Optional.<OWLPrimitiveData>of(result.get()));
        }
        else if (context.isIRIAllowed() && isAbsoluteIRI(trimmedContent)) {
            IRIData iriData = new IRIData(IRI.create(trimmedContent));
            callback.onSuccess(Optional.<OWLPrimitiveData>of(iriData));
        }
        else if (context.isLiteralAllowed()) {
            OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
            callback.onSuccess(Optional.<OWLPrimitiveData>of(literalData));
        }
    }

    public native boolean isAbsoluteIRI(String url) /*-{
        var pattern = /(\w+):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
        return pattern.test(url);
    }-*/;











}
