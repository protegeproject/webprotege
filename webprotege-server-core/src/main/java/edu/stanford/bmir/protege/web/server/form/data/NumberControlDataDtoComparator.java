package edu.stanford.bmir.protege.web.server.form.data;

import edu.stanford.bmir.protege.web.shared.form.data.NumberControlDataDto;

import javax.inject.Inject;
import java.util.Comparator;

public class NumberControlDataDtoComparator implements Comparator<NumberControlDataDto> {

    @Inject
    public NumberControlDataDtoComparator() {
    }

    @Override
    public int compare(NumberControlDataDto o1, NumberControlDataDto o2) {
        return o1.compareTo(o2);
    }
}
