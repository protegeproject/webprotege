package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.NoteField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public class NoteContentQuery implements Serializable {

    private Map<NoteField<?>, NoteFieldMatcher<?>> matcherMap = new HashMap<NoteField<?>, NoteFieldMatcher<?>>();

    private NoteContentQuery(Map<NoteField<?>, NoteFieldMatcher<?>> matcherMap) {
        this.matcherMap = matcherMap;
    }

    private NoteContentQuery() {
    }

    public Collection<NoteField<?>> getFields() {
        return new ArrayList<NoteField<?>>(matcherMap.keySet());
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Map<NoteField<?>, NoteFieldMatcher<?>> map = new HashMap<NoteField<?>, NoteFieldMatcher<?>>();

        private Builder() {
        }

        public <T extends Serializable> Builder setMatcher(NoteField<T> noteField, NoteFieldMatcher<T> fieldMatcher) {
            map.put(noteField, fieldMatcher);
            return this;
        }

        public NoteContentQuery build() {
            return new NoteContentQuery(map);
        }

    }
}
