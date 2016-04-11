package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.lang.LanguageCode;
import edu.stanford.bmir.protege.web.shared.lang.LanguageCodeParser;

import javax.inject.Provider;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class LangCodesProvider implements Provider<List<LanguageCode>> {

    @Override
    public List<LanguageCode> get() {
        LanguageCodeParser parser = new LanguageCodeParser();
        return parser.parse(WebProtegeClientBundle.BUNDLE.languageCodes().getText());
    }
}
