package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Aug 2018
 */
public class DictionaryLanguageIO {

    private static final String DELIMITER = "|";

    private static final String IRI_LANG_SEPARATOR = "@";

    public String write(List<DictionaryLanguageData> list) {
        return list.stream()
                   .map(l -> Optional.ofNullable(l.getAnnotationPropertyIri()).map(Object::toString).orElse("") + IRI_LANG_SEPARATOR + l.getLanguageTag())
                   .collect(Collectors.joining(DELIMITER));
    }

    public ImmutableList<DictionaryLanguageData> parse(@Nullable String in) {
        if(in == null) {
            return ImmutableList.of();
        }
        return Splitter.on(DELIMITER)
                       .splitToList(in).stream()
                       .map(dls -> Splitter.on("@").splitToList(dls))
                       .map(pair -> {
                           if(pair.size() != 2) {
                               return null;
                           }
                           String iri = pair.get(0);
                           String lang = pair.get(1);
                           if (iri.isEmpty()) {
                               return DictionaryLanguage.localName();
                           }
                           else {
                               return DictionaryLanguage.create(IRI.create(iri), lang);
                           }
                       })
                       .filter(Objects::nonNull)
                       .map(dl -> DictionaryLanguageData.get(dl.getAnnotationPropertyIri(), dl.getLang()))
                       .collect(toImmutableList());
    }
}
