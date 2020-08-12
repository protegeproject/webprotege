package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.*;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Aug 2018
 */
public class DictionaryLanguageIO {

    private static final String DELIMITER = "!!";

    private static final String LANG_IRI_SEPARATOR = "@";

    private static final String PATH_DELIMETER = ">>>";

    public static final String LOCAL_NAME = "[LocalName]";

    public static final String OBO_ID = "[OboId]";

    public static final String ANNOTATION_ASSERTION = "[AnnotationAssertion]";

    public static final String ANNOTATION_ASSERTION_PATH = "[AnnotationAssertionPath]";

    public static final String PREFIXED_NAME = "[PrefixedName]";

    private final DictionaryLanguageVisitor<String> visitor = new DictionaryLanguageVisitor<String>() {
        @Override
        public String visit(@Nonnull LocalNameDictionaryLanguage language) {
            return LOCAL_NAME;
        }

        @Override
        public String visit(@Nonnull OboIdDictionaryLanguage language) {
            return OBO_ID;
        }

        @Override
        public String visit(@Nonnull AnnotationAssertionDictionaryLanguage language) {
            return ANNOTATION_ASSERTION + language.getLang() + LANG_IRI_SEPARATOR + language.getAnnotationPropertyIri();
        }

        @Override
        public String visit(@Nonnull AnnotationAssertionPathDictionaryLanguage language) {
            return ANNOTATION_ASSERTION_PATH + language.getLang() + "@" + language.getAnnotationPropertyPath()
                    .stream()
                    .map(IRI::toString)
                    .collect(Collectors.joining(PATH_DELIMETER));
        }

        @Override
        public String visit(@Nonnull PrefixedNameDictionaryLanguage language) {
            return PREFIXED_NAME;
        }
    };

    public String write(List<DictionaryLanguage> list) {
        return list.stream().map(language -> language.accept(visitor)).collect(Collectors.joining(DELIMITER));

    }

    public ImmutableList<DictionaryLanguage> parse(@Nullable String in) {
        if (in == null) {
            return ImmutableList.of();
        }
        if(in.isEmpty()) {
            return ImmutableList.of();
        }
        String[] langStrings = in.split(DELIMITER);
        return Arrays.stream(langStrings)
                     .map(this::parseLangString)
                     .collect(toImmutableList());

    }

    private DictionaryLanguage parseLangString(String langString) {
        if (langString.startsWith(LOCAL_NAME)) {
            return DictionaryLanguage.localName();
        }
        else if (langString.startsWith(OBO_ID)) {
            return DictionaryLanguage.oboId();
        }
        else if (langString.startsWith(PREFIXED_NAME)) {
            return DictionaryLanguage.prefixedName();
        }
        else if (langString.startsWith(ANNOTATION_ASSERTION)) {
            return parseAnnotationAssertion(langString.substring(ANNOTATION_ASSERTION.length()));
        }
        else if (langString.startsWith(ANNOTATION_ASSERTION_PATH)) {
            return parseAnnotationAssertionPath(langString.substring(ANNOTATION_ASSERTION_PATH.length()));
        }
        else {
            return DictionaryLanguage.localName();
        }
    }

    @Nonnull
    private DictionaryLanguage parseAnnotationAssertion(String langString) {
        int index = langString.indexOf(LANG_IRI_SEPARATOR);
        // Lang then IRI
        String lang = "";
        IRI iri;
        if (index == -1) {
            iri = IRI.create(langString);
        }
        else {
            iri = IRI.create(langString.substring(index + 1));
            lang = langString.substring(0, index);
        }
        if(iri.toString().isEmpty()) {
            return LocalNameDictionaryLanguage.get();
        }
        return AnnotationAssertionDictionaryLanguage.get(iri, lang);
    }

    @Nonnull
    private DictionaryLanguage parseAnnotationAssertionPath(String langString) {
        int index = langString.indexOf(LANG_IRI_SEPARATOR);
        // Lang then IRI
        String lang = "";
        String iriPathPortion = langString.substring(index + 1);
        if (index == -1) {
            iriPathPortion = langString;

        }
        else {
            iriPathPortion = langString.substring(index + 1);
            lang = langString.substring(0, index);
        }
        ImmutableList<IRI> iris = Splitter.on(PATH_DELIMETER)
                                          .splitToList(iriPathPortion)
                                          .stream()
                                          .map(IRI::create)
                                          .collect(toImmutableList());
        return AnnotationAssertionPathDictionaryLanguage.get(iris, lang);
    }

}
