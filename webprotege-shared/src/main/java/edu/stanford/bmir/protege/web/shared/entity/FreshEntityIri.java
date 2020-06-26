package edu.stanford.bmir.protege.web.shared.entity;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
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

    public static final String DISCRIMINATOR_PARAM_NAME = "discriminator";

    public static final String LANG_TAG_PARAM_NAME = "langTag";

    public static final String PARENT_IRI_PARAM_NAME = "parentIri";

    public static final String SCHEME = "entity";

    /**
     * Creates a {@link FreshEntityIri}.  This isn't a well formed IRI, necessarily.  It is just used
     * to collect together information for the later creation of an entity.
     * @param suppliedName The supplied (human readable) name.  This may be the empty string.
     * @param langTag The language tag of a supplied name.  This may be empty.  Even if the supplied name
     *                is empty then a non-empty language tag will be accepted.  The language tag is not
     *                validated.
     * @param discriminator This should only really be provided if the supplied name is empty, when it is used
     *             as a discriminator for fresh entities.  The UUID may be the empty string, otherwise it
     *             should conform to a standard UUID pattern.
     * @param parentIris A possibly empty set of parent IRIs.  For classes, parent IRIs are class IRIs.  For individuals,
     *                   parent IRIs are class IRIs. For object properties, parent IRIs are object property IRIs.
     *                   For data properties, parent IRIs are data property IRIs.  For annotation properties, parent
     *                   IRIs are annotation property IRIs.  For datatypes, parent IRIs are datatype IRIs.
     * @return A fresh entity IRI containing the supplied information.
     */
    @Nonnull
    public static FreshEntityIri get(@Nonnull String suppliedName,
                                     @Nonnull String langTag,
                                     @Nonnull String discriminator,
                                     @Nonnull ImmutableSet<IRI> parentIris) {
        return new AutoValue_FreshEntityIri(suppliedName, langTag, discriminator, parentIris);
    }

    public IRI getIri() {
        String schemeAndTypeName = SCHEME + ":";
        String langTagParam = LANG_TAG_PARAM_NAME + "=" + getLangTag();
        String discriminatorParam = DISCRIMINATOR_PARAM_NAME + "=" + getDiscriminator();
        String parentIriParams = getParentIris().stream()
                                                .map(IRI::toString)
                                                .map(iri -> iri.replace("&", AMPERSAND_ESCAPE))
                                                .map(iri -> iri.replace("#", HASH_ESCAPE))
                                                .map(param -> PARAM_SEPARATOR + PARENT_IRI_PARAM_NAME + "=" + param)
                                                .collect(joining(""));
        String queryParams = QUERY_SEPARATOR + langTagParam + QUERY_PARAM_SEPARATOR + discriminatorParam + parentIriParams;
        String fragment = FRAGMENT_IDENTIFIER + getSuppliedName();
        String iri = schemeAndTypeName + queryParams + fragment;
        return IRI.create(iri);
    }

    @Nonnull
    public abstract String getSuppliedName();

    @Nonnull
    public abstract String getLangTag();

    @Nonnull
    public abstract String getDiscriminator();

    @Nonnull
    public abstract ImmutableSet<IRI> getParentIris();

    @Nonnull
    public ImmutableList<OWLEntity> getParentEntities(@Nonnull OWLEntityProvider dataFactory,
                                                      @Nonnull EntityType<?> forEntityType) {
        return getParentIris()
                      .stream()
                      .map(iri -> {
                          if(forEntityType.equals(EntityType.CLASS) || forEntityType.equals(
                                  EntityType.NAMED_INDIVIDUAL)) {
                              return dataFactory.getOWLClass(iri);
                          }
                          else if(forEntityType.equals(EntityType.OBJECT_PROPERTY)) {
                              return dataFactory.getOWLObjectProperty(iri);
                          }
                          else if(forEntityType.equals(EntityType.DATA_PROPERTY)) {
                              return dataFactory.getOWLDataProperty(iri);
                          }
                          else if(forEntityType.equals(EntityType.ANNOTATION_PROPERTY)) {
                              return dataFactory.getOWLAnnotationProperty(iri);
                          }
                          else {
                              return null;
                          }
                      })
                      .filter(Objects::nonNull)
                      .map(e -> (OWLEntity) e)
                      .collect(toImmutableList());
    }

    /**
     * Determines if the specified IRI is a fresh entity IRI.
     * @param iri The IRI
     * @return true if the IRI represents a fresh entity IRI, otherwise false
     */
    public static boolean isFreshEntityIri(@Nonnull IRI iri) {
        checkNotNull(iri);
        return Objects.equals(iri.getScheme(), SCHEME);
    }

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
            throw new RuntimeException("Malformed fresh " + SCHEME + " IRI");
        }
        final String queryToken = iriString.substring(querySeparatorIndex + 1, fragmentIdentifierIndex);

        String discriminator = "";
        String langTag = "";
        ImmutableSet.Builder<IRI> parentIris = ImmutableSet.builder();

        for(String keyValue : Splitter.on(PARAM_SEPARATOR).trimResults().split(queryToken)) {
            List<String> keyValueList = Splitter.on("=").splitToList(keyValue);
            String key = keyValueList.get(0);
            String value = keyValueList.get(1);
            if(key.equals(DISCRIMINATOR_PARAM_NAME) && discriminator.isEmpty()) {
                discriminator = value;
            }
            else if(key.equals(LANG_TAG_PARAM_NAME) && langTag.isEmpty()) {
                langTag = value;
            }
            else if(key.equals(PARENT_IRI_PARAM_NAME)) {
                parentIris.add(IRI.create(value.replace(AMPERSAND_ESCAPE, "&").replace(HASH_ESCAPE, "#")));
            }
        }

        String suppliedName = iriString.substring(fragmentIdentifierIndex + 1);
        return FreshEntityIri.get(suppliedName, langTag, discriminator, parentIris.build());
    }
}
