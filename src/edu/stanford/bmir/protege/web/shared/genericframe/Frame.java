package edu.stanford.bmir.protege.web.shared.genericframe;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/02/2013
 */
public class Frame<S> {

    private S subject;

    private List<FrameSection> sections = new ArrayList<FrameSection>();

    public S getSubject() {
        return subject;
    }

    public List<FrameSection> getSections() {
        return new ArrayList<FrameSection>(sections);
    }


    public <T> List<T> getFrameSectionValues(FrameSection<T> type) {
        return null;
    }

    public <T> void addFrameSectionValue(FrameSection<T> type, T value) {

    }
}
