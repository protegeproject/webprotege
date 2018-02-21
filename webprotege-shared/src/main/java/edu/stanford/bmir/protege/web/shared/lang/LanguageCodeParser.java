package edu.stanford.bmir.protege.web.shared.lang;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class LanguageCodeParser {

    private static final RegExp pattern = RegExp.compile("\"(..)\",\"([^\"]+)\"");


    public List<LanguageCode> parse(String codes) {
        String [] lines = codes.split("\n");
        List<LanguageCode> result = new ArrayList<>();
        for(String line : lines) {
            String trimmedLine = line.trim();
            if (pattern.test(trimmedLine)) {
                MatchResult matchResult = pattern.exec(trimmedLine);
                String lang = matchResult.getGroup(1);
                String name = matchResult.getGroup(2);
                result.add(new LanguageCode(lang, name));
            }
        }
        return result;
    }
}
