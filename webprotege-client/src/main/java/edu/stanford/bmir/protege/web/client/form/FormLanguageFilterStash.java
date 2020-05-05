package edu.stanford.bmir.protege.web.client.form;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.portlet.HasNodeProperties;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;

import javax.annotation.Nonnull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-05
 */
public class FormLanguageFilterStash {

    public static final String FORMS_LANGUAGE_FILTER = "forms.language-filter";

    @Nonnull
    private final HasNodeProperties hasNodeProperties;

    private static final String langTagSeparator = ",";

    public FormLanguageFilterStash(@Nonnull HasNodeProperties hasNodeProperties) {
        this.hasNodeProperties = checkNotNull(hasNodeProperties);
    }

    public void stashLanguage(@Nonnull Collection<LangTag> langTags) {
        checkNotNull(langTags);
        String serialized = langTags.stream()
                .map(LangTag::getLanguageCode)
                .collect(Collectors.joining(langTagSeparator));
        hasNodeProperties.setNodeProperty(FORMS_LANGUAGE_FILTER, serialized);
    }

    public ImmutableList<LangTag> getStashedLangTags() {
        String serialized = hasNodeProperties.getNodeProperty(FORMS_LANGUAGE_FILTER, "");
        if(serialized.isEmpty()) {
            return ImmutableList.of();
        }
        return Splitter.on(langTagSeparator)
                .trimResults()
                .splitToList(serialized)
                .stream()
                .map(LangTag::get)
                .collect(toImmutableList());
    }

}
