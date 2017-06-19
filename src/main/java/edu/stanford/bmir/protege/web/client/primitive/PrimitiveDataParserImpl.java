package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLLiteralData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/01/2013
 */
public class PrimitiveDataParserImpl implements PrimitiveDataParser {


    private EntityDataLookupHandler entityDataLookupHandler;

    @Inject
    public PrimitiveDataParserImpl(EntityDataLookupHandler entityDataLookupHandler) {
        this.entityDataLookupHandler = entityDataLookupHandler;
    }

    /**
     * Parses the specified content and optional language tag into an {@link OWLLiteralData} object.
     * @param trimmedContent The trimmed content.  Not {@code null}.
     * @param language The language as an optional.  Not {@code null}.
     * @return The specified content and optional language parsed into an {@link OWLLiteralData} object.  Not {@code
     *         null}.
     */
    private OWLLiteralData parseLiteralData(String trimmedContent, java.util.Optional<String> language) {
        OWLLiteral literal = DataFactory.parseLiteral(trimmedContent, language);
        return new OWLLiteralData(literal);
    }

    @Override
    public void parsePrimitiveData(String text, java.util.Optional<String> language, Set<PrimitiveType> allowedTypes, PrimitiveDataParserCallback callback) {
        final String trimmedContent = checkNotNull(text).trim();
        parsePrimitiveDataFromTrimmedContent(trimmedContent, checkNotNull(language), checkNotNull(allowedTypes), checkNotNull(callback));
    }

    private void parsePrimitiveDataFromTrimmedContent(final String trimmedContent, final java.util.Optional<String> lang, Set<PrimitiveType> allowedTypes, final PrimitiveDataParserCallback callback) {
        if (trimmedContent.isEmpty()) {
            callback.onSuccess(java.util.Optional.empty());
            return;
        }

        if (lang.isPresent()) {
            if (allowedTypes.contains(PrimitiveType.LITERAL)) {
                OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
                callback.onSuccess(java.util.Optional.of(literalData));
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

    private void parseEntityDataIRIOrLiteral(final String trimmedContent, final java.util.Optional<String> lang, final Set<EntityType<?>> allowedEntityTypes, final Set<PrimitiveType> allowedTypes, final PrimitiveDataParserCallback callback) {
        entityDataLookupHandler.lookupEntity(trimmedContent, allowedEntityTypes, new AsyncCallback<java.util.Optional<OWLEntityData>>() {
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

    private void handleEntityDataParsingResult(Optional<OWLEntityData> result, PrimitiveDataParserCallback callback, String trimmedContent, java.util.Optional<String> lang, Set<PrimitiveType> allowedTypes) {
        if (result.isPresent()) {
            callback.onSuccess(java.util.Optional.of(result.get()));
        }
        else if (allowedTypes.contains(PrimitiveType.IRI) && isAbsoluteIRI(trimmedContent)) {
            IRIData iriData = new IRIData(IRI.create(trimmedContent));
            callback.onSuccess(java.util.Optional.of(iriData));
        }
        else if (allowedTypes.contains(PrimitiveType.LITERAL)) {
            OWLLiteralData literalData = parseLiteralData(trimmedContent, lang);
            callback.onSuccess(java.util.Optional.of(literalData));
        }
        else {
            callback.parsingFailure();
        }
    }

    public boolean isAbsoluteIRI(String url) {
        return (url.startsWith("http://")
                || url.startsWith("https://")
                || url.startsWith("ftp://"))
                && !url.contains(" ")
                && !url.contains("\t")
                && !url.contains("\r")
                && !url.contains("\n");
    }












}
