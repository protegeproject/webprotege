package edu.stanford.bmir.protege.web.shared.genericframe;

import edu.stanford.bmir.protege.web.shared.genericframe.grammar.SectionGrammar;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/02/2013
 */
public class FrameSection<T> {

    private FrameSectionId id;

    private String displayName;

    private FrameSectionType<T> frameSectionType;

    private FrameSection() {
    }

    public FrameSection(FrameSectionId id, String displayName, FrameSectionType<T> frameSectionType) {
        this.id = id;
        this.displayName = displayName;
        this.frameSectionType = frameSectionType;
    }

    public FrameSectionId getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public FrameSectionType<T> getFrameSectionType() {
        return frameSectionType;
    }

    public SectionGrammar getGrammar() {
        return null;
    }

    @Override
    public int hashCode() {
        return "FrameSection".hashCode() + id.hashCode() + displayName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof FrameSection)) {
            return false;
        }
        FrameSection other = (FrameSection) obj;
        return this.id.equals(other.id) && this.displayName.equals(other.displayName);
    }
}
