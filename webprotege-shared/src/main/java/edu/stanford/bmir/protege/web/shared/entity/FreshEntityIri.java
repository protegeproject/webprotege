package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.util.UUIDUtil;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-03
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FreshEntityIri {

    private static final String QUERY_SEPARATOR = "?";

    private static final String QUERY_PARAM_SEPARATOR = "&";

    private static final String FRAGMENT_IDENTIFIER = "#";

    private static final String PARAM_SEPARATOR = "&";

    public static final String AMPERSAND_ESCAPE = "%26";

    public static final String HASH_ESCAPE = "%23";

    public static final String UUID_PARAM_NAME = "uuid";

    public static final String LANG_TAG_PARAM_NAME = "langTag";

    public static final String PARENT_IRI_PARAM_NAME = "parentIri";

    public static FreshEntityIri get(@Nonnull EntityType<?> entityType,
                                     @Nonnull String suppliedName,
                                     @Nonnull String langTag,
                                     @Nonnull String uuid,
                                     @Nonnull ImmutableSet<IRI> parentIris) {
        if(!uuid.isEmpty() && !UUIDUtil.isWellFormed(uuid)) {
            throw new RuntimeException("Malformed UUID: " + uuid);
        }
        return new AutoValue_FreshEntityIri(entityType, suppliedName, langTag, uuid, parentIris);
    }

    public IRI getIri() {
        String schemeAndTypeName = "entity:" + getEntityType().getName();
        String langTagParam = LANG_TAG_PARAM_NAME + "=" + getLangTag();
        String uuidParam = UUID_PARAM_NAME + "=" + getUuid();
        String parentIriParams = getParentIris().stream()
                                                .map(IRI::toString)
                                                .map(iri -> iri.replace("&", AMPERSAND_ESCAPE))
                                                .map(iri -> iri.replace("#", HASH_ESCAPE))
                                                .map(param -> PARAM_SEPARATOR + PARENT_IRI_PARAM_NAME + "=" + param)
                                                .collect(joining(""));
        String queryParams = QUERY_SEPARATOR + langTagParam + QUERY_PARAM_SEPARATOR + uuidParam + parentIriParams;
        String fragment = FRAGMENT_IDENTIFIER + getSuppliedName();
        String iri = schemeAndTypeName + queryParams + fragment;
        return IRI.create(iri);
    }

    @Nonnull
    public abstract EntityType<?> getEntityType();

    @Nonnull
    public abstract String getSuppliedName();

    @Nonnull
    public abstract String getLangTag();

    @Nonnull
    public abstract String getUuid();

    @Nonnull
    public abstract ImmutableSet<IRI> getParentIris();

    @Nonnull
    public static FreshEntityIri parse(@Nonnull String iriString) {
        int colonIndex = iriString.indexOf(":");
        if(colonIndex == -1) {
            throw new RuntimeException("Missing colon");
        }
        int querySeparatorIndex = iriString.indexOf(QUERY_SEPARATOR);
        if(querySeparatorIndex == -1) {
            throw new RuntimeException("Missing query separator (?)");
        }
        int fragmentIdentifierIndex = iriString.indexOf(FRAGMENT_IDENTIFIER);
        if(fragmentIdentifierIndex == -1) {
            throw new RuntimeException("Missing fragment identifier (#)");
        }
        if(!(colonIndex < querySeparatorIndex) && !(querySeparatorIndex < fragmentIdentifierIndex)) {
            throw new RuntimeException("Malformed fresh entity IRI");
        }
        final String entityTypeToken = iriString.substring(colonIndex + 1,
                                                querySeparatorIndex);
        final EntityType<?> entityType = EntityType.values()
                  .stream()
                  .filter(e -> e.getName().equals(entityTypeToken))
                  .findFirst()
                  .orElseThrow(() -> new RuntimeException("Malformed entity type"));

        final String queryToken = iriString.substring(querySeparatorIndex + 1, fragmentIdentifierIndex);

        String uuid = "";
        String langTag = "";
        ImmutableSet.Builder<IRI> parentIris = ImmutableSet.builder();

        for(String keyValue : Splitter.on(PARAM_SEPARATOR).trimResults().split(queryToken)) {
            List<String> keyValueList = Splitter.on("=").splitToList(keyValue);
            String key = keyValueList.get(0);
            String value = keyValueList.get(1);
            if(key.equals(UUID_PARAM_NAME) && uuid.isEmpty()) {
                uuid = value;
            }
            else if(key.equals(LANG_TAG_PARAM_NAME) && langTag.isEmpty()) {
                langTag = value;
            }
            else if(key.equals(PARENT_IRI_PARAM_NAME)) {
                parentIris.add(IRI.create(value.replace(AMPERSAND_ESCAPE, "&").replace(HASH_ESCAPE, "#")));
            }
        }

        String suppliedName = iriString.substring(fragmentIdentifierIndex + 1);
        return FreshEntityIri.get(entityType, suppliedName, langTag, uuid, parentIris.build());
    }
}
