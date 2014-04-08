package edu.stanford.bmir.protege.web.server.notes.api;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.notes.NoteField;

import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class RegexNoteFieldMatcher implements NoteFieldMatcher<String> {

    private Pattern pattern;

    public RegexNoteFieldMatcher(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(NoteField<String> field, Optional<String> value) {
        if(value.isPresent()) {
            return pattern.matcher(value.get()).matches();
        }
        else {
            return pattern.matcher("").matches();
        }
    }
}
