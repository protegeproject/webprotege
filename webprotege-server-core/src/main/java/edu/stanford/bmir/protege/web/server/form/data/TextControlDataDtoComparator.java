package edu.stanford.bmir.protege.web.server.form.data;

import edu.stanford.bmir.protege.web.shared.form.data.TextControlDataDto;

import javax.inject.Inject;
import java.util.Comparator;

public class TextControlDataDtoComparator implements Comparator<TextControlDataDto> {

    @Inject
    public TextControlDataDtoComparator() {
    }

    @Override
    public int compare(TextControlDataDto o1, TextControlDataDto o2) {
        return o1.compareTo(o2);
    }
}
